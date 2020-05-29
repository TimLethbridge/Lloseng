// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import common.*;
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
  
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the server.
   */
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
    System.out.println
      ("Message received: " + msg + " from " + client.getInfo("login id"));

    try{ //Avoid index out of bound 
        if(msg.toString().substring(0, 6).equals("#login")){
          if(client.getInfo("login id") == null){
            String loginID = msg.toString().substring(6, msg.toString().length()).replaceAll("\\s+","");
            client.setInfo("login id", loginID);
            System.out.println(loginID + " has logged on.");
            this.sendToAllClients(loginID + " has logged on.");
          }
          else{
            try{
              client.sendToClient("Error: you have already logged in with your login id.");
              client.close();
            }
            catch(IOException e){}
          } 
      }
      else{
        this.sendToAllClients(client.getInfo("login id") +": "+ msg);
      }
    }
    catch(IndexOutOfBoundsException ex){ //For message with length less than 6
      this.sendToAllClients(client.getInfo("login id") +": "+ msg);
    }
  }
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message){
    try{
      //Determine whether the message is command or not
      if(String.valueOf(message.charAt(0)).equals("#")){
        if(message.equals("#quit")){
          System.out.println("The server quits.");
          quit();
        }
        else if(message.equals("#stop")){
          stopListening();
          this.sendToAllClients("WARNING: Server has stopped listening for connections.");
        }
        else if(message.equals("#close")){
          close();
        }
        else if(message.substring(0, 4).equals("#set")){
          if(!isListening() && getNumberOfClients() == 0){  //Whether the server is closed or not
            if(message.substring(4, 8).equals("port")){
              String newPort = message.substring(8, message.length()).replaceAll("\\s+","");
              setPort(Integer.parseInt(newPort));

              System.out.println("Port set to: " + newPort + ", successfully.");
            }
          }
          else{
            System.out.println("Error: server has not close yet.");
          }
        }
        else if(message.equals("#start")){
          if(!isListening()){ //Whether the server is stopped or not
            listen();
            System.out.println("Server starts successfully.");
          }
          else{
            System.out.println("Error: server has not stopped yet.");
          }
        }
        else if(message.equals("#getport")){
           System.out.println("The port number is: " + String.valueOf(getPort()));
        }
        else{
          System.out.println("Error: unkonwn command");
        }
      }
      else{ //Non command message
        serverUI.display(message);
        this.sendToAllClients("SERVER MSG> " + message.toString());
      }   
    }
    catch(Exception e){
      System.out.println("Unexpect error, Terminating client.");
      quit();
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

  /**
   * This method terminates the server.
   */
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  //Override methods ************************************************

  @Override
  public void clientConnected(ConnectionToClient client) {
    System.out.println("A new client is attempting to connect to the server.");
  }

  @Override
  synchronized public void clientDisconnected(ConnectionToClient client) {
  	String message = client.getInfo("login id") + " has disconnected.";
    System.out.println(message);
    this.sendToAllClients(message);
  }

  @Override
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
    clientDisconnected(client);
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
    
    ServerConsole chat = new ServerConsole(port); //Initialize server console
    chat.accept(); 
  }

}
//End of EchoServer class
