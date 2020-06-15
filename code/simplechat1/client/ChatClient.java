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
  String loginid;
  
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
    this.loginid=loginid;
    try
    {openConnection();
    sendToServer("#login ID: " + loginid);
    }catch(IOException e){
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
    try    {
     if(message.startsWith("#")){
       switch(message){
         case "#quit":
         quit();
         case "#logoff":
         quit();
         case "#sethost":
         if(!isConnected()){
         try{
           String host = message.substring(0);
           setHost(host);
           System.out.println("Host is set to: "+host);
         }catch(Exception e) {
          clientUI.display("invalid host, please try again");
          return;
         }
        }else{
          clientUI.display("You need to log off before set host");
          return;
         }
         break;
         case "#setport":
         if(!isConnected()){
         try{
           int port =Integer.parseInt(message.substring(0));
           setPort(port);
           System.out.println("Port is set to: "+ port);
         }catch(Exception e) {
          clientUI.display("invalid port, please try again");
          return;
         }
        }else{
          clientUI.display("You need to log off before set port");
          return;
         }
         break;
         case "#login":
         if(!isConnected()){
         try{
           openConnection();
           clientUI.display("^^ logging you in ...^^");
         }catch(Exception e) {
          clientUI.display("Exception occurred when log in. please try again");
          return;
         }
        }else{
          clientUI.display("You already logged in.");
          return;
         }
         break;
         case "#gethost":
         clientUI.display("Your current host is: " + getHost());
         break;
         case "#getport":
         clientUI.display("Your current port is: " + getPort());
         break;
       }
     }else

     
      sendToServer(message);


    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  public void connectionClosed(){
    try {
			if (!isConnected()) {
				closeConnection();
			}
		} catch (IOException e) {
      connectionException(e);
      clientUI.display("Abnormal termination of connection.");
    }
  }
  
    /* @param exception
     *            the exception raised.
     */
    public void connectionException(Exception exception) {
    
      clientUI.display("Abnormal termination of connection.");
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
      clientUI.display("client quits");
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
