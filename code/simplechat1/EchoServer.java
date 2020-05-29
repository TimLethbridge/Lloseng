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
    String[] splitMessage = msg.split(" ");
    if(splitMessage[0]=="#login"){
      if(client.getInfo("loginid") == null){
        client.setInfo(splitMessage[1]);
      }
      else{
        client.sendToClient("Your login ID has already been set.");
      }
    }
    else if(client.getInfo("loginid")==null){
      client.sendToClient("ERROR: You must set a login ID first.");
      close();
    }
    System.out.println("Message received: " + msg + " from " + client.getInfo("loginid"));
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

  @Override
  public void clientConnected(){
    System.out.println
            (client + " has connected to the chat.");
  }

  @Override
  public void clientDisconnected(){
    System.out.println
            (client + " has disconnected from the chat.");
  }

  public void handleMessageFromServer(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
              ("Could not send message to server.  Terminating client.");
      quit();
    }

    String firstChar = message[0];
    if(firstChar == "#"){
      String[] splitMessage = message.split(" ");
      switch (splitMessage[0]){
        case "#quit":
          this.stop();
          break;
        case "#stop":
          this.stopListening();
          break;
        case "#close":
          this.stopListening();
          try {
            this.close();
          }
          catch(IOException exception){
            System.out.println("An error occurred when trying to close the server.");
          }
          break;
        case "setport":
          if (!this.isListening()) {
            super.setPort(Integer.parseInt(parameters[1]));
            System.out.println("New port: " + Integer.parseInt(parameters[1]));
          } else {
            System.out.println("An error occurred when changing the port.");
          }
          break;
        case "#start":
          if (!this.isListening()) {
            this.startListening();
          }
          else{
            System.out.print("The server has already started.");
          }
          break;
        case "#getport":
          System.out.print("Current port: " + this.getPort());
          break;
        default:
          System.out.println("Invalid command.");
          break;
      }
    }

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
