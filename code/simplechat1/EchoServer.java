// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;
import common.*;
import java.util.LinkedList;

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

  //Instance variables *************************************************

  /**
   * This is a boolean variable indicating whether the server has stopped.
   */
  private boolean serverStopped;

  /**
   * This is a boolean variable indicating whether the server has closed
   */
  private boolean serverClosed;

  /**
   * This is to store all the clients who have ever made connections with the
   * server, to help printing information of disconnction when all clients
   * have already disconnected.
   */
  LinkedList<ConnectionToClient> clients;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {
    super(port);
    clients = new LinkedList<ConnectionToClient>();
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
    try {
      String message = (String) msg;
      if (message.length() != 0) {
        if (message.substring(0,1).equals("#")) {
          if (message.length() >= 7) {
            if (message.substring(1,6).equals("login")) {
              if (client.getInfo("loginID") == null) {
                client.setInfo("loginID", message.substring(7));
                System.out.println("Client "+client.getInfo("loginID")+" connects. ");
                System.out.println("New login from "+client+", loginID: "+message.substring(7));
                for (ConnectionToClient cli: clients) {
                  cli.sendToClient(message.substring(7)+" has connected.");
                }
                clients.add(client);
                client.sendToClient("#successfullogin");
              } else {
                client.sendToClient("[Error] You have already logged in, loginID: "+client.getInfo("loginID"));
              }
            } else if (message.substring(1,7).equals("logoff"))  {
              clients.remove(client);
              System.out.println("Client "+client.getInfo("loginID")+" disconnected. ");
              for (ConnectionToClient cli: clients) {
                cli.sendToClient(message.substring(7)+" has disconnected.");
              }
            } else {
              System.err.println("[Error] Invalid command expression from client! ");
            }
          }
        } else {
          if (client.getInfo("loginID") == null) {
            client.sendToClient("[Error] Missing login command with loginID, terminate connection! ");
            client.close();
          } else {
            if (serverStopped) {
              client.sendToClient("[Warning] Server has stopped listening for connections. ");
            }
            System.out.println("Message received: " + msg + " from " + client+", loginID: "+client.getInfo("loginID"));
            this.sendToAllClients(client.getInfo("loginID")+": "+msg);
          }
        }
      }
    }
    catch (IOException e) {}
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
    serverStopped = false;
    serverClosed = false;
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
    serverStopped = true;
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server is closed.
   */
  protected void serverClosed() {
    System.out.println("Server has closed. ");
    serverClosed = true;
  }


  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {
    try {
      String message = (String) msg;
      if (message.length() != 0) {
        if (message.substring(0,1).equals("#")) {
          handleCommandMessage(message.substring(1));
        } else {
          this.sendToAllClients("SERVER MSG> "+message);
          System.out.println("SERVER MSG> "+message);
        }
      }
    }
    catch (IOException e) {}
  }

  /**
   * This is a help method to handle commands.
   * @param command The command message from the server sonsole without #.
   */
  private void handleCommandMessage(String command) throws IOException {
    if (command.equals("quit")) {
      existingClientDisconnected(clients);
      this.sendToAllClients("#servershutdown");
      close();
      System.exit(0);
    } else if (command.equals("stop")) {
      this.sendToAllClients("[Warning] Server has stopped listening for connections. ");
      serverStopped();
      stopListening();
    } else if (command.equals("close")) {
      existingClientDisconnected(clients);
      this.sendToAllClients("#servershutdown");
      close();
    } else if (command.equals("start")) {
      if (serverStopped) {
        listen();
      } else {
        System.err.println("[Error] The server is not stopped! ");
      }
    } else if (command.equals("getport")) {
      System.out.println(getPort());
    } else if (command.length() >= 9) {
      if (command.substring(0,8).equals("setport ")) {
        if (serverClosed) {
          try {
            setPort(Integer.parseInt(command.substring(8)));
          }
          catch (Exception e){
            System.err.println("[Error] Invalid command expression! ");
          }
        } else {
          System.err.println("[Error] The server is not closed! ");
        }
      } else {
        System.err.println("[Error] Invalid command expression! ");
      }
    } else {
      System.err.println("[Error] Invalid command expression! ");
    }
  }

  /**
   * This is to printing information of disconnction when all clients
   * have already disconnected with the help of variable clients.
   *
   * The purpose of setting this method is that when a client is quit, the
   * connection is broken, which result in a null value when originally
   * calling clientDisconnected method.
   *
   * @param clients A linkedlist storing all clients who have ever made
   * connections with the server, no matter they are currently connected or not.
   */
  private void existingClientDisconnected(LinkedList<ConnectionToClient> clients) {
    for (ConnectionToClient cli: clients) {
      try {
        System.out.println("Client "+cli.getInfo("loginID")+" disconnects. ");
      }
      catch (Exception e) {}
    }
  }
}
//End of EchoServer class
