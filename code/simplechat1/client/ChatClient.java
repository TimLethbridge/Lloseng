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


   */
  public void handleMessageFromClientUI(String message){

    if (message.equals("#quit")) {
      clientUI.display("Quitting...");
      quit();
    }

    else if (message.equals("#logoff")) {
      try {
        closeConnection();
      }
      catch (IOException ignored){
        return;
      }
      
    }

    else if (message.startsWith("#sethost")) {
      if (isConnected()) {
        clientUI.display("Invalid command\n Can not set host once logged in\nPlease log out first with command #logoff");
        return;
      }
      
      else if (!isConnected()) {
        try{
          String host = message.split("\\s+")[1];
          setHost(host);
          clientUI.display("Host server has been set to: " +host);
        }
        catch(Exception e){
          clientUI.display("Invalid host server.");
          return;
        }  
      }     
    }

    else if (message.startsWith("#setport")) {
      if (isConnected()) {
        clientUI.display("Invalid command\n Can not set port once logged in\nPlease log out first with command #logoff");
        return;
      
      }

      else if (!isConnected()) {
        try{
          int port = Integer.parseInt(message.split("\\s+")[1]);
          setPort(port);
          clientUI.display("Port has been set to: " +port);
        }
        catch(Exception e){
          clientUI.display("Invalid port.");
          return;
        }  
      }
    }

    else if (message.equals("#login")) {
      if (isConnected()) {
        clientUI.display("Invalid command\n Can not log in once logged in\nPlease log out first with command #logoff");
        return;
      }

      else if (!isConnected()) {
        try{
          openConnection();
          clientUI.display("Loggin in.");
        }
        catch(IOException e){
          clientUI.display("Unable to log in.");
        }
      }
    }

    else if (message.equals("#gethost")) {
      clientUI.display("Host: "+getHost());
    }
    else if (message.equals("#getport")) {
      clientUI.display("Port: "+getPort());
    }
    else if (message.startsWith("#")) {
      clientUI.display("Invalid command. Please try again.\nPossible commands are :\n\n#quit\t: to quit\n#logoff\t: to log off\n#sethost\t: to set host if logged off\n#setport\t: to set port if logged off\n#login\t: to log in if not logged in already\n#gethost\t: to get the current host\n#getport\t: to get the current port");
    }
    else{
      try{
        sendToServer(message);
      }
      catch(IOException e){
        clientUI.display("Could not send message to server.\nTerminating client.");
        quit();
      }
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
    clientUI.display("The connection to the server has been closed.");
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
    clientUI.display("The server has shut down. \nQuitting...");
    quit();
  }

  /**
   * Hook method called after a connection has been established. The default
   * implementation does nothing. It may be overridden by subclasses to do
   * anything they wish.
   */
  protected void connectionEstablished() {
    clientUI.display("The connection to the server has been established.");
  }

  
}
//End of ChatClient class




























