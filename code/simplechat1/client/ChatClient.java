// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.net.ConnectException;

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
  String loginId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginId, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginId = loginId;
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
    try
    {
      String[] msg = message.split(" ");
      switch(msg[0]) {
        case "#quit":
          quit();
          break;
        case "#logoff":
          clientUI.display("Logging off.");
          closeConnection();
          break;
        case "#sethost":
          if (isConnected() == false) {
            try {
              setHost(msg[1]);
              clientUI.display("Host set to " + msg[1]);
            } catch (NumberFormatException e) {
              clientUI.display("Could not set host.");
            }
          }
          break;
        case "#setport":
          if (isConnected() == false) {
            try {
              setPort(Integer.parseInt(msg[1]));
              clientUI.display("Port set to " + msg[1]);
            } catch (NumberFormatException e) {
              clientUI.display("Could not set port.");
            }
          }
          break;
        case "#login":
          try {
            try {
              clientUI.display("Logging in.");
            } catch (Exception ex) {
              clientUI.display("ERROR -  No login ID specified.");
            }
            openConnection();
            sendToServer(msg[0] + " " + msg[1]);
          } catch (Exception e) {
            clientUI.display("ERROR -  No login ID specified.");
          }
          break;
        case "#gethost":
          clientUI.display(getHost());
          break;
        case "#getport":
          clientUI.display(Integer.toString(getPort()));
          break;
        default:
          sendToServer(message);
          break;
      }
      
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

  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
    clientUI.display("Connection closed!");
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
    clientUI.display("Abnormal termination of connection.");
    System.exit(0);
	}

	/**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
	protected void connectionEstablished() {
    clientUI.display("Connection established!");
  }
}
//End of ChatClient class
