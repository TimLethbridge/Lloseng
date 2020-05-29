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

  /**
   * The string that store the id of login client.
   */
  String loginID;

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, String loginID) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;

    //Allow commands to be typed in console without server connection, messages
    //other than commands will terminate client.
    try{
      openConnection();
      sendToServer("#login " + loginID);
    }
    catch(IOException ex){ 
      clientUI.display("Cannot open connection.  Awaiting command.");
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
      try{ //Avoid blank space cause index out of bound
        //Determine whether the message is command or not
        if(String.valueOf(message.charAt(0)).equals("#")){
          try{ //Avoid index out of bound
            if(message.equals("#quit")){
              clientUI.display("Client terminates.");
              quit();
            }
            else if(message.equals("#logoff")){
              try{
                closeConnection();
              }
              catch(IOException e){}
             }
            else if(message.substring(0, 4).equals("#set")){
              if(!isConnected()){  //Whether the client is log off or not
                if(message.substring(4, 8).equals("host")){
                  String newHost = message.substring(8, message.length()).replaceAll("\\s+","");
                  setHost(newHost);

                  clientUI.display("Host set to: " + newHost + ", successfully.");
                }
                else if(message.substring(4, 8).equals("port")){
                  String newPort = message.substring(8, message.length()).replaceAll("\\s+","");
                  setPort(Integer.parseInt(newPort));

                  clientUI.display("Port set to: " + newPort + ", successfully.");
                }
              }
              else{
                clientUI.display("Error: client has not logged off yet.");
              }
            }        
            else if(message.substring(0, 6).equals("#login")){
              if(isConnected() && message.length() > 6){ //login command (follow with id) should sent to server
                sendToServer(message);
              }
              else if(!isConnected() && message.length() > 6){//Situation where client has logged off and want to re-log in
                clientUI.display("To login, please enter #login"); 
              }
              else if(!isConnected()){ //Whether the client is log off or not
                openConnection();
                sendToServer("#login " + loginID);
                clientUI.display("You have login successfully.");
              }
              else{
                clientUI.display("Error: client has already logged in.");
              }
            }
            else if(message.equals("#gethost")){
              clientUI.display("The host name is: " + getHost());
            }
            else if(message.equals("#getport")){
              clientUI.display("The port number is: " + String.valueOf(getPort()));
            }
            else{
              clientUI.display("Error: unkonwn command");
            }  
          }
          catch(IndexOutOfBoundsException ex){
            clientUI.display("Error: unkonwn command");
          }    
        }
        else{ //Non command message
            sendToServer(message);
        }             
      }
      catch(IndexOutOfBoundsException ex){
        sendToServer(message);
      }
         
    }
    catch(IOException e)
    {
      clientUI.display
        ("Cannot oppen connection/send message to server,  awaiting command.");
      //No quit and terminate cliet here since we want client 
      //console still wait for inputs after server shutted down.
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection( );
    }
    catch(IOException e) {}
    System.exit(0);
  }

  //Override methods ************************************************

  @Override
  public void connectionClosed(){
    clientUI.display
      ("Connection closed.");
  }

  @Override
  public void connectionException(Exception exception){
    clientUI.display
      ("SERVER SHUTTING DOWN! DISCONNECTING!\n"+ "> Abnormal termination of connection.");
    //No quit and terminate cliet here since we want client 
    //console still wait for inputs after server shutted down.
  }

}
//End of ChatClient class
