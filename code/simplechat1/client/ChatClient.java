// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package simplechat1.client;

import ocsf.client.*;
import simplechat1.common.*;
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
  
  public ChatClient(String loginid,String host, int port, ChatIF clientUI)
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    try {
      //System.out.println("0");
      //openConnection();
      this.loginId = loginid;
      handleMessageFromClientUI("#login " + loginId);
      //sendToServer("#login " + loginId);
      /*this.loginId = loginId;
      System.out.println("1");

      System.out.println("2");

       */
      //sendToServer("#login " + loginId);
      //System.out.println("2");
      //openConnection();
    } catch (Exception e) {
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

  protected void connectionClosed() {
    //System.out.println("Client terminates.");

  }

  protected void connectionException(Exception exception) {
    quit();
  }


  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message) {

    String[] argumentsArray = message.split(" ");
    //System.out.println(argumentsArray[0]);

    if(argumentsArray[0].subSequence(0, 1).equals("#")) {
      switch (argumentsArray[0]) {
        case "#quit":
          quit();
          break;

        case "#logoff":
          if (isConnected()) {
            try {
              closeConnection();
              System.out.println("Client log off ");
            } catch (IOException e) {
              System.out.println("Client log off fail");
            }
          }
          break;

        case "#sethost":
          if (isConnected()) {
            clientUI.display("Only allowed if the client is logged off");
            return;
          }
          try {
            String host = argumentsArray[1];
            setHost(host);
            System.out.println("Host set to: " + host);
          } catch (Exception e) {
            clientUI.display("Invalid host.");
          }
          break;

        case "#setport":
          if (isConnected()) {
            clientUI.display("Only allowed when the client is logged off");
            return;
          }
          try {
            int port = Integer.parseInt(argumentsArray[1]);
            setPort(port);
            System.out.println("port set to: " + port);
          } catch (Exception e) {
            clientUI.display("Invalid port.");
          }
          break;

        case "#login":
          //System.out.println("l0");
          if (isConnected()) {
            // System.out.println("l1");
            clientUI.display("Only allowed if the client is not already connected.");
            return;
          } else {
            try {
              //System.out.println("l21");
              openConnection();
              sendToServer("#login " + loginId);
              //System.out.println(loginId);
              //System.out.println("l22");

              //System.out.println("l23");

            } catch (IOException e) {
              clientUI.display("Cannot open connection.  Awaiting command.");
              //System.exit(0);
            }
          }
          break;

        case "#gethost":
          clientUI.display("current host: " + getHost());
          break;

        case "#getport":
          clientUI.display("current port: " + getPort());
          break;

        default:
          clientUI.display("wrong command");
          break;
      }
    }
    else{
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
  public void quit() {
    if (isConnected()) {
      try {
        closeConnection();
      } catch (IOException e) {
      }
    }
    System.out.println("Abnormal termination of connection.");
    System.exit(0);
  }



}
//End of ChatClient class
