// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import client.ChatClient;
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
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
    String message = msg.toString();
    if(message.charAt(0) =='#'){
      if(message.substring(1,6).equals("login")){
        System.out.println("xD");
        client.setInfo("id",message.substring(6));
      }
      
    }
    System.out.println("Message received: " + message + " from " + client.getInfo("id"));
    this.sendToAllClients(client.getInfo("id") + ": " + message);
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
    System.out.println("A Client has connected to the server !");
  }
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println("A Client has disconnected from the server .");
  }

  //Class methods *************************************************** 
  public void handleMessageFromServerUI(String message){
    if(message.charAt(0) == '#'){
      if(message.substring(1,5).equals("quit")){
        System.exit(0);
      }else if(message.substring(1,5).equals("stop")){
        stopListening();
      }else if(message.substring(1,6).equals("close")){
        stopListening();
      }else if(message.substring(1,6).equals("start")){
        if(!isListening()){
          try{
            listen();
          } catch(Exception exception){
            System.out.println("friggin error xD");
          }
        }else{
          System.out.println("Server already listening");
        }
      }else if(message.substring(1,8).equals("setport")){
        if(isListening()){
          System.out.println("Error: yous already connected foo");
        }else{
          setPort(Integer.parseInt(message.substring(9)));
        }
      }else if(message.substring(1,8).equals("getport")){
        System.out.println(getPort());
      }
    }else{
    this.sendToAllClients("SERVER MSG > "+message);
    }
  }
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
