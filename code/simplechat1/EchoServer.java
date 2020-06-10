// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;
import common.ChatIF;

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

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {
    super(port);
  }


     public EchoServer(int port,ChatIF svUserInterface)
     {
       super(port);
       serverUI = svUserInterface;
     }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    String message=String.valueOf(msg);
    if(message.startsWith("#login")){
         if(client.getInfo( "#loginId" )!= null){
              System.out.println("You can't set your login Id twice!");
         }else{
          String iD= message.substring(10,message.length()-14);
          client.setInfo("#loginId",(Object) iD);
          System.out.println(msg);
        }
        this.sendToAllClients(client.getInfo("#loginId")+" has logged in");
    }


else{
    System.out.println("Message received:< " + msg + " >from< " + client.getInfo("#loginId")+">");
    this.sendToAllClients( "Message from "+ client.getInfo("#loginId")+":"+msg);
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

    System.out.println("A new client is attempting to connect to the server.");

    //this.sendToAllClients(client.getInfo("#loginId")+"has logged in");
  }
  synchronized protected void clientDisconnected(
    ConnectionToClient client) {
      System.out.println("Client has disconnected!");
    }
    synchronized protected void clientException(
      ConnectionToClient client, Throwable exception) {
        System.out.println(client.getInfo("#loginId")+"  has disconnected");
        this.sendToAllClients(client.getInfo("#loginId")+"  has disconnected");

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
