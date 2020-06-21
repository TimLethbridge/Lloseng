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
  
  public ChatClient(String host, int port, ChatIF clientUI, String login) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID=login;
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
    //intercepts "commands" instead of echoing them.
    try{
      if(message.equals("#quit")){
        this.quit();
      } else if (message.equals("#logoff")) {
        try{
          closeConnection();
        }catch(IOException e){};
      } else if (message.equals("#login")) {
         if(!this.isConnected()){
          try{
            openConnection();
          }catch(IOException e){clientUI.display("Could not connect to server");}
        } else {
          clientUI.display("Error: Already logged in!");
        }
      } else if (message.substring(0,8).equals("#getport")) {
        clientUI.display(Integer.toString(this.getPort()));
      } else if (message.substring(0,8).equals("#gethost")) {
        clientUI.display(this.getHost());
      } else if (message.substring(0,8).equals("#sethost")) {
        if(!this.isConnected()){
          setHost(message.substring(9));
        } else {
          clientUI.display("Error: Cannot set host when connected.");
        }
      } else if (message.substring(0,8).equals("#setport")) {
        if(!this.isConnected()){
          setPort(Integer.parseInt(message.substring(9)));
        } else {
          clientUI.display("Error: Cannot set port when connected.");
        }
      }
    } catch (Exception e){}
    
    try
    {
      if((message.charAt(0))!='#'){
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

  @Override
  protected void connectionClosed(){
    clientUI.display("The connection has been closed.");
  }

  @Override
  protected void connectionException(Exception exception) {
    clientUI.display("Connection Exception.");
    quit();
  }
  protected void connectionEstablished() {
    try{
      sendToServer("#login"+loginID);
    }catch(Exception e){}
  }
}
//End of ChatClient class
