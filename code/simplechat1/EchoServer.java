// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import common.ChatIF;
import ocsf.server.*;

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
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
    
  }

  ChatIF serverUI;
  
  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
      String temp = msg.toString();
      if(temp.contains("#login")){
        System.out.println(msg);
        this.sendToAllClients("ServerMSG> Welcome to the server " + temp.split(" ")[1]+".");
        client.setInfo("Login ID",temp.split(" ")[1]);
      }else{
        System.out.println("Message received: " + msg + " from " + client);
        this.sendToAllClients(client.getInfo("Login ID")+": "+msg);
      }
  }
  
    
  protected void serverStarted(){
    System.out.println("Server listening for connections on port # " + getPort());
    try{
      listen();
    }catch(IOException exception){
      System.out.println("ERROR.");
    }
  }
  protected void serverStopped(){
    stopListening();
    System.out.println("Server has stopped listening for connections.");
    this.sendToAllClients("Server stopped listening for connections.");
    
  }

  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client has connected to the server.");
  }

  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println(client.getInfo("Login ID")+" has left the server.");
  }

  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println(client.getInfo("Login ID")+" has left the server.");
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
