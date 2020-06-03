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

    try {
      listen();
    } catch (Exception exception) {
      serverUI.display("ERROR - Could not listen for clients!");
    }
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
    String message = (String) msg;
    String[] loginid = message.split(" ");
    
    if (loginid[0].equals("#login"))
    {
      client.setInfo("loginid", loginid[1]);
      System.out.println("Message received: " + msg + " from " + client.getInfo("loginid"));
      System.out.println(client.getInfo("loginid") + " has logged on.");
      sendToAllClients(client.getInfo("loginid") + " has logged on.");
    }

    else if (loginid[0].equals("abort")) // if #login <loginid> is not first command
    {
      try {
        client.sendToClient("Login command must be called first");
        client.close();
      } catch (IOException exception) {
        System.out.println("An error occured while attempting to terminate client");
      }

    }
    
    else 
    {
      System.out.println("Message received: " + msg + " from " + client.getInfo("loginid"));
      msg = client.getInfo("loginid") + "> " + msg;
      sendToAllClients(msg);
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
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) 
  {
    System.out.println("A new client is attempting to connect to the server");
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(
    ConnectionToClient client) 
    {
      System.out.println(client.getInfo("loginid") + " has disconnected");
      sendToAllClients(client.getInfo("loginid") + " has disconnected");
    }

  /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) 
    {
      System.out.println(client.getInfo("loginid") + " has disconnected");
      sendToAllClients(client.getInfo("loginid") + " has disconnected");
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
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message)
  {
    String[] command = message.split(" ");

    if (message.charAt(0) == '#'){

      if (command[0].equals("#quit")) {
        try {
          close();
          System.exit(0);
        } catch (IOException exception){
          System.out.println("Error occured while attempting to close");
        }
      }

      else if (command[0].equals("#stop")) {
        sendToAllClients("WARNING - Server has stopped listening for new connections");
        stopListening();
      }

      else if (command[0].equals("#close")) {
        try {
          stopListening();
          close();
        } catch (IOException exception){
          System.out.println("Error occured while attempting to close");
        }
      }

      else if (command[0].equals("#setport")) {
        if (isListening()) {
          System.out.println("Cannot change port because server is listening for connections");
        } else {
          try{
            setPort(Integer.parseInt(command[1]));
            System.out.println("Port set to: " + command[1]);
          } catch (IndexOutOfBoundsException exception) {
            System.out.println("Please choose a valid port!");
          }
        }
      }

      else if (command[0].equals("#start")) {
        if (isListening()) {
          System.out.println("Server must be stopped to start listening for connections");
        } else {
          try {
            listen();
          } catch (IOException exception){
            System.out.println("The server ran into an error while attempting to reconnect");
          }
        }
      }

      else if (command[0].equals("#getport")) {
        System.out.println("Port : "+getPort());
      }

      else {
        System.out.println("Not a valid command");
      }

    } else {
      sendToAllClients("SERVER MSG> " + message);
      System.out.println(message);
    }
    
  }
  
  //Class methods ***************************************************
  
}
//End of EchoServer class
