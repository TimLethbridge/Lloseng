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

  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the serverUI.
   */
  ChatIF serverUI;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   * @param serverUI The refence to the server interface
   */
  public EchoServer(int port, ChatIF serverUI)
  throws IOException{
    super(port);
    this.serverUI = serverUI;
  }

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  throws IOException{
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
      String temp = String.valueOf(msg);
      char [] messg = temp.toCharArray();
      if(messg[0] == '#'){
        temp = temp.replace("#", "");
        String [] messageSplit = temp.split(" ");

        if(messageSplit[0].equals("login") &&
        client.getInfo("Login ID") == null){

          client.setInfo("Login ID", messageSplit[1]);
          client.setInfo("First Command", "Login");
          System.out.println("Mesage recieved: #login " + client.getInfo("Login ID") + " from null ");
          System.out.println(client.getInfo("Login ID") + " has logged on ");
          this.sendToAllClients(client.getInfo("Login ID") + " has logged on ");

        }else if(messageSplit[0].equals("login") &&
        client.getInfo("Login ID") != null){

          try{
            client.sendToClient("ERROR: Already logged in to server!");
        }catch(IOException e){
        }
      }
      }else{

        if(client.getInfo("First Command") != "Login"){
          try{
            client.sendToClient("ERROR: No login command found!");
            client.close();
          }catch (IOException e){

          }
        }else{
          System.out.println("Message recieved: " + msg + " from " + client.getInfo("Login ID"));
          this.sendToAllClients(client.getInfo("Login ID") + "> "+  msg);
        }

      }


  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromServerUI(String message)
  {
    String temp = (String) message;
    String changeValue = "";
    char [] messg = temp.toCharArray();
    if(messg[0] == '#'){
      temp = temp.replace("#", "");
      String [] messageSplit = temp.split(" ");
      if(messageSplit[0].equals("setport")){
        handleCommandFromServerUI(messageSplit[0], messageSplit[1]);
      }else{
        handleCommandFromServerUI(temp);
      }

    }else{
        if(getNumberOfClients() > 0){
          sendToAllClients("SERVER MSG> "+ message);
        }else{
          serverUI.display
            ("No Client connected!");
        }

      }

    }

  /**
   * This method handles all commands coming from the UI
   *
   * @param cmd The cmd from the UI.
   * @param change The value for "set" commands from the UI
   */

  private void handleCommandFromServerUI(String cmd){

    switch(cmd){
      case "quit":
      serverUI.display("Terminating....");
      try{
        close();
        System.exit(0);
      }catch (IOException e){
      }
        break;

      case "stop":
      serverUI.display("Terminating listening program....");
      stopListening();
      this.sendToAllClients("WARNING: Server has stopped listening for connections");
        break;

      case "close":
      serverUI.display("Closing....");
      try{
        this.sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
        close();
      }catch (IOException e){
      }
        break;

      case "getport":
      serverUI.display("Port: " + String.valueOf(getPort()));
        break;

      case "start":
      if(!isListening()){
        try{
        listen();
      }catch (IOException e){
        serverUI.display("ERROR: Cannnot start listening");
      }

      }else{
        serverUI.display("Server is already listening!");
      }
        break;
    }
  }


/**
 * This method handles all commands coming from the UI
 *
 * @param cmd The cmd from the UI.
 * @param change The value for "set" commands from the UI
 */

private void handleCommandFromServerUI(String cmd, String change){
  switch(cmd){
    case "setport":
    if(!isListening() && getNumberOfClients() == 0){
      int newPort = Integer.valueOf(change);
      setPort(newPort);
      serverUI.display("New Port Number: " + newPort);
    }else if (isListening()){
      serverUI.display("Server is still listening. Close the server to change ports");
    }else{
      serverUI.display("Clients are still connected. Close the server to change ports");
    }
      break;
  }
  }


  private static String charRemoveAt(String str, int p) {
        return str.substring(0, p) + str.substring(p + 1);
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

  //Class methods ***************************************************
  /**
   * This method is called each time a new client connection is
   * accepted.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client is attempting to connect to the server");
  }
  /**
   * This method is called each time a client disconnects.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(
    ConnectionToClient client) {
      System.out.println(client.getInfo("Login ID") + " has disconnected from the server");
      this.sendToAllClients(client.getInfo("Login ID") + " has disconnected from the server");
      client.setInfo("First Command", "Login");
    }

  /**
   * This method is called each time an exception is thrown in a
   * ConnectionToClient thread.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
      System.out.println(client.getInfo("Login ID") + " has been disconnected from the server");
      this.sendToAllClients(client.getInfo("Login ID") + " has disconnected from the server");
      client.setInfo("First Command", "Login");
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

       ServerConsole sv = new ServerConsole(port);
       sv.accept();


   }
}
//End of EchoServer class
