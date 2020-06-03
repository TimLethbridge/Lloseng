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

  private String loginid;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginid, String host, int port, ChatIF clientUI) 
  {
    super(host, port); //Call the superclass constructor
    this.loginid = loginid;
    this.clientUI = clientUI;
    
    try {
      openConnection();
      sendToServer("#login " + loginid);
    } catch (IOException exception) {
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
      String[] command = message.split(" ");

      if (message.charAt(0)=='#') {
        
        if (command[0].equals("#quit")) {
          quit();
        }

        else if (command[0].equals("#logoff")) {
          try{
            closeConnection();
          } catch(Exception exception){
            clientUI.display("Error : Could not logoff");
          }
        }

        else if (command[0].equals("#sethost")) {
          
          if(!isConnected()) {
            setHost(command[1]);
            clientUI.display("Host set to: " + command[1]);

          } else {
            clientUI.display("Cannot set host! Client is logged off");
          }
        }

        else if (command[0].equals("#setport")) {

          if(!isConnected()) {
            int customPort = Integer.parseInt(command[1]); 
            setPort(customPort);
            clientUI.display("Port set to: " + command[1]);

          } else {
            clientUI.display("Cannot set port! Client is logged off");
          }
        }

        else if (command[0].equals("#login")) {
          try{
            if (!isConnected() && command.length == 2){
              loginid = command[1];
              openConnection();
              sendToServer(message);

            } else if (!isConnected() && command.length < 2){
              clientUI.display("ERROR - No login ID specified.");
            } 
            
            else if (isConnected() && command.length == 2) {
              sendToServer("abort");
            }

          } catch(Exception exception){
            System.out.println("Error : could not login!");
          }
        }

        else if (command[0].equals("#gethost")) { 
          String hostAddress = getHost(); 
          clientUI.display("Host : "+hostAddress);
        }

        else if (command[0].equals("#getport")) {
          int portAddress = getPort(); 
          clientUI.display("Port : "+portAddress);
        }

        else {
          clientUI.display("No such command exists");
        }
    }

    else {
        sendToServer(message);
      }
    }

    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
    clientUI.display("Abnormal termination of connection.");
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
    clientUI.display("SERVER SHUTTING DOWN! DISCONNECTING!");
    connectionClosed();
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
