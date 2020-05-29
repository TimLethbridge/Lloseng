// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import common.*;
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
  ChatIF serverChat;
  boolean isClosed;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverChat) 
  {
    super(port);
    this.serverChat = serverChat;
    isClosed = false;
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
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
    isClosed = false;
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

  //Displays a message to all clients when a new client connects
  public void clientConnected(ConnectionToClient client){
    String message = client + " has connected!";
    sendToAllClients(message);
  }

  //Displays a message to all clients when a client disconnects
  public void clientDisconnected(ConnectionToClient client){
    String message = client + "has disconnected";
    sendToAllClients(message);
  }

  public void handleMessageFromServerUI(String message){
    
    char commandKey = "#".charAt(0);

    if(message != null && message.charAt(0) == commandKey){

      parseServerCommand(message);

    }
    else{
    message = "SERVER MSG> " + message;
    System.out.println(message);
    sendToAllClients(message);
    }
  }

  private void parseServerCommand(String command){

    String[] commandList = command.split(" ");

    switch(commandList[0]){

      case "#quit":
        System.exit(0);
      break;

      case "#close":
      try{
        close();
      }
      catch(IOException e){
        System.out.println("Error: Unknown IOException");
      }
      break;

      case "#setport":

        if(isClosed){

          try{
            setPort(Integer.parseInt(commandList[1]));
          }
          catch(IndexOutOfBoundsException e){
            System.out.println("Error: Port Not Specified");
          }
          catch(NumberFormatException ex){
            System.out.println("Error: Port Must be a Number");
          }

        }
        else{
          System.out.println("Error: Server Not Closed");
        }
      
      break;

      case "#getport":
        System.out.println("Port: " + getPort());
      break;

      case "#start":
        try{
          listen();
        }
        catch(IOException e){
          System.out.println("Error: Unknown IOException");
        }
      break;

      case "#stop":
        stopListening();
      break;

      default:
        System.out.println("Error: Command not Recognized");
      
    }

  }

  public void serverClosed(){
    isClosed = true;
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
}
//End of EchoServer class
