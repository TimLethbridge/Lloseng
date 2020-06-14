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
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI=serverUI;
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
    String message=(String)msg;
    String loginid=message.substring(11);
    
    if (message.substring(0,10)=="#login ID: "){
      if(client.getInfo("loginid")==null){
        client.setInfo("loginid",loginid);
        try{
          client.sendToClient( client.getInfo("loginid") +" logged in");
      }catch(IOException e){}
      }else{
        try{
          client.sendToClient("You have already logged in!");
      }catch(IOException e){
      }

      }
    }else{
      if(client.getInfo("loginid")==null){
        try{     
        client.sendToClient( "create a login ID first");
        client.close();
      }catch(IOException e){}
    }
    System.out.println("Message received: " + msg + " from " + client.getInfo("loginid"));
    
    this.sendToAllClients(client.getInfo("loginid") + "> " +msg);
  }
  }
  public void handleMessageFromServerUI(String message){
    try{
      if(message.startsWith("#")){
       switch(message.substring(0,7)){
         case "#close":
         serverClosed();
         break;
         case "#stop":
         serverStopped();
         break;
         case "#quit":
         close();
         serverUI.display("server quits...");
         break;
         case "#setport":
         if(!isListening()){
         try{
           int port = Integer.parseInt(message.substring(9));
           setPort(port);
           serverUI.display("Port is set to: "+ port);
         }catch(Exception e) {
          serverUI.display("invalid port, please try again");
          return;
         }
        }else{
          serverUI.display("You need to close server before set port");
          return;
        }
         break;
         case "#start":
         if(!isListening()){
         try{
           listen();
           serverUI.display("^^ logging you in ...^^");
         }catch(Exception e) {
          serverUI.display("Exception occurred when log in. please try again");
          return;
         }
        }else{
          serverUI.display("You need to close server before start server");
          return;
         }
         break;
         case "#getport":
         serverUI.display("Your current port is: " + getPort());
         break;
       }     }else    
       serverUI.display(message);  
       this.sendToAllClients("SERVER MSG> " + message);   
      }catch(IOException e){
        serverUI.display("Could not send message from server. Terminating server.");
        System.exit(1);
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
   * This method overrides the one in the superclass.  
   * Called called when the server is closed.
   */
   
  protected void serverClosed()
  {
    System.out.println("Server has closed.");
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
	
    ServerConsole sc = new ServerConsole(port);
    sc.accept();
    
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
