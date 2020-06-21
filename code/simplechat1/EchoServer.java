// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import client.*;
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
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) throws IOException
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
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    if(msg.toString().contains("#")){
      if(msg.toString().contains("login")){
        client.setInfo("LoginID", msg.toString().replace("#login",""));
      }
    }
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients("(" + client.getInfo("LoginID") + "): " + msg);
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


  //This method overrides the superclass to display when clients are connected
  protected void clientConnected(ConnectionToClient client){
    serverUI.display("#login" + client + " has connected to this server.");
  }

  //This method overrides the superclass to display when clients are disconnected
  synchronized protected void clientDisconnected(ConnectionToClient client){
    serverUI.display("#login" + client + " has disconnected from the server.");
  }


  //This method handles all user input and excutes the desire effects
  public void handleMessageFromServerUI(String message)
  {
    if(message.contains("#")){
      if(message.contains("quit")){
        try{
          close();
        }catch(IOException e){}
        System.exit(0);
      }

      if(message.contains("stop")){
        stopListening();
      }

      if(message.contains("close")){
        try{
          close();
        }catch(IOException e){}
      }

      if(message.contains("setport")){
        if(isListening()){
          serverUI.display("This command can not be issued while the server is still on.");
        }else{
          setPort(Integer.parseInt(message.replace("#setport","")));
        }
      }

      if(message.contains("start")){
        if(isListening()){
          serverUI.display("This command can not be issued while the server is still on.");
        }else{
          try{
            listen();
          }
          catch(IOException e) {}
        }
      }

      if(message.contains("getport")){
        serverUI.display(String.valueOf(getPort()));
      }
    }else{
      message = "SERVER MSG> "+ message;
      this.sendToAllClients(message);
    }
  }
  
  //Class methods ***************************************************
  
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
	
    ServerConsole sv = new ServerConsole(port);
    
    try 
    {
      sv.server.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
