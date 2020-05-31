// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
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
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    String message = msg.toString();
    if (message.startsWith("#")) {
        String[] splitMessage = message.substring(1).split(" ");
        if (splitMessage.length > 1 && splitMessage[0]==("login")) {
            if (client.getInfo("loginid") == null) {
                client.setInfo("loginid", splitMessage[1]);
                System.out.println(client.getInfo("loginid")+" has logged on.");
                this.sendToAllClients(client.getInfo("loginid") + " has logged on.");
            } else {
                try {
                    client.sendToClient("ERROR: Your username has been set already.");
                } catch (IOException e) {
                  System.out.print("An error has occurred.");
                }
            }

        }
    } else {
        if (client.getInfo("loginid") == null) {
            try {
                client.sendToClient("ERROR: You must set a username.");
                client.close();
            } catch (IOException e) {
              System.out.print("An error has occurred.");
            }
        } else {
            System.out.println("Message received: " + msg + " from " + client.getInfo("loginid"));
            this.sendToAllClients(client.getInfo("loginid") + " > " + message);
        }
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

  public void clientConnected(ConnectionToClient client){
    if(client.getInfo("loginid")!=null){
    System.out.println
            (client.getInfo("loginid") + " has connected to the chat.");
    }
    else{
      System.out.println("A client has connected to the chat.");
    }
  }

  @Override
  public void clientDisconnected(ConnectionToClient client){
    System.out.println
            (client.getInfo("loginid") + " has disconnected from the chat.");
            this.sendToAllClients(client.getInfo("loginid") + " has disconnected from the chat.");
  }

  public void handleMessageFromServer(String message)
  {
    if(message.startsWith("#")){
      String[] splitMessage = message.split(" ");
      switch (splitMessage[0]){
        case "#quit":
        try {
          System.out.println("Quitting...");
          this.close();
          System.out.println("Server successfully quit.");
        }
        catch(IOException exception){
          System.out.println("An error occurred when trying to close the server.");
        }
          break;
        case "#stop":
          this.sendToAllClients("WARNING: the server has stopped listening for connections.");
          this.stopListening();
          break;
        case "#close":
          this.stopListening();
          try {
            this.close();
            System.out.println("The server is now closed.");
          }
          catch(IOException exception){
            System.out.println("An error occurred when trying to close the server.");
          }
          break;
        case "#setport":
          if (!this.isListening()) {
            super.setPort(Integer.parseInt(splitMessage[1]));
            System.out.println("New port: " + Integer.parseInt(splitMessage[1]));
          } else {
            System.out.println("An error occurred when changing the port.");
          }
          break;
        case "#start":
          if (!this.isListening()) {
            try{this.listen();
              System.out.println("The server has begun listening for connections.");
            }
            catch(IOException exception){
              System.out.println("An unexpected error occurred.");
            }
          }
          else{
            System.out.println("The server has already started.");
          }
          break;
        case "#getport":
          System.out.println("Current port: " + this.getPort());
          break;
        default:
          System.out.println("Invalid command.");
          break;
      }
    }
    else{
      sendToAllClients("SERVER MSG>" + message);
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
      sv.accept(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
