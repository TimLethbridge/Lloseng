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
  
  public ChatClient(String loginid, String host, int port, ChatIF clientUI)
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();

    this.sendToServer("#login " + loginid);

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
    if(message.startsWith("#")){
      String[] splitMessage = message.split(" ");
      switch (splitMessage[0]){
        case "#quit":
          System.out.println("Quitting server...");
          quit();
          break;
        case "#logoff":
          try
          { 
            System.out.println("Logging off..."); 
            closeConnection();
            System.out.println("Connection closed.");}
          catch (IOException e)
          { System.out.println("There was an error trying to close connection."); }
          break;
        case "#sethost":
          if(!isConnected()){
            this.setHost(splitMessage[1]);
            System.out.println("New host has been set.");
          }
          else{
            System.out.println("You cannot set host while connected.");
          }
          break;
        case "#setport":
          if(!isConnected()){
            this.setPort(Integer.parseInt(splitMessage[1]));
            System.out.println("New port has been set.");
          }
          else{
            System.out.println("You cannot set port while connected.");
          }
          break;
        case "#login":
          try
          { openConnection();
            System.out.println("The connection has been opened.");}
          catch (IOException e)
          { System.out.println("There was an error trying to open connection."); }
          break;
        case "#gethost":
          System.out.println("Currrent host: " + getHost());
          break;
        case "#getport":
          System.out.println("Currrent port: " + getPort());
          break;
        default:
          System.out.println("Invalid command.");
          break;
      }
    }
    else{
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
    }
   

  // quit if the connection is closed
  public void ConnectionClosed(){
    System.out.println("The server has shut down.");
  }

  // quit if there is a connection exception
  public void connectionException(Exception exception) {
    System.out.println("There has been a connection error. Shutting down...");
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
