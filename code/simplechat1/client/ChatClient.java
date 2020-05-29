// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

    /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	 
	 //cleanup method called whenever the user disconnects from the server.
	 //It prints a message telling the user so
	protected void connectionClosed() {
		System.out.println("You have logged off from the server.");
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	 
	 //When an exception is raised, it tells the user that they are no longer connected to the server
	 //then it closes the connection to the server formally
	 //without quitting (so that the user could log back on if they so chose
	protected void connectionException(Exception exception) {
		System.out.println("Connection to server terminated");
		try{closeConnection();	}
		catch(IOException e) {}
	}


	/**
	*
	*This method handles the case when a user enters a string starting with a #
	*It parses the message to see what command it was then it executes said command
	*
	* @param message The message from the UI.
	*/

    public void commandHelperMethod(String message){
		
		//seperates the command from the arguments (if pertinent - for #sethost and #setport)
		String[] argumentsArray = message.split(" ");
		
		switch(argumentsArray[0]){
			
			case "#quit": //calls the quit method to logoff without having to use ctrl+c 
				quit();
				break;
			
			case "#logoff": //allows the user to logoff from the server without exiting completely
				try{closeConnection();	}
				catch(IOException e) {}
				break;
				
			case "#sethost": //allows the user to change the host (only available when not connected to a server)
				if(!isConnected()){
					try{
					setHost(argumentsArray[1]);
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("Host name not specified. Please use the following format: #sethost <hostname> ");
					}
				}
				else{
					System.out.println("You are currently connected to a server. You must first #logoff to change hosts");
				}
				break;
				
			case "#setport": //allows the user to change the port (only available when not connected to a server)
				if(!isConnected()){
					try{
						setPort(Integer.parseInt(argumentsArray[1]));
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("Port number not specified. Please use the following format: #setport <portnumber> ");
					}
				}
				else{
					System.out.println("You are currently connected to a server. You must first #logoff to change ports");
				}
				break;
				
			case "#login": //allows the user to log into a server (cannot be used when already connected)
				if(!isConnected()){
					try{openConnection();	}
				catch(IOException e) {}
					
				}
				else{
					System.out.println("You are already connected to a server.");
				}
				break;	
				
			case "#gethost": //prints the host name
				System.out.println("The host name is: "+getHost());
				break;	
				
			case "#getport": // prints the port number
				System.out.println("The port number is: "+Integer.toString(getPort()));
				break;			
			
			default: //if someone typed the wrong command they would get an error message and a list of accepted commands
				System.out.println("This is not a recognised command");
				System.out.println("Please use one of the following:");
				System.out.println("#quit, #logoff, #sethost, #setport, #login, #gethost or #getport");
				break;
		}
		
	}



  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	//Checks if the message is a command and redirects it to the helper method
	if(message.charAt(0)=='#'){
		commandHelperMethod(message); 
	}
	else{
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
	}
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
