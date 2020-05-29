// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
  // Instance variables **********************************************

  /**
   * The interface type variable. It allows the implementation of the display
   * method in the client.
   */
  ChatIF clientUI;
  public String loginid;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host     The server to connect to.
   * @param port     The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
    super(host, port); // Call the superclass constructor
    this.clientUI = clientUI;
  }

  // Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {
    clientUI.display(msg.toString());
  }

  // Add by me
  protected void connectionClosed() {
    clientUI.display("Connection Closed.");
  }
  protected void connectionException(Exception exc){
    clientUI.display("Abnormal termination of connection");

  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   * @throws IOException
   */
  public void handleMessageFromClientUI(String message) {
 
    String[] messages = message.split(" ");
    if (message.charAt(0) == '#') {

      switch (messages[0]) {
        case ("#logoff"):
          try {
            closeConnection();
          } catch (IOException exc) {
            clientUI.display("Can't close connection");
          }
          break;
        case ("#sethost"):
          clientUI.display("Host set to: " + messages[1]);
          setHost(messages[1]);

          break;
        case ("#setport"):
          setPort(Integer.parseInt(messages[1]));
          clientUI.display("Port set to: " + messages[1]);
          break;
        case ("#login"):
        try{
          login(messages[1]);
        }catch(IndexOutOfBoundsException | IOException except){
          clientUI.display("Username Required or The server cannot connect");
        }
          break;
        case ("#quit"):
          quit();
          break;
        case ("#gethost"):
          clientUI.display(getHost());
          break;
        case ("#getport"):
          clientUI.display(Integer.toString(getPort()));
          break;

      }
    } else if(isConnected()) {
      try {
        sendToServer(message);
      } catch (IOException e) {
        clientUI.display("Could not send message to server.  Terminating client.");
        quit();
      }
    }

  }
public void login(String name) throws IOException{
    openConnection();
    sendToServer("#login "+name);

}
  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {
    }
    System.exit(0);
  }
}
// End of ChatClient class
