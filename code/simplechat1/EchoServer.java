/*
* This is the edited version of the EchoServer class for the Assignment1. 
* All edits made specifically for the question number will be added as a comment
*/

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
//new edit #1 E6 b)
import ocsf.server.*;
import common.ChatIF;

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

  /**
  *new edit #2 E6b)
  *Instance variables
  */
  ChatIF serverUI;

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   * new edit #3 E6 b) line 48, line 51
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   * new edit #4 E6 b) 
   */
  public void handleMessageFromClient (Object msg, ConnectionToClient client)
  {
    String tmp = msg.toString();
    if (tmp.contains("#login")){
    	System.out.println(msg);
    	this.sendToAllClients("Server MSG>: "+tmp.split(" ")[1]+".");
    	client.setInfo("LoginID", tmp.split(" ")[1]);
    }else{
    	System.out.println("Message received: " + msg + " from " + client);
    	this.sendToAllClients(client.getInfo("Login ID ")+""+msg);
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   * new edit #5 E6 b)
   */
  protected void serverStarted()
  {
    System.out.println ("Server listening for connections on port " + getPort());
    try{
    	listen();
    }catch(IOException exception){
    	System.out.println("Error");
    }
  }


/**
* new edit #1 E5c)
* This method shows if the client has made a connection with the server
* @param client the client that is trying to make the connetion
* new edit #7 E6 b) line 100
*/
  protected void clientConnected(ConnectionToClient client){
  	System.out.println("A client is connected: "+client.getInfo("Login ID"));
  }

/**
* new edit #2 E5c)
* This method shows if the client has disconnected with the server
* @param client the client that has disconnected with the server
* new edit #8 E6 b) line 109
*/
  synchronized protected void clientDisconnected(ConnectionToClient client){
  	System.out.println("A client is disconnected: "+client.getInfo("Login ID"));
  }
  
/**
* new edit #3 E5c)
* This method is called whenever an exception is thrown
* @param client the client that had the exception
* new edit #9 E6 b) line 119
*/
  synchronized protected void clientException(ConnectionToClient client, Throwable exception){
  	System.out.println("Client error: "+client.getInfo("Login ID"));
  }



  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   * new edit #6 E6 b)
   */
  protected void serverStopped()
  {
  	stopListening();
    System.out.println("Server has stopped listening for connections.");
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
/*  public static void main(String[] args) 
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
}*/
//End of EchoServer class