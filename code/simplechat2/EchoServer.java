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
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  private ChatIF serverUI;
  /**
   * The boolean isOpen variable.  Allows the system to keep track if 
      if the server is open or closed.
   */

  private boolean isOpen = false;

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
   * @param serverUI The ServerConsole instance.
   */

  public EchoServer(int port){
    super(port);
  }
  public EchoServer(int port, ChatIF serverUI) throws IOException{
    super(port);
    this.serverUI = serverUI; 
    listen();
  }

  
  //Instance methods ************************************************
  
 
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
    String mesg = (String)msg; //cast Obj msg to String, used to concatenate later
    //if it is the #login command to the server, do this...
    if (mesg.contains("#login")){ //if it has the keyword "#login"
      String loginID = mesg.substring(7); //splice the loginID from it
      client.setInfo("#login",loginID); //assign loginID as a "#login" for this instance of client
      System.out.println("> Message received: "+mesg+" from "+client);
      System.out.println("> "+loginID+" has logged on.");

    }
    //if it is NOT the #login command, treat as an echo
    else{
      String loginSTR = (String) client.getInfo("#login"); //cast getInfo data to String
      System.out.println("> Message received: "+mesg+" from "+loginSTR);
      this.sendToAllClients(loginSTR+": "+mesg);
    }
  }

  
  public void handleMessageFromServerUI(String message){
   try{

      if (message.equals("#quit")){
        quit();
      }

      else if (message.equals("#stop")){
       stopListening();
       sendToAllClients("WARNING - Server has stopped listening for connections");
      }

      else if (message.equals("#close")){
        sendToAllClients("WARNING - Server has stopped listening for connections");
        sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
        close();
        

      }

      else if (message.equals("#getport")){
        serverUI.display("Current port: "+getPort());
      }

      else if (message.contains("#setport")){
        if (!isOpen){
          int newPort = Integer.parseInt( message.substring(9) );
          setPort(newPort);
        }
        else{
          serverUI.display("Cannot set port while connected to server.");
        }
      }

      else if (message.equals("#start")){
        listen();
      }

    //if the server is not responding to a command, treat it as a generic server msg from server
    else{
    sendToAllClients("SERVER MSG> "+message);}
    }
    catch(IOException e)
    {
      serverUI.display
        ("Could not send message to all clients.  Terminating server.");
      quit();
    } 
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted(){
    System.out.println
      ("Server listening for connections on port " + getPort());
      isOpen = true;
  }
  /**
   * This method overrides the one in the superclass.  Called
   * when the is server closed, but not terminated. Sets instance variable isOpen
   */
  protected void serverClosed(){
    isOpen = false;}
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped(){
    System.out.println("Server has stopped listening for connections.");
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when a client connects.
   */
  protected void clientConnected(ConnectionToClient client){
    System.out.println("> A new client is attempting to connect to the server");
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when a client disconnects.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client){
    String loginSTR = (String) client.getInfo("#login");
    sendToAllClients("> "+loginSTR+" has disconnected from the server.");}

  /**
   * This method overrides the one in the superclass.  Called
   * to shut the server down and terminates the program.
   */
  public void quit(){
    try{
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }
public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        handleMessageFromServerUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }


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
      sv.listen();
      sv.accept(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  
}
//End of EchoServer class
