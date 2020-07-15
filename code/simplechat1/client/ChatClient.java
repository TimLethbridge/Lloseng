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
  String userID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String username, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.userID = username;
    openConnection();

    this.sendToServer("#login " + username); //E6 login command
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    if (msg.toString().charAt(0) == '#') {
      return;
    }
    if (msg == null){
            System.out.println("WARNING - The server has stopped listening for connections. \nSERVER SHUTTING DOWN! DISCONNECTING");  
            }
            
        System.out.println("SERVER MSG> " + msg.toString());
  }


  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {

    if (message.charAt(0) == '#') {                   // detect '#'
            String[] index = message.split(" "); //split message to factor for other inputs
            String command = index[0];           
            switch (command) {
                case "#quit":         
                    quit();
                    break;
                case "#logoff":
                    try {
                        closeConnection();
                    } catch (IOException e) {
                        System.out.println("Error closing connection!!!");
                    }
                    break;
                case "#sethost":
                    if (this.isConnected()) {
                        System.out.println("There is already a connection established. Disconnect and try again.");
                    } else {
                        this.setHost((index[1]));
                    }
                    break;
                case "#setport":
                    if (this.isConnected()) {
                        System.out.println("There is already a connection established. Disconnect and try again.");
                    } else {
                        this.setPort(Integer.parseInt(index[1]));
                    }
                    break;
                case "#login":
                    if (this.isConnected()) {
                        System.out.println("There is already a connection established. Disconnect and try again.");
                    } else {
                        try {
                            this.openConnection();
                        } catch (IOException e) {
                            System.out.println("Could not establish connection to server.");
                        }
                    }
                    break;
                case "#gethost":
                    System.out.println("Current host is " + this.getHost());
                    break;
                case "#getport":
                    System.out.println("Current port is " + this.getPort());
                    break;
                default:
                    System.out.println("IThe command '" + command+ "' is not recognized.");
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

  public String getUsername(){
    return this.userID;
  }
  

  public void connectionException(Throwable exception){
    System.out.println("WARNING - The server has stopped listening for connections.");
        quit(); //displays information about the exception

  }
  public void connectionClosed(){
     System.out.println("Connection Closed to Server.");
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
    catch(IOException e) {
      connectionException(e);
    }
    System.exit(0);
  }





}

//End of ChatClient class
