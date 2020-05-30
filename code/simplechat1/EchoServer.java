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
  
  //Constructors ****************************************************
  ChatIF serverUI;
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  public EchoServer(int port, ChatIF serverUI) throws IOException {
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
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
    if(msg.toString().startsWith("#login")){
      if(client.getInfo("loginID") != null){
        try{
          System.out.println("You are already logged in.");
        }
        catch(Exception e){}
      }
      client.setInfo("loginID",msg.toString().substring(8));
      try{
        client.sendToClient(client.getInfo("loginID") + " has logged on.");
      }
      catch(IOException g){}
    }
    else{
      try{
        if(client.getInfo("loginID") == null){
          System.out.println("Could not login.. use #login has first argument!");
          client.close();
        }
      }
      catch(IOException e){
        e.printStackTrace();
      }
    }
    System.out.println("Message received: " + msg + " from " + client.getInfo("loginID") + ", Users IP: " + client);
    if(msg.toString().startsWith("#login")){
      if(client.getInfo("loginID") != null){
        try{
          System.out.println(client.getInfo("loginID") + " has logged on.");
        }
        catch(Exception gg){}
      }
    }
    this.sendToAllClients(client.getInfo("loginID") + " > " + msg);
  }

  public void handleMessageFromServerUI(String message){
      try{
        if(message.charAt(0) == '#'){
          if(message.equals("#quit")){
            stopListening();
            close();
            System.out.println("Server is quitting..");
            System.exit(1);
          }
          if(message.equals("#stop")){
            stopListening();
            this.sendToAllClients("WARNING - Server has stopped listening for connections.");
          }
          if(message.equals("#close")){
            close();
          }
          if(message.startsWith("#setport")){
            if(!isListening()){
              try{
                String p = message.substring(9);
                setPort(Integer.parseInt(p));
                System.out.println("The server port has been changed to: " + getPort());
              }
              catch(Exception ex){
                System.out.println("Could not set the server port!");
              }
            }
            else{
              System.out.println("Server must be stopped.");
            }
          }
          if(message.equals("#start")){
            if(!isListening()){
              try{
                listen();
              }
              catch(Exception el){
                System.out.println("Could not listen for new connections.");
              }
            }
            else{
              System.out.println("Server must be stopped.");
            }
          }
          if(message.equals("#getport")){
            System.out.println("Port: " + getPort());
          }
        }
        else{
          serverUI.display(message);
          this.sendToAllClients("SERVER MSG> " + message);
        }
      }
      catch(IOException e){
        System.out.println("Could not send message from server. Terminating server.");
        System.exit(1);
      }
  }
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println("Server has stopped listening for connections.");
  }

  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client is attempting to connect to the server.");
  }
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println(client.getInfo("loginID") + " has disconnected from the server.");
    this.sendToAllClients(client.getInfo("loginID") + " has disconnected from the server.");
  }
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println(client.getInfo("loginID") + " has disconnected from the server.");
    this.sendToAllClients(client.getInfo("loginID") + " has disconnected from the server.");
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
