// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import common.*;
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
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
   
   //when an exception is thrown, it calls the clientDisconnected hook method to print the appropriate message.
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
		clientDisconnected(client);
	}
  
    /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
   
   //prints a message when the client logs on.
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client has logged on");
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
   
   //prints a message when the client logs off 
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client has logged off");
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
      ("Server has stopped listening for new connections.");
  }
  
    /**
   * Hook method called when the server stops accepting
   * connections because an exception has been raised.
   * The default implementation does nothing.
   * This method may be overriden by subclasses.
   *
   * @param exception the exception raised.
   */
  protected void listeningException(Throwable exception) {
  }


  /**
   * Hook method called when the server is clased.
   * The default implementation does nothing. This method may be
   * overriden by subclasses. When the server is closed while still
   * listening, serverStopped() will also be called.
   */
  protected void serverClosed(){
  System.out.println
      ("Server has closed all connections."); }
  
  public void commandHelperMethod(String message){
		
		//seperates the command from the arguments (if pertinent - for #sethost and #setport)
		String[] argumentsArray = message.split(" ");
		
		switch(argumentsArray[0]){
			
			case "#quit": //calls the quit method to logoff without having to use ctrl+c 
				try{
				close();
				}
				catch(IOException e){
				
				}
				System.out.print("Shutting down server");
				System.exit(0);
				break;
	
			case "#stop":
				stopListening();
				break;
				
			case "#close":
				try{
				close();
				}
				catch(IOException e){
				
				}
				break;
				
			case "#setport": //allows the user to change the port (only available when not connected to a server)
				if(!isListening()&&getNumberOfClients()==0){
					try{
						setPort(Integer.parseInt(argumentsArray[1]));
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("Port number not specified. Please use the following format: #setport <portnumber> ");
					}
				}
				else{
					System.out.println("The server is currently open. You must first close the server using #close");
				}
				break;
							
			case "#start":
				try{
				listen();
				}
				catch(IOException e){
				
				}
				break;
 							
			case "#getport": // prints the port number
				System.out.println("The port number is: "+Integer.toString(getPort()));
				break;			
			
			default: //if someone typed the wrong command they would get an error message and a list of accepted commands
				System.out.println("This is not a recognised command");
				System.out.println("Please use one of the following:");
				System.out.println("#quit, #stop, #close, #setport, #start or #getport");
				break;
		}
		
	}
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message)
  {
	//Checks if the message is a command and redirects it to the helper method
	if(message.charAt(0)=='#'){
		commandHelperMethod(message); 
	}
	else{
		sendToAllClients("SERVER MSG> "+message);
		  //makes the messages comming from the server begin with 'SERVER MSG>'
		System.out.println("SERVER MSG> " + message); 
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
