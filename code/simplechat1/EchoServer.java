// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package simplechat1;

import simplechat1.common.*;
import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) {
    super(port);
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
  (Object msg, ConnectionToClient client) {

    String message = msg.toString();

    String[] idArray = message.split(" ");


    if (idArray[0].equals("#login")) {
      //System.out.println("h00");
      try {
        if (client.getInfo("loginId") != null) {
          client.sendToClient("Error - You has already logined.");
        } else {
          client.setInfo("loginId", idArray[1]);
          //System.out.println("h01");
          System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));
          //System.out.println("h02");
          client.setInfo("loginId", idArray[1]);
          //System.out.println("h03");
          System.out.println(client.getInfo("loginId") + " has logged on");
          this.sendToAllClients(client.getInfo("loginId") + " has logged on");
        }
      } catch(IOException e){

      }
      //
    }
    else{
      if (client.getInfo("loginId") == null){
        try {
          client.close();
        }
        catch (IOException e) {}
      }
      else{
        //System.out.println(client.getInfo("loginId"));
        //System.out.println(String.valueOf(client.getInfo("loginId ")));
        System.out.println
                ("Message received: " + msg + " from " + client.getInfo("loginId"));
        this.sendToAllClients(client.getInfo("loginId")+"> "+msg);
      }
    }
  }


  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted() {
    System.out.println
            ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped() {
    System.out.println
            ("Server has stopped listening for connections.");
  }

  //protected void serverClosed(){System.out.println("Server offline");}

  public void commandMethod(String message) throws IOException {

    //seperates the command from the arguments (if pertinent - for #sethost and #setport)

    String[] argumentsArray = message.split(" ");
    switch (argumentsArray[0]) {
      //System.out.println("123");

      //#quitCauses the server to quit gracefull
      case "#quit":

          stopListening();

        System.out.println("The server quits.");
        sendToAllClients("The server quits.");
        System.exit(0);
        break;

      //#stop Causes the server to stop listening for new clients.
      case "#stop":

        //System.out.println("Server has stopped listening for connections.");
        sendToAllClients("WARNING - Server has stopped listening for connections.");
        stopListening();break;


      //#close Causes the server not only to stop listening for new clients,
      // but also to disconnect all existing clients
      case "#close":
        try {
          sendToAllClients("WARNING - The server has stopped listening for connections");
          sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
          close();
        } catch (IOException e) {
        }
        System.out.println("Server offline");
        break;


      //#setport <port> Calls the setPort method in the server.
      // Only allowed if the server is close
      case "#setport":
        if (!isListening() && getNumberOfClients() == 0) {
          try {
            setPort(Integer.parseInt(argumentsArray[1]));
            System.out.println("port now is " + getPort());
          } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please use the following format: #setport <portnumber> ");
          }
        } else {
          System.out.println("The server is open. Please close the server first");
          System.out.println("You can use #close to close it");
        }
        break;

      //#start Causes the server to start listening for new clients.
      // Only valid if the server is stopped
      case "#start":
        listen();
        break;

      //#getport Displays the current port number
      case "#getport":
        System.out.println("The port number is: " + Integer.toString(getPort()));
        break;

      //if not the command has be setted
      default:
        System.out.println("This is not a acceptable command");
        System.out.println("Please use one of the following command:");
        System.out.println("#quit, #stop, #close, #setport, #start, #getport");
        break;
    }

  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromServerUI(String message) {
    //Checks if the message is start with "#" which is a command

    String[] argumentsArray = message.split(" ");
    if (argumentsArray[0].subSequence(0, 1).equals("#")) {
      try {
        commandMethod(message);
      } catch (Exception e) {

      }
    } else {

      sendToAllClients("SERVER MSG> " + message);
      //Any message originating from the end-user of the server
      // should be prefixed by the string "SERVER MSG> "
      System.out.println("SERVER MSG> " + message);
    }
  }

  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client is attempting to connect to the server.");
  }


  protected synchronized void clientDisconnected(ConnectionToClient client) {
    sendToAllClients(client.getInfo("loginId") + " has disconnected.");
    System.out.println(client.getInfo("loginId") + " has disconnected.");
  }


  protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
    clientDisconnected(client);
  }
  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555
   *                if no argument is entered.
   */
  public static void main(String[] args) {
    int port = 0; //Port to listen on

    try {
      port = Integer.parseInt(args[0]); //Get port from command line
    } catch (Throwable t) {
      port = DEFAULT_PORT; //Set port to 5555
    }

    EchoServer sv = new EchoServer(port);
    try {
      sv.listen(); //Start listening for connections
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }


  }
}
//End of EchoServer class
