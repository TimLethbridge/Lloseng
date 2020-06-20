/*
* This is the edited version of the ChatClient class for the Assignment1. 
* All edits made specifically for the question number will be added as a comment
*/

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
  //new edit #1 E7a)
  String loginId;

  
  //Constructors ****************************************************
  
/*
* This is the edited version of the ChatClient class for the Assignment1. 
* All edits made specifically for the question number will be added as a comment
*/

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   *
   * new edit #2 E7a) line 57, line 62
   * @param loginId The login ID of the client
   *
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
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display ("Could not send message to server.  Terminating client.");
      quit();
    }
  }

/**
* new edit #1 E5 a)
* This method lets the client know that the server has shut down
* and will be quitting/stopping the connection
*/
protected void connectionClosed(){
  System.out.println("The server has closed, will quit connection shortly");
}

/**
*new edit #3 E7a)
*This method returns the loginID of the client
*/
public String getLogin(){
  return loginId;
}

/**
*new edit #4 E7b)
*This method sends the loginID to the server.
*/
public void sendLogin(){
  handleMessageFromClientUI("#login: "+getLogin());
}

/**
*new edit #5 E7b)
*This method lets the server/client know that a connection has been established
*/
protected void connectionEstablish(){
  sendLogin();
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
