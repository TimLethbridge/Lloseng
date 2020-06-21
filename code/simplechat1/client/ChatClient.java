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
   
  private boolean loggedOff;
  private static String loginID;
  ChatIF clientUI;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, String loginID, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
	this.loginID = loginID;
	
	try {
		openConnection();
		sendToServer("#login " + loginID);
		loggedOff = false;
	} catch(Exception e) {
		System.out.println("Cannot open connection.  Awaiting command.");
		loggedOff = true;
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
    try
    {
		String temp1[] = message.split(" ");
		
		if(temp1[0].equals("#quit")) {
			try {
				System.out.println("Closing Connection");
				quit();
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(temp1[0].equals("#logoff")) {
			try {
				loggedOff = true;
				sendToServer("client has logged off");
				System.out.println("Closing Connection");
				closeConnection();
			} catch(IOException e) {
				System.out.println("ERROR!");
			}
		} else if(temp1[0].equals("#sethost") && loggedOff) {
			try {
				System.out.println("Host set to: " + temp1[1]);
				setHost(temp1[1]);
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(temp1[0].equals("#setport") && loggedOff) {
			try {
				System.out.println("Port set to: " + Integer.parseInt(temp1[1]));
				setPort(Integer.parseInt(temp1[1]));
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(temp1[0].equals("#login") && loggedOff) {
			if(temp1.length == 2) {
				if(temp1[1].equals(loginID)) {
					try {
						System.out.println("Opening connection to server");
						openConnection();
						sendToServer("#login " + loginID);
						loggedOff = false;
					} catch(Exception e) {
						System.out.println("ERROR!");
					}
				} else
					System.out.println("Invalid Login ID!");
			} else
				System.out.println("Command must be followed by loginID i.e.: #login <loginID>");
		} else if(message.equals("#gethost")) {
			try {
				System.out.println(getHost());
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(message.equals("#getport")) {
			try {
				System.out.println(getPort());
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(temp1[0].charAt(0) == '#') {
			try {
				System.out.println("ERROR! Command not valid");
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else
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
  
   protected void connectionException(Exception e) {
	clientUI.display("Connection to Server Has Forcefully Terminated!");
	System.exit(0);
  }
  
  protected void connectionClosed() {
	clientUI.display("Connection Closed!");
  }
}
//End of ChatClient class
