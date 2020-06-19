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
  String loginID;
  
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
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if (message.startsWith("#")) {
      String[] cmd = message.split(" ");
      switch (cmd[0]) {
        case "#quit":
          clientUI.display("Terminating connection.");
          quit();
          break;
        case "#logoff":
          try {
            clientUI.display("Logging off.");
            closeConnection();
          } catch(IOException e) {
            System.out.println("Error logging off.");
          }
        case "#sethost":
          if (!isConnected()) {
            this.setHost(cmd[1]);
            clientUI.display("New host has been set.");
          } else {
            clientUI.display("Must be logged off.");
          }
          break;
        case "#setport":
          if (!isConnected()) {
            this.setPort(Integer.parseInt(cmd[1]));
            clientUI.display("New port has been set.");
          } else {
            clientUI.display("Must be logged off.");
          }
          break;
        case "#login":
          try {
            openConnection();
          } catch (IOException e) {
            System.out.println("Must not be connected.");
          }
          break;
        case "#gethost":
          clientUI.display("Currect host: " + this.getHost());
          break;
        case "#getport":
          clientUI.display("Current port: " + this.getPort());
          break;
      }
    } else {
        try {
          sendToServer(message);
        } catch (IOException e) {
          clientUI.display
            ("Could not send message to server.  Terminating client.");
          quit();
        }
      }
    }

  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	public void connectionClosed() {
      clientUI.display("The connection with the server has ended.");
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	public void connectionException(Exception exception) {
      clientUI.display("Server has stopped communicating. Shutting down.");
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
