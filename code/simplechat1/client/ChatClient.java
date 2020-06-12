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
   * The class variable that stores the login ID of the client
   */
  String loginID = "";


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
    if(loginID == "N/A"){
      throw new IOException();
    }else{
      this.clientUI = clientUI;
      this.loginID = loginID;
      try{
        openConnection();
        sendToServer("#login " + loginID);
      }catch (IOException e){
        System.out.println("Cannot open connection. Awaiting command...");
      }

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
    String temp = (String) message;
    String changeValue = "";
    char [] messg = temp.toCharArray();
    if(messg[0] == '#'){
      temp = temp.replace("#", "");
      String [] messageSplit = temp.split(" ");
      if(messageSplit[0].equals("login") || messageSplit[0].equals("sethost")
      || messageSplit[0].equals("setport")){
        temp = messageSplit[0];
        changeValue = messageSplit[1];
      }
      handleCommandFromClientUI(temp, changeValue);
    }else{
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

  /**
   * This method handles all commands coming from the UI
   *
   * @param cmd The cmd from the UI.
   * @param change The value for "set" commands from the UI
   */
  private void handleCommandFromClientUI(String cmd, String change){

    switch(cmd){
      case "quit":
      clientUI.display("Terminating....");
      quit();
        break;

      case "logoff":
      if(isConnected()){
        try{
          clientUI.display("Logging off...");
          closeConnection();
        }catch(IOException e){
        }
      }else{
        clientUI.display("Already logged out");
      }

        break;

      case "login":
      if(!isConnected()){
        try{
          clientUI.display("Logging in...");
          openConnection();
          sendToServer("#login " + change);
        }catch (IOException e){
        }
      }else{
        clientUI.display("Already logged in");
      }

        break;

      case "gethost":
      clientUI.display("Host: " + getHost());
        break;

      case "getport":
      clientUI.display("Port: " + String.valueOf(getPort()));
        break;

      case "sethost":
      if(!isConnected()){
        setHost(change);
        clientUI.display("New Host Name: " + change);
      }else{
        clientUI.display("Cannot change host while connected to server!");
      }
        break;

      case "setport":
      if(!isConnected()){
        int newPort = Integer.valueOf(change);
        setPort(newPort);
        clientUI.display("New Port Number: " + newPort);
      }else{
        clientUI.display("Cannot change port while connected to server!");
      }
        break;

      default:
      try{
        sendToServer("#"+ cmd + " " + change);
      }catch (IOException e){

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
    clientUI.display("Connection closed");
	}

/**
 * Overriding the connectionException() method to terminate the client when the connection raises an exception
 * @param exception
 *            the exception raised.
 */
protected void connectionException(Exception exception) {
  clientUI.display
    ("Abnormal termination of connection.");
}


}


//End of ChatClient class
