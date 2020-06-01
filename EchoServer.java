// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import java.util.Arrays;
import java.util.List;

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
   * This method handles all data coming from the ServerConsole            
   *
   * @param message The message from the ServerConsole.    
   */
  public void handleMessageFromServerConsole(String message)
  {
	this.sendToAllClients(message);
	System.out.println(message);
  }
  
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
  
    /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) 
  {
	  System.out.println("SYSTEM ::: A new client connected ::: " + client);	  
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) 
	{
		System.out.println("SYSTEM ::: A client disconnected");
	}
	
	  /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) 
	{
		clientDisconnected(client);
	}
	
  /**
   * This method contains all the server commands.
   * Every commands begin with '#'.
   * 
   * @param command word that needs to be actioned.
   */
  public void serverCommand(String message){
	
	//Make the message received into an array (necessary for setHost/setPort)
	String[] messageArray = message.split(" ");
	String[] commandArray = new String[]{"#quit","#start","#stop","#close","#getport","#setport"};
	
	//If the command isn't in the 'known' list, give error message and command list.
	if (!Arrays.asList(commandArray).contains(messageArray[2])){
		System.out.println("The command line you entered is 'INVALID', here is a list of all current commands.");
		System.out.println("#quit // #start // #stop // #close // #getport // #setport");
	}
	
	switch(messageArray[2])	{
		case "#quit": //not done
			try
			{
			  close();
			}
			catch(IOException e) {System.out.println("exception");}
			System.exit(0);
			break;
			
		case "#start"://finished
			//what happens in #start
			if (!isListening())
			{
				try
				{
					listen();
				} 
				catch (IOException e) {}
			} break;
			
		case "#stop"://completed
			this.stopListening();
			break;
			
		case "#close"://completed
			System.out.println("SYSTEM ::: Server closed - Disconnect all current Client and Stop Listening for new Client");
			try
			{
			  this.stopListening(); 
			  this.close();
			}
			catch(IOException e) {}
			break;
			
		case "#getport"://completed
		
			try
			{
				System.out.println("SYSTEM ::: The current Port is: " + getPort());
			}
			
			catch (Exception a)
			{
				System.out.println("SYSTEM ::: There was an error in the getPort attempt");
			}
			break;
			
		case "#setport": //completed
			
			try {
				int newPort = Integer.parseInt(messageArray[3]);
				
				setPort(newPort);
				System.out.println("SYSTEM ::: The new Port has been set to: " + getPort());
			} catch (Exception ex2) 
			{
				System.out.println("SYSTEM ::: Array Out-of-Bound or your Port was not a valid Integer between 1 and 65535.");
			} break;
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
  public static void main(int port) 
  {
    //int port = 0; //Port to listen on

    // try
    // {
      // port = Integer.parseInt(args[0]); //Get port from command line
    // }
    // catch(Throwable t)
    // {
      // port = DEFAULT_PORT; //Set port to 5555
    // }
	
    //EchoServer sv = new EchoServer(port);
    
    // try 
    // {
      // sv.listen(); //Start listening for connections
    // } 
    // catch (Exception ex) 
    // {
      // System.out.println("ERROR - Could not listen for clients!");
    // }
  }
}
//End of EchoServer class
