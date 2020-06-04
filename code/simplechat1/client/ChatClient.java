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
  public String loginid;
  
  
  
  
  public ChatClient(String loginid, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
	this.loginid = loginid;
    openConnection();
	
	try{
		openConnection();
		sendToServer("#login " + loginid);
	}
	catch(IOException e){
		System.out.println(e);
	}
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
	char sharp = '#';
	if(message.charAt(0) == sharp && message != null){
		clientCommands(message);
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
  
  public void clientCommands(String command){
	  if (command.equals("#quit")){
		  System.out.println("Quitting...");
		  quit();
	  }
	  else if (command.equals("#logoff")){
		  try{
			  closeConnection();
		  }
		  catch(Exception e){
			  System.out.println(e);
		  }
	  }
	  else if(command.split(" ")[0].equals("#sethost")) {
		if(isConnected()){
			System.out.println("Please disconnected from current host before attempting to set a new host");
		}
		else{

			setHost(command.split(" ")[1]);

		}
	  }
	  
	  else if(command.split(" ")[0].equals("#setport")) {
		if(isConnected()){
			System.out.println("Please disconnected from current port before attempting to set a new port");
		}
		else{
			int port = Integer.parseInt(command.split(" ")[1]);
			setPort(port);
		}
	  }
	  else if(command.equals("#login")){
		  try{
			if(isConnected()){
				System.out.println("Already connected");
			}
			else{
				openConnection();
			}
		  }
		  catch(IOException e){
			  System.out.println(e);
		  }
	  }
	  else if(command.equals("#gethost")){
		  System.out.println(getHost());
	  }
	  else if(command.equals("#getport")){
		  System.out.println(getPort());
	  }
	  else if(!command.split(" ")[0].equals("#login")){
		  System.out.println("Command not recognized");
	  }
		  
  }
  
  public void connectionClosed() {
	  clientUI.display("Connection to server has been lost.");
	}
	
	public void connectionException(Exception exception) {
		System.out.println(exception);
		quit();
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
