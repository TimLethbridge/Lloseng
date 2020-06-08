// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

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
  
  /**
   * LoginID for the client.
   */
   public String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
	this.loginID = loginID;
    this.clientUI = clientUI;
	
	try{
    openConnection();}
	catch (Exception e){}
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
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Cannot open connection.  Awaiting command.");
      //quit();
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
  
    	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
		System.out.println("Connection closed.");
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		//connectionClosed();
		System.out.println("SERVER SHUTTING DOWN! DISCONNECTING!");
		System.out.println("Abnormal termination of connection.");
		// quit();
	}
  
  	/**
	* This method contains all the client commands.
	* Every commands begin with '#'.
	* 
	* @param command word that needs to be actioned.
	*/
	public void clientCommand(String message){
		
		//Make the message received into an array (necessary for setHost/setPort)
		String[] messageArray = message.split(" ");
		String[] commandArray = new String[]{"#quit","#login","#logoff","#gethost","#sethost","#getport","#setport"};
		
		//If the command isn't in the 'known' list, give error message and command list.
		if (!Arrays.asList(commandArray).contains(messageArray[0])){
			System.out.println("The command line you entered is 'INVALID', here is a list of all current commands.");
			System.out.println("#quit // #login // #logoff // #gethost // #sethost // #getport // #setport");
		}
		
		switch(messageArray[0])	{
			case "#quit":
				quit();
				
			case "#login":
				//what happens in #login
				if (isConnected()){
					System.out.println("SYSTEM ::: The client is already connected");
					return;
				}
				else {
					try
					{
					openConnection();
					
						  try{
							  handleMessageFromClientUI("#login " + loginID);}
							  catch (NullPointerException f) {}

					}
					catch (IOException ex){System.out.println("SYSTEM ::: Failed to connect to Server");}
					return;
				}
				
			case "#logoff":
			    try
				{
					closeConnection();
					return;
				}
				catch(IOException e) {}
				return;
				
			case "#gethost":
				System.out.println("SYSTEM ::: The Host name is: " + getHost());
				return;
				
			case "#sethost":
			
				try 
				{
					setHost(messageArray[1]);
					System.out.println("SYSTEM ::: The new Host has been set to: " + getHost());
				} catch (Exception ex2) 
				{
					System.out.println("SYSTEM ::: Array Out of Bound: Please try again with the new Host name.");
				} return;
				
			case "#getport":
			
				try
				{
					System.out.println("SYSTEM ::: The current Port is: " + getPort());
				}
				
				catch (Exception a)
				{
					System.out.println("SYSTEM ::: There was an error in the getPort attempt");
				}
				return;
				
			case "#setport": 
				
				try {
					int newPort = Integer.parseInt(messageArray[1]);
					
					setPort(newPort);
					System.out.println("SYSTEM ::: The new Port has been set to: " + getPort());
				} catch (Exception ex2) 
				{
					System.out.println("SYSTEM ::: Array Out-of-Bound or your Port was not a valid Integer between 1 and 65535.");
				} return;
		}
	}
  
}
//End of ChatClient class
