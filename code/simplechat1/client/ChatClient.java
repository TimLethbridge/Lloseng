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
    private String userId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, String userId)
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
      this.userId = userId;
      
      try
      {
        openConnection();

          if(userId==""){
              System.out.println("ERROR - No login ID specified.  Connection aborted.");
              quit();
          }
          else{
              System.out.println(userId+" has logged on.");
          }
      }
      catch(IOException exception)
      {
        System.out.println("Cannot open connection.  Awaiting command.");
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
    
    
    //print message when server disconnect
    public void connectionException(Exception exception){
        System.out.println("The server closed, quit connection");
        quit();
    }
    //when connection closed print message
    public void connectionClosed() {
        System.out.println("Connection Closed");
    }
    public String getuserId(){
        return userId;
    }
    public void setuserId(String userId){
        this.userId = userId;
    }
    
    
}
//End of ChatClient class
