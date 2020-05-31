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
  private String loginId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI,String loginId)
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    try {
      openConnection();
      this.loginId = loginId;
      sendToServer("#login " + loginId);
    } catch (IOException e) {
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
    System.out.println(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if(message.equals("#quit")) {
      quit();
    } else if(message.equals("#logoff")) {
      try {
        closeConnection();
      } catch (IOException ignored) { }
    } else if(message.startsWith("#sethost ")) {
      if(isConnected()) {
        clientUI.display("Only allowed if the client is logged off");
        return;
      }
      try {
        String host = message.split("\\s+")[1];
        setHost(host);
        System.out.println("Host set to: " + host);
      } catch (Exception e) {
        clientUI.display("Invalid host.");
      }
    } else if(message.startsWith("#setport ")) {
      if(isConnected()) {
        clientUI.display("Only allowed if the client is logged off");
        return;
      }
      try {
        int port = Integer.parseInt(message.split("\\s+")[1]);
        setPort(port);
        System.out.printf("port set to: %d.\n",port);
      } catch (Exception e) {
        clientUI.display("Invalid port.");
      }
    } else if(message.equals("#login")) {
      if(isConnected()) {
        clientUI.display("Only allowed if the client is not already connected.");
        return;
      }
      try {
        openConnection();
        sendToServer("#login " + loginId);
      } catch (IOException e) {
        clientUI.display("login error");
      }
    } else if(message.equals("#gethost")) {
      clientUI.display("current host: " + getHost());
    } else if(message.equals("#getport")) {
      clientUI.display("current port: " + getPort());
    } else if(message.startsWith("#")) {
      clientUI.display("Unknown command.");
    } else {
      try {
        sendToServer(message);
      } catch (IOException e) {
        clientUI.display
                ("Could not send message to server.  Terminating client.");
        quit();
      }
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    if(isConnected()) {
      try {
        closeConnection();
      } catch (IOException e) {
      }
    }
    System.out.println("Client terminates.");
    System.exit(0);
  }

  @Override
  protected void connectionClosed() {
    System.out.println("Abnormal termination of connection.");
  }


  @Override
  protected void connectionException(Exception exception) {
    System.out.println("Abnormal termination of connection.");
  }

}
//End of ChatClient class
