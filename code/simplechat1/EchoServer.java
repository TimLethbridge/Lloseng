// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

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
public class EchoServer extends AbstractServer 
{
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
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    String message = msg.toString();
    if (message.startsWith("#")) {
      String[] param = message.substring(1).split(" ");

      if (param[0].equals("login") && param.length > 1) {
        if (client.getInfo("username") == null) {
          client.setInfo("username", param [1]);
          System.out.println("Received message #login " + param[1]);
          this.sendToAllClients(param[1] + "has logged onto the server.");
        }

        else {
          try {
            client.sendToClient("You already have a username. Couldn't execute commmand.");
          }
          catch (IOException e) {
        }
        }
      }
    }
    else {
      if (client.getInfo("username") == null) {
        try {
          client.sendToClient("You must set a username before sending messages to the server.");
          client.close();
        }
        catch (IOException e) {}
      }
      else {
        System.out.println("Message received: " + msg + " from " + client.getInfo("username"));
        this.sendToAllClients(client.getInfo("username") + " > " + message);
      }
    }
  }

  public void handleMessageFromServer(String messages){
    if (messages.startsWith("#")) {
      String[] parameters = messages.split(" ");


      String consoleCommand = parameters[0];

      //we go through all cases for potential commands if the console line starts with a #.
      switch (consoleCommand) {
        case "#quit":
          //closes the server and then exits it
          try {
            this.close();
          } catch (IOException e) {
            System.exit(1);
          }
          System.exit(0);
          break;
        case "#close":
          try {
            this.close();
          } catch (IOException e) {
          }
          break;
        case "#stop":
          this.stopListening();
          break;
        case "#start":
          if (!this.isListening()) {
            try {
              this.listen();
            } catch (IOException e) {
              //error listening for clients
            }
          } else {
            System.out.println("The Server is already listening for connections.");
          }
          break;
        case "#setport":
          if (!this.isListening() && this.getNumberOfClients() < 1) {
            super.setPort(Integer.parseInt(parameters[1]));
            System.out.println("Port is set to " + Integer.parseInt(parameters[1]));
          } else {
            System.out.println("The Server is already connected.");
          }
          break;
        case "#getport":
          System.out.println("Current open port is " + this.getPort());
          break;

        default:
          System.out.println("Invalid command: '" + consoleCommand+ "' please try again.");
          break;
      }
    } else {//if it isnt a command, we simply send it to the other clients as a server message.

      messages = "SERVER MSG> " + messages;
      this.sendToAllClients(messages);
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  public void clientConnected(ConnectionToClient client) {
    System.out.println("New client connected to the Server!");
  }

  /**
   * Hook method called each time a client disconnects.
   * The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized public void clientDisconnected(ConnectionToClient client) {
    System.out.println(client.getInfo("username") + " has disconnected from the Server!");
    this.sendToAllClients(client.getInfo("username") + " has disconnected from the Server!");
  }
  synchronized public void clientException(
          ConnectionToClient client, Throwable exception) {
    System.out.println("Error in the client's connection to the Server!");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
