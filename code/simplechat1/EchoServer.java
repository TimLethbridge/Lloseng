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
    if (msg.toString().startsWith("#login")) {
      String[] message = msg.toString().split(" ");
      if (message.length != 0) {
        if (client.getInfo("loginID") == null) {
          client.setInfo("loginID", message[1]);
          this.sendToAllClients(client.getInfo("loginID") + " has logged on.");
        } else {
          try {
            client.sendToClient("Username cannot be changed.");
          } catch (IOException e) {
            //
          }
        }
      }
    } else {
      if (client.getInfo("loginID") == null) {
        try {
          client.sendToClient("Error: No username provided.");
          client.close();
        } catch (IOException e) {
          //
        }
      } else {
        System.out.println(client.getInfo("loginID") + "> " + msg);
        this.sendToAllClients(client.getInfo("loginID") + "> " + msg);
      }
    }
  }

  public void handleMessageFromServerUI(String message)
  {
    if (message.startsWith("#")) {
      String[] cmd = message.split(" ");
      switch (cmd[0]) {
        case "#quit":
          try {
            close();
            System.out.println("Connection terminated.");
          } catch (IOException e) {
            System.out.println("Unable to terminate connection.");
          }
          break;
        case "#stop":
          this.sendToAllClients("Server has stopped listening for new clients.");
          this.stopListening();
          break;
        case "#close":
          this.sendToAllClients("Server has stopped listening for new clients.");
          this.stopListening();
          try {
            close();
            System.out.println("Connection terminated.");
          } catch (IOException e) {
            System.out.println("Unable to terminate connection.");
          }
          break;
        case "#setport":
          if (!isListening()) {
            this.setPort(Integer.parseInt(cmd[1]));
            System.out.println("New port has been set.");
          } else {
            System.out.println("Server must be closed.");
          }
          break;
        case "#start":
          if (!isListening()) {
            try {
              listen();
            } catch (IOException e) {
              System.out.println("Unable to listen for new clients.");
            }
          } else {
            System.out.println("Server must be stopped.");
          }
          break;
        case "#getport":
          System.out.println("Current port: " + this.getPort());
          break;
      }
    } else {
        this.sendToAllClients("SERVER MSG> " + message);
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
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
    if (client.getInfo("loginID") != null) {
      this.sendToAllClients(client.getInfo("loginID") + " has connected to the server.");
    } else {
      this.sendToAllClients("A client has connected to the server.");
    }
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    this.sendToAllClients(client.getInfo("loginID") + " has disconnected from the server.");
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
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
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
