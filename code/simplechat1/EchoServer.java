// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import common.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  ChatIF serverUI;
  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) {
    super(port);
    this.serverUI = serverUI;
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    serverUI.display("Message received: " + msg + " from " + client.getInfo("login"));
    if (msg.toString().charAt(0) == '#') {
      if (client.getInfo("Loggedin") == "Y") {
        try {
          client.sendToClient("You are already Logged  in");
          client.close();
        } catch (IOException exc) {
        }

      } else {
        String[] messages = msg.toString().split(" ");
        if(messages[0].equals("#login")){
        switch (messages[0]) {
          case ("#login"):
            client.setInfo("login", messages[1]);
            client.setInfo("Loggedin", "Y");
            break;
        }
        serverUI.display(messages[1] + " has logged on");
        sendToAllClients(messages[1] + " has logged on");
      }else{
        try{ 
        client.sendToClient("Invalid Command");
        }
        catch(IOException exc){}
      }
      }
      
    } else{
      
      this.sendToAllClients(client.getInfo("login") + ">" + msg);
    }

  }

  public void handleMessageFromServerUI(String message) throws IOException {
    String[] messages = message.split(" ");
    if (message.charAt(0) == '#') {
      switch (messages[0]) {
        case ("#quit"):
          quit();
          break;
        case ("#stop"):
          stopTheServer();
          break;
        case ("#close"):
          sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
          close();
          
          break;
        case ("#setport"):
          setPort(Integer.parseInt(messages[1]));
          serverUI.display("Port set to:" + messages[1]);
          break;
        case ("#start"):
          start();
          break;
        case ("#getport"):
          serverUI.display(Integer.toString(getPort()));
          break;
      }
    } else {
      display(message);

    }
  }
  public void start() throws IOException {
    listen();
  }

  synchronized protected void clientException(ConnectionToClient client,
  java.lang.Throwable exception) {
    serverUI.display(client.getInfo("login") + " has disconnected");
    sendToAllClients(client.getInfo("login") + " has disconnected");
  }

  public void display(String msg) {
    sendToAllClients("<SERVER MSG> " + msg);
  }

  synchronized protected void clientDisconnected(ConnectionToClient client) {
    serverUI.display(client.getInfo("login") + " has disconnected");
    sendToAllClients(client.getInfo("login") + " has disconnected");

  }

  protected void clientConnected(ConnectionToClient client) {
    serverUI.display("A new client is attempting to connect to the server.");
  }

  public void stopTheServer() {
    stopListening();
    sendToAllClients("WARNING - Server has stopped listening for connections."); // means stopped

  }

  public void quit() throws IOException {
    sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!"); // means closed
    close();
    System.exit(0);
  }

  /**
   * This method overrides the one in the superclass. Called when the server
   * starts listening for connections.
   */
  protected void serverStarted() {
    serverUI.display("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called when the server stops
   * listening for connections.
   */
  protected void serverStopped() {
    serverUI.display("Server has stopped listening for connections.");
  }
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number
    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
    try
    {
      host = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    ServerConsole chat= new ServerConsole(port);
    chat.accept();  //Wait for console data
  }
}

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of the server instance (there is
   * no UI in this phase).
   *
   * @param args[0] The port number to listen on. Defaults to 5555 if no argument
   *                is entered.
   */

  /*
   * public static void main(String[] args) { int port = 0; //Port to listen on
   * 
   * try { port = Integer.parseInt(args[0]); //Get port from command line }
   * catch(Throwable t) { port = DEFAULT_PORT; //Set port to 5555 }
   * 
   * EchoServer sv = new EchoServer(port);
   * 
   * try { sv.listen(); //Start listening for connections } catch (Exception ex) {
   * System.out.println("ERROR - Could not listen for clients!"); } }
   */

// End of EchoServer class
