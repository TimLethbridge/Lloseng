// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;
import common.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  ChatIF serverUI;
  private static String loginId;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port,ChatIF serverUI)
  {
    super(port);
    this.serverUI = serverUI;
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */

  //  public void setInfo(String loginId){
  //    this.loginId = loginId;
  // }
  // public String getInfo(){
  //   return loginId;
  // }

  public void handleMessageFromClient
    (Object msg, ConnectionToClient client){
    String temp[] = String.valueOf(msg).split(" ");
     if(temp[0].equals("#login") && temp.length> 1){
      client.setInfo(loginId,temp[1]);
      this.sendToAllClients(client.getInfo(loginId) + " has logged in");
      System.out.println("Message received: " + msg + " from " + client);
    }else{
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(client.getInfo(loginId) + ">" + msg);
  }
}

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  protected void clientConnected(ConnectionToClient client) {
    System.out.println("The Server has accepted a client ");
  }
  protected void clientDisconnected(ConnectionToClient client) {
    System.out.println("The client has disconnected");
    this.sendToAllClients(client.getInfo(loginId) + "has logged off");
  }
  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555
   *          if no argument is entered.
   */

}
//End of EchoServer class
