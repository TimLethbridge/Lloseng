// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version June 2020, edited by Morris Cai from Version. July 2000
 */

public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) {
    super(port);
  }

  /**
   * Implemented hook method called each time a new client connection is accepted.
   * Behavior modified to announce each time a new client is connected.
   * 
   * ex.
   * 
   * Client 127.0.0.1 (127.0.0.1) has Connected
   * 
   */
  public void clientConnected(ConnectionToClient client) {
    System.out.println("Client " + client.getInfo("loginID") + " has Connected");
  }

  /**
   * Implemented hook method called each time a client disconnects. Behavior
   * modified to announce each time a new client is disconnected.
   * 
   * ex.
   * 
   * Client 127.0.0.1 (127.0.0.1) has disconnected
   * 
   */
  public void clientDisconnected(ConnectionToClient client) {
    System.out.println("Disconnecting");
    System.out.println("Client " + client.getInfo("loginID") + " has Disconnected");
    sendToAllClients("Client " + client.getInfo("loginID") + " has Disconnected");
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    
    // Handles cases where message is null, signaling a client force quit, then
    // disconnects client
    if (msg == null) {
      clientDisconnected(client);
    }

    // Handles cases of login attempts, by looking for #login in substring(0,6)
    else if (msg.toString().length() > 9 && msg.toString().substring(0, 6).equals("#login")) {
      System.out.println("Message received: " + msg + " from; " + client.getInfo("loginID"));

      // Checks if the client has already logged in. If so, doesn't allow a second
      // login
      try {
        client.getInfo("loginID").toString();
        client.sendToClient("Only one login attempt is allowed per client.");
      } catch (Exception ex) {
        Object loginID = msg.toString().substring(8, msg.toString().length() - 1);
        client.setInfo("loginID", loginID);
      }
    }

    // Handles cases that are not login attempts
    else {
      // Checks that the client has logged in. If not,
      try {
        client.getInfo("loginID").toString();
        System.out.println("Message received: " + msg + " from; " + client.getInfo("loginID"));
        client.sendToClient(client.getInfo("loginID").toString() + "> " + msg);
      }

      //If the first client is not already logged in, which would trigger an exception in client.getInfo, 
      // tell the client that the login command must be the first entry
      catch (Exception ex) {
        try {
          client.sendToClient("SERVER MSG> First command must be #login, please try again");
          client.close();
        } catch (Exception ex2) {
          System.out.println("Error terminating connection with client");
        }
      }
    }
  }

  /**
   * This method overrides the one in the superclass. Called when the server
   * starts listening for connections.
   */
  protected void serverStarted() {
    System.out.println("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called when the server stops
   * listening for connections.
   */
  protected void serverStopped() {
    System.out.println("Server has stopped listening for connections.");
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of the server instance (there is
   * no UI in this phase).
   *
   * @param args[0] The port number to listen on. Defaults to 5555 if no argument
   *                is entered.
   */
  public static void main(String[] args) {
    int port = DEFAULT_PORT; // Port to listen on

    try {
      port = Integer.parseInt(args[0]); // Get port from command line
    } catch (Throwable t) {
      
    }

    EchoServer sv = new EchoServer(port);

    try {
      sv.listen(); // Start listening for connections
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }

    // Initiates and starts a serverConsole to allow for server communications.
    ServerConsole console = new ServerConsole(sv);
    console.startConsole();
  }
}
// End of EchoServer class
