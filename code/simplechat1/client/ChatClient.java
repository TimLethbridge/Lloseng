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
      if(message.contains("#")){
        switch(message) {
          case message.contains("quit"):
          quit();
          break;

          case message.contains("logoff"):
          closeConnection();
          break;

          case message.contains("sethost"):
          if(isConnected()){
            clientUI.display("This operation is unavailable while logged on.");
          }else{
            setHost(message.replace("#sethost",""));
          }
          
          break;

          case message.contains("setport"):
          if(isConnected()){
            clientUI.display("This operation is unavailable while logged on.");
          }else{
            setPort(message.replace("#setport",""));
          }
          
          break;

          case message.contains("login"):
          if(isConnected()){
            clientUI.display("This operation is unavailable while logged on.");
          }else{
            openConnection();
          }
          break;

          case message.contains("gethost"):
          clientUI.display(getHost());;
          break;

          case message.contains("getport"):
          clientUI.display(getPort().toString());;
          break;

 
}

      }else{
        sendToServer(message);
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

  public void connectionClosed() {
    clientUI.display("The connection has been closed");
  }

  protected void connectionException(Exception exception) {
    clientUI.display("An error has occured, the connection has been closed");
  }
}
//End of ChatClient class
