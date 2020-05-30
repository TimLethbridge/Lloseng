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

  private String loginID;

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
    this.loginID = "Guest";
    openConnection();
    sendToServer("#loginID " + loginID);
  }
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
    sendToServer("#loginID " + loginID);
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

   // protected void connectionClosed()
   // {
   //   clientUI.display("Connection closed! Exiting..");
   // }

  protected void connectionException(Exception exception) 
  {
    clientUI.display("Connection to server closed.");
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
      if(message.charAt(0) == '#'){
        if(message.equals("#quit")){
          closeConnection();
          clientUI.display("Connection closed.");
          System.exit(0);
        }
        if(message.equals("#logoff")){
          try{
            closeConnection();
            clientUI.display("Connection closed.");
          }
          catch(Exception ex){
            clientUI.display("You have disconnected.");
          }
        }
        if(message.startsWith("#sethost")){
          if(!isConnected()){
            try{
              String h = message.substring(9); // hostname begins from index 9 to end (includes space)
              setHost(h);
              clientUI.display("New host set to " + getHost());
            }
            catch(Exception e){
              clientUI.display("Couldn't set host.. error occurred.");
            }
          }
          else{
            clientUI.display("Error: You must disconnect from the server.");
          }
        }
        if(message.startsWith("#setport")){
          if(!isConnected()){
            try{
              String p = message.substring(9); // same as above (for port however)
              setPort(Integer.parseInt(p));
              clientUI.display("New port set to " + getPort());
            }
            catch(Exception e){
              clientUI.display("Couldn't set port.. error occurred.");
            }
          }
          else{
            clientUI.display("Error: You must disconnect from the server.");
          }
        }
        if(message.startsWith("#login")){
          if(!isConnected()){
            loginID = message.substring(7);
            try{
              openConnection();
              sendToServer("#loginID " + loginID);
            }
            catch(Exception ex){
              clientUI.display("Error: Could not connect to server.");
            }
          }
          else{
            clientUI.display("Error: You must disconnect from the server.");
          }
        }
        if(message.equals("#gethost")){
          clientUI.display("Host: " + getHost());
        }
        if(message.equals("#getport")){
          clientUI.display("Port: " + getPort());
        }
      }
      else{
        sendToServer(message);
      }
    }
    catch(IOException e)
    {
      clientUI.display("Could not send message to server.  Terminating client.");
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
