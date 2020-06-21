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
  String loginid;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginid, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginid = loginid;
    this.clientUI = clientUI;
    openConnection();
    sendToServer("#login <" + loginid + ">");	
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
	//check if message is a command
			if(message.charAt(0) == '#') {
				try{
					String[] input = message.split(" ", 0);
					switch (input[0]) {

					case "#quit": quit();
					break;

					case "#logoff" : closeConnection();
					break;

					case "#sethost" :
						if(!isConnected()) {
							setHost(input[1]);
						}
						else {
							throw new IOException("You must logoff first before setting the host.");
						}
						break;

					case "#setport":
						if(!isConnected()) {
							setPort(Integer.parseInt(input[1]));
						}
						else {
							throw new IOException("You must logoff first before setting the port.");
						}
						break;

					case "#login":
						if(!isConnected()) {
							openConnection();
						}
						else {
							throw new IOException("You have not logged out.");
						}
						break;

					case "#gethost":
						clientUI.display("Host: "+ getHost());
						break;

					case "#getport":
						clientUI.display("Port: " + getPort());
						break;

					default:
						throw new IOException("Invalid Command"); 
					}
				}
				catch(IOException e) {
					System.exit(0);
				}
			}
			else {
				try
				{
					sendToServer(message);
				}
				catch(IOException e)
				{
					clientUI.display
					("Could not send message to server. Terminating client.");
					quit();
				}  
			}
  }
  
  /**
	 * This method closes the connection to the server.
	 *
	 * @param quit The decision to close connection.
	 */
	protected void connectionClosed(boolean quit) {
		if (quit) {
			System.exit(0);
		}
	}

	/**
	 * This method responds to the shutdown of the server.
	 *
	 * @param exception The exception to be handled before closing.
	 */
	protected void connectionException(Exception exception) {
		System.out.println("The connection to the server is now closing.");
		connectionClosed(true);
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
