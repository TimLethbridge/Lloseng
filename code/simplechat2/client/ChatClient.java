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
  public String loginID;

  
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
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
    sendToServer("#login " + loginID);
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
    
    char commandKey = "#".charAt(0);

    if(message != null && message.charAt(0) == commandKey){

      parseClientCommand(message);

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

  private void parseClientCommand(String message){

    String[] messageList = message.split(" ");

   switch(messageList[0]){
      case "#quit":
        quit();
        break;

      case "#logoff":
        if(!isConnected()){
          System.out.println("Error: Client Not Connected to a Server");
        }
        else{
          try{
          closeConnection();
          }
          catch(IOException e){
            System.out.println(e.toString());
          }
        }
        break;

      case "#sethost":
        if(!isConnected()){
          setHost(messageList[1]);
        }
        else{
          System.out.println("Error: Cannot Change Host While Connected to a Server");
        }
        break;

      case "#setport":

        if(!isConnected()){
          setPort(Integer.parseInt(messageList[1]));
        }
        else{
          System.out.println("Error: Cannot Change Port While Connected to a Server");
        }
          
        break;

      case "#login":
        
        if(!isConnected()){
          try{
          openConnection();
          sendToServer("#login " + loginID);
          }
          catch(IOException e){
            System.out.println(e.toString());
          }
        }
        else{
          System.out.println("Error: Cannot login with an open connection");
        }
        
        break;

      case "#gethost":
        System.out.println("Host: " + this.getHost());
        break;

      case "#getport":
        System.out.println("Port: " + this.getPort());
        break;
     
      default:
        System.out.println("Error: Command Not Recognized");

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

  //Terminates Client when server terminates
  public void connectionException(Exception Error){

    System.out.println("The server has closed");
    quit();

  }

  //Displays Message upon client termination
  public void connectionClosed(){
    
  }

}
//End of ChatClient class
