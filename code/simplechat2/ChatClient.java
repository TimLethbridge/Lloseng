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
public class ChatClient extends AbstractClient{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  private ChatIF clientUI;
  private String loginID = ""; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   * @param loginID User entered loginID through cmd-line args
   */

  
  
  public ChatClient(String host, int port, ChatIF clientUI, String loginID) throws IOException{
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI; //assign rest of instance variables
    this.loginID = loginID;
    if (loginID.equals("")){ //if login ID is blank, refuse connection
      clientUI.display("Error - No login ID specified. Connection aborted.");
      quit();
    }
    else{ //if login ID was entered, connect, and send loginID to server
      
      try{
      openConnection();
      sendToServer("#login "+loginID);
      clientUI.display(loginID+" has logged on.");
    }
    catch(Exception ex){clientUI.display("Cannot open connection. Awaiting command");}
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
  public void handleMessageFromClientUI(String message){
    try{
      //series of commands user can enter through the ClientUI
      if (message.equals("#quit")){
        quit();
      }

      else if ( message.equals("#logoff") ){
        //if already not connected, display error message
        if (!isConnected()){
          clientUI.display("You are not connected to the server.");
        }
        else{
          closeConnection();
          clientUI.display("You have been disconnected from "+getHost()+".");
          }
      }

      else if (message.equals("#login")){
        //if you are already logged in, display error message.
        if (isConnected()){
          clientUI.display("You are already connected.");
        }
        else{
          openConnection();
          clientUI.display("You have connected to "+getHost()+".");
        }
      }

      else if (message.equals("#gethost")){
        clientUI.display("Current host: "+getHost());
      }

      else if (message.equals("#getport")){
        clientUI.display("Current port: "+getPort());
      }

      else if (message.contains("#setport")){
        if (!isConnected()){
          int newPort = Integer.parseInt( message.substring(9) );
          setPort(newPort);
          clientUI.display("Port set to: "+getPort());
        }
        else{
          clientUI.display("Cannot set port while connected to server.");
        }
      }


      else if (message.contains("#sethost")){
        if (!isConnected()){
          String newHost =  new String();
          newHost = message.substring(9) ;
          setHost(newHost);
          clientUI.display("Host set to: "+getHost());
        }
        else{
          clientUI.display("Cannot set host while connected to server.");
        }
      }
    //if it is not a command, treat as a regular message to server
    else{
    sendToServer(message);}
    }
    
    //catch exceptions and disconnect from server
    catch(IOException e){
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    }
  }


  //disconnect from server if server shuts down while connected.
  public void connectionException(Exception exception){
    String exceptionString = exception.toString();
    System.out.println("Abnormal termination of connection.");
    
  }
  
  /**
   * This method terminates the client.
   */
  public void quit(){
    try{
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
