// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.sql.Connection;

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
    System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));

    char commandKey = "#".charAt(0);
    String msgString = msg.toString();

    if(msg!= null && msgString.charAt(0) == commandKey){
      handleClientLogin(msgString, client);
    }
    else{
      msg = client.getInfo("loginID") + "> " + msg;
      this.sendToAllClients(msg);
    }
  }

  private void handleClientLogin(String msg, ConnectionToClient client){

    String[] loginList = msg.split(" ");

    switch(loginList[0]){
      case "#login":
        if(client.getInfo("loginID") == null){
          client.setInfo("loginID", loginList[1]);

          String message = client.getInfo("loginID") + " has logged on.";
          System.out.println(message);
          sendToAllClients(message);
        }
        else{
          System.out.println("Error: Client Already Logged In");

          try{
          client.sendToClient("Error: Client Already Logged In");
          client.close();
          }
          catch(IOException e){
            System.out.println("Error: IOException Thrown on Client Invalid Login");
          }
          
        }
      break;

      default:
        System.out.println("Error: Unknown Client-Sent Command");
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
  
  public void clientConnected(ConnectionToClient client){

    System.out.println("A new client is attempting to connect to the server.");

  }

  //Displays a message to all clients when a client disconnects
  public void clientDisconnected(ConnectionToClient client){
    String message = client.getInfo("loginID") + " has disconnected";
    System.out.println(message);
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
        sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
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
        sendToAllClients("WARNING - The server has stopped listening for connections");
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

  public static void main(String[] args) 
  {
    int DEFAULT_PORT = 5555; //Default Port
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    ServerConsole chat = new ServerConsole(port);
    chat.accept();  //Wait for client data
  }
  
}
//End of EchoServer class
