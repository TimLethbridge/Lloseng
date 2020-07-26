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
public class ChatClient extends AbstractClient {
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
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) throws IOException   {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
	openConnection();
	sendToServer("#login " + loginID);
  }

  
  //Instance methods ************************************************
    
  	/**
	* This method handles all data coming from the UI            
	*
	* @param message The message from the UI.    
	*/
	public void handleMessageFromClientUI(String message) {
		// Anything starting with "#" is considered to be a command
		if (message.startsWith("#")) {
			String[] splitMsg = message.split(" ");
			switch (splitMsg[0]) {
				// Client terminates gracefully
				case "#quit":
					System.out.println("Commanded to quit.");
					quit();
					break;
				// Client disconnects from server, but not quit the program
				case "#logoff":
					System.out.println("Commanded to log off.");
					try {
						closeConnection();
					}
					catch (IOException e) {
						System.out.println("Error. Cannot log off.");
					}
					break;
				// Sets server host for next connection, but only if client is logged off
				case "#sethost":
					if (!isConnected()) {
						System.out.println("Commanded to set host.");
						setHost(splitMsg[1]);
					} else {
						System.out.println("Error. Cannot set host because the client is still logged on.");
					}
					break;
				// Sets server port number for next connection, but only if client is logged off
				case "#setport":
					if (!isConnected()) {
						System.out.println("Commanded to set port.");
						setPort(Integer.parseInt(splitMsg[1]));
					} else {
						System.out.println("Error. Cannot set port because the client is still logged on.");
					}
					break;
				// Connects client to server, but if not already connected
				case "#login":
					try {
						System.out.println("Commanded to log in.");
						openConnection();
					} catch (IOException e) {
						System.out.println("Error. Cannot connect client to server because they are already connected.");
					}
					break;
				// Display current host name
				case "#gethost":
					System.out.println("Host name: " + getHost());
					break;
				// Display current port number
				case "#getport":
					System.out.println("Port number: " + getPort());
					break;
				// Command not recognized
				default:
					System.out.println("Command not recognized");
					break;
			}
		} else {
			try {
				sendToServer(message);
			}
			catch(IOException e) {
				clientUI.display
				("Could not send message to server.  Terminating client.");
				quit();
			}
		}
	}
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)   {
    clientUI.display(msg.toString());
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

	// The connection is closed
	public void connectionClosed() {
		System.out.println("Connection closed.");
	}

	// Exception handling if connection is closed
	public void connectionException(Exception exception) {
		System.out.println("Connection error: server closed. Closing program.");
		quit(); // Exits the program gracefully
	}

}
//End of ChatClient class
