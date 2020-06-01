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
   * This variable stores loginID that the user entered.
   */
  private String loginID;


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
    this.loginID = loginID;
    this.clientUI = clientUI;
    openConnection();
    sendToServer("#login "+loginID);
  }


  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    String message = (String) msg;
    if (message.equals("#servershutdown")) {
      System.err.println("SERVER SHUTTING DOWN! DISCONNECTING!");
      System.err.println("Abnormal termination of connection.");
      quit();
    } else if (message.equals("#successfullogin")) {
      System.out.println("Connected!");
    } else {
      clientUI.display(msg.toString());
    }
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
      if (message.length() != 0) {
        if (message.substring(0,1).equals("#")) {
          handleCommandMessage(message.substring(1));
        } else {
          sendToServer(message);
        }
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
   * This method is called when the connection to the server is closed.
   */
  public void connectionClosed() {
    System.err.println("Connection closed! ");
  }


  /**
   * This is the getter method of loginID.
   */
  public String getLoginID() {
    return loginID;
  }

  /**
   * This is a help method to handle commands.
   * @param command The command message from the client sonsole without #.
   */
  private void handleCommandMessage(String command) throws IOException {
    if (command.equals("quit")) {
      sendToServer("#logoff "+loginID);
      quit();
    } else if (command.equals("logoff")) {
      try
      {
        sendToServer("#logoff "+loginID);
        closeConnection();
      }
      catch(IOException e) {}
    } else if (command.equals("login")) {
      if (! isConnected()) {
        openConnection();
        sendToServer("#login "+loginID);
      } else {
        System.err.println("[Error] The client is already connected! ");
      }
    } else if (command.equals("gethost")) {
      System.out.println(getHost());
    } else if (command.equals("getport")) {
      System.out.println(getPort());
    } else if (command.length() >= 9) {
      if (command.substring(0,8).equals("sethost ")) {
        if (! isConnected()) {
          setHost(command.substring(8));
          System.out.println("Host set to: "+command.substring(8));
        } else {
          System.err.println("[Error] The client is not logged off! ");
        }
      } else if (command.substring(0,8).equals("setport ")) {
        if (! isConnected()) {
          try {
            setPort(Integer.parseInt(command.substring(8)));
            System.out.println("Port set to: "+command.substring(8));
          }
          catch (Exception e){
            System.err.println("[Error] Invalid command expression! ");
          }
        } else {
          System.err.println("[Error] The client is not logged off! ");
        }
      } else {
        System.err.println("[Error] Invalid command expression! ");
      }
    } else {
      System.err.println("[Error] Invalid command expression! ");
    }
  }
}
//End of ChatClient class
