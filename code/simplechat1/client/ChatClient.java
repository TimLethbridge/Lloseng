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
  
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }
  public ChatClient(String host, int port, ChatIF clientUI, String loginID, boolean errorFrom){

    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;

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
  * This method sends the id at the first time that the connection has been established.
  *
  **/

  protected void connectionEstablished() {

    try{
      sendToServer("#login <" + loginID+ ">");
    }
    catch(IOException e){

      System.out.println("Error : connectionEstablished");
    }

    


  }

  /**
  * This method checks if th einput from client is a command or not.
  *
  * @param message The message from UI
  **/
  private boolean checkCommand(String message){

    if(message.charAt(0) == '#'){
      return true;
    }

    return false;

  }
  /**
  * This method retrive the port from the string that came from a UI.
  *
  **/
  private int retrivePort(String str){

    String port= "";
    int start =10; // #setport <port>
    int end = str.length();

    int portDigits=0;
    try{

      for(int i =start ; i < end-1 ; i++){

        port += str.charAt(i);

      }

    }catch(Exception e){

      System.out.println("Unexcpected #port.");

    }
    try{
      portDigits = Integer.parseInt(port);
    }catch(Exception e){
      System.out.println("Not an acceptable port value. Please enter a number from 1 to 65535.");
    }

    return portDigits;

  }

  /**
  * This method retrive the host from the string that came from a UI.
  *
  **/

  private String retriveHost(String str){

    String host= "";
    int start = 10;
    int end = str.length();

    try{



      for(int i =start ; i < end-1 ; i++){

        host += str.charAt(i);

      }

    }catch(Exception e){

      System.out.println("Not an acceptable command for setting host.");

    }

    return host;

  }



  /** This method run the command
  *
  * @param message The message from UI
  */

  private void commandConsole(String message){

    int port = 0;
    String host = "";

    try{

      if( message.startsWith("#setport <")){
        port = retrivePort(message);
        message = "#setport <";
      }else if(message.startsWith("#sethost <")){
        host = retriveHost(message);
        message = "#sethost <";
      }

      switch(message){

        case "#quit":

          System.out.println("You are quiting.");
          quit();
          break;

        case "#logoff":

          try{
            System.out.println("You are logging off.");
            closeConnection();
          }
          catch(IOException e) {
            System.out.println("Something went wrong in logging off");
          }
          break;

        case "#sethost <": // only allowed when client is logged off

          if(isConnected()== false){
            System.out.println("Setting host to " + host );
            setHost(host);
          }else{
            System.out.println("Setting host error : You need to log off first.");
          }
          break;

        case "#setport <":

          if(isConnected()== false){

            if(port< 65535 && port > 0 ){
              System.out.println("Setting port to "+ port);
              setPort(port);
            }else{
              System.out.println("Not an acceptable port value. Please enter a number from 1 to 65535.");
            }

            
          }else{
            System.out.println("Setting port error : You need to log off first.");
          }

          break;

        case "#login":
          if(isConnected()== false){
            System.out.println("You are logging in.");
            openConnection();
          }else{

            System.out.println("Login error : You need to log off first.");

          }

          break;
        case "#gethost":

          System.out.println(getHost());
          break;
        case "#getport":

          System.out.println(getPort());
          break;

        default :
          System.out.println("Not a correct command, try again.");


      }

    }
    catch(Exception e){
      System.out.println("There is something wrong with the UI message.");
    }

  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if(checkCommand(message)){
      commandConsole(message);
    }
    else{

      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display
          ("Could not send message to server.");

      }

    }

  }

  /**
   * Hook method called after the connection has been closed. The default
   * implementation does nothing. The method may be overriden by subclasses to
   * perform special processing such as cleaning up and terminating, or
   * attempting to reconnect.
   */
  protected void connectionClosed() {
    System.out.println("Connection lost.");
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

    System.out.println("Abnormal termination of connection.");

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
