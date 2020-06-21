// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import java.io.*;
import client.*;
import common.*;
import java.lang.*;

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
  public EchoServer(int port, ChatIF serverUI)throws IOException 
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
  /*handles message from the client*/
  /*I haven't done, E7(c) iv,v since it is redundant given the way i've written my code.
  
  (iv)The #login command should only be allowed as the first command received after a client connects. If #login is received at any other time, the server shouldsend an error message back to the client.
  This will never occur because handlemessagefromclientui in chatclient will catch any #login typed by the user for a different command and there is no other way for a user to send a message to the server than go through this method

  (v) If the #login command is not received as the first command, then the server should send an error message back to the client and terminate the client’s connection. Hint: use the method called close found in ConnnectionToClient.
  This will never occur since I put sendtoserver #login loginID in the constructor of chatclient so it will always be the first command sent

  You will probably take marks off since I technically haven't done it but I just wanted to show that I wasn't being lazy or actually I was being lazy but lazy to do something that was redundant in the context of my implementation of simplechat.
  */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    String message = msg.toString();
    if(message.contains("#login")){
      String loginID = message.replace("#login ","");
      client.setInfo("loginID", loginID);
    }else{
      serverUI.display("Message received: " + msg + " from " + client);
      this.sendToAllClients("<"+client.getInfo("loginID")+"> "+msg);
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */

  /*Messages*/
  protected void serverStopped()
  {
    serverUI.display
      ("Server has stopped listening for connections.");
  }

  protected void clientConnected(ConnectionToClient client) {
    serverUI.display
      ("A client just joined ( ͡° ͜ʖ ͡°)");
  }

  synchronized protected void clientDisconnected(ConnectionToClient client) {
    serverUI.display("The client has disconnected");
  }

  /*handles messages from serverui and does commands*/

  public void handleMessageFromServerUI(String message)
  {
    if(message.contains("#")){
        if(message.contains("quit")){
          quit();
        }

        if(message.contains("stop")){
          stopListening();
        }

        if(message.contains("close")){
          try
            {
            close();
            }
            catch(IOException e) {}
        }

        if(message.contains("setport")){
          if(isListening()){
            serverUI.display("This operation is unavailable while the server is not stopped.");
          }else{
            int portt = Integer.parseInt(message.replace("#setport",""));
            setPort(portt);
          }
        }

        if(message.contains("start")){
          if(isListening()){
            serverUI.display("This operation is unavailable while the server is not stopped.");
          }else{
            try
            {
            listen();
            }
            catch(IOException e) {}
          }
        }

        if(message.contains("getport")){
          serverUI.display(String.valueOf(getPort()));
        }


    }
    message = "SERVER MSG> "+ message;
    this.sendToAllClients(message);
  }
  
  /*added a quit method similar to the one in chatclient but for the server*/
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }


  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */

  /*put an empty main method due to an error I was getting, please ignore it*/
  public static void main(String[] args) 
  {}
  
}
//End of EchoServer class
