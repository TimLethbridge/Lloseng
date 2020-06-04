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
	char sharp = '#';
	String message = msg.toString();
	if(message.charAt(0) == sharp && message.split(" ")[0].equals("#login") && message.length() > 6){
		if(client.getInfo("loginID") == null){
		System.out.println(message.split(" ")[0] + message.split(" ")[1]);
		client.setInfo("loginID", message.split(" ")[1]);
		}
		else{
			try{
			client.sendToClient("You already have a login ID");
			}
			catch(IOException e){
				System.out.println(e);
			}
		}
	}
	else{
		System.out.println("Message received: " + msg + " from " + client);
		this.sendToAllClients(client.getInfo("loginID") + ": " + msg);
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
  
  public void handleMessageFromServerUI(String message)
  {	
	char sharp = '#';

	if(message.charAt(0) == sharp){
		serverCommands(message);
	}
	else{
		try
		{
			sendToAllClients("SERVER MESSAGE> " + message);
		}
		catch(Exception e)
		{
		  System.out.println(e);
		}
	}
  }
  
  public void serverCommands(String command){
	 if (command.equals("#quit")){
		  System.out.println("Quitting...");
		  System.exit(0);
	  }
	  else if(command.equals("#stop")){
		  stopListening();
	  }
	  else if(command.equals("#close")){
		  try{
			close();
		  }
		  catch(IOException e){
			  System.out.println(e);
		  }
	  }
	  else if(command.split(" ")[0].equals("#setport")){
		int setport = Integer.parseInt(command.split(" ")[1]);
		setPort(setport);
	  }
	  else if(command.equals("#start")){
		  if(!isListening()){
			  try{
				listen();
				System.out.println("Now listening");
			  }
			  catch(IOException e){
				 System.out.println(e);
			  }			
	  }
		  else{
			  System.out.println("Already listening");
		}
	  }
	  else if(command.equals("#getport")){
		  System.out.println(getPort());
	  }
	  else{
		  System.out.println("Command not recognized");
	  }
	  
		  
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
  
  protected void clientConnected(ConnectionToClient client){
	  System.out.println("A new client has connected to the server");
  }
  
  protected void clientDisconnected(ConnectionToClient client){
	  System.out.println("A client has disconnected");
  }
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
		System.out.println("Client has disconnected");
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
	
	ServerConsole chat = new ServerConsole(DEFAULT_PORT);
	chat.accept();
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
