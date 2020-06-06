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
	String message = msg.toString();
	if (message.contains("#server")) {
		int ind = message.indexOf('#');
		String mes = message.substring(0, ind);
		clientUI.display("SERVER MSG> " + mes);
	} 
	else
    		clientUI.display("> " + message);
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
	// Check if message is a command
	if (message.charAt(0) == '#') {
		// Split the message into two
		String[] mes = message.split(" ", 3);
		if (mes[0].equals("#login") || mes.length == 2) {
			sendToServer(message);
		}
		else {
			// Select appropriate function
			switch(mes[0]) {
				case "#quit":
					// Break the connection and close the client 
					closeConnection();
					System.exit(0); // Indicate succesful exit
					break;
				case "#logoff": 
					// Break the connection
					closeConnection();
					break;
				case "#sethost": 
					// Send the argument of the command to the function
					if (mes[1].equals(""))
						setHost("localhost");
					else
						setHost(mes[1]);
					break;
				case "#setport": 
					// Convert argument of the command to int and pass it
					try {
						setPort(Integer.parseInt(mes[1]));
					}
					catch (Exception e) {
						System.out.println("No port supplied. Using default....");
						setPort(5555);
					}
					break;
				case "#login": 
					// Open connection only if connection is closed
					if (!(isConnected()))
						openConnection();
					else 
						System.out.println("You must logoff first before login");
					break;
				case "#gethost": 
					System.out.println(getHost());
					break;
				case "#getport": 
					System.out.println(getPort());
					break;
				default:
					System.out.println("Command not valid!");
					break;
			}
		}
	}
	else
      		sendToServer(message); 
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
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
