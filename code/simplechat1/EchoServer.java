// Test push
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
  private boolean serverIsOpen = false ;
  private ChatIF serverUI;
  
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
  public EchoServer(int port,ChatIF serverUI) throws IOException{     
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
     String mesg = (String)msg; 
    
     if (mesg.contains("#login")){ 
       String loginID = mesg.substring(7); 
       client.setInfo("#login",loginID); 
       System.out.println("> Message received: "+mesg+" from "+client);
       System.out.println("> "+loginID+" has logged on.");
      }
     
     else{
       String loginSTR = (String) client.getInfo("#login"); //cast getInfo data to String
       System.out.println("> Message received: "+mesg+" from "+loginSTR);
       this.sendToAllClients(loginSTR+": "+mesg);
     }
   }
  /*
   *
   */
  public void handleMessageFromServerUI(String message){
    try{
      if (message.equals("#quit")) {
          sendToAllClients("The server is quitting...");
          quit();
      }

      else if (message.equals("#stop")) {
        sendToAllClients("The server has stopped listening for new connections...");
        stopListening();
      }

      else if (message.equals("#close")) {
        sendToAllClients("Server is shutting down...\nServer will now disconnect...");
        close();
        
      }

      else if (message.startsWith("#setport")) {
        if (!serverIsOpen) {
          int port = Integer.parseInt(message.split("\\s+")[1]);
          setPort(port);
        }
        else{
          serverUI.display("Cant set port");
        }






        /*
        if (!serverIsOpen) {
          serverUI.display("Invalid command\nCannot set port if server is open\nPlease close server with command #close");
          return;        
        }
        try{
          int port = Integer.parseInt(message.split("\\s+")[1]);
          setPort(port);
          serverUI.display("Port has been set to :"+port);
        }
        catch(Exception e){
          serverUI.display("Invalid port");
          return;
        }
        */
      }

      else if (message.equals("#getport")) {
        serverUI.display("Port: "+getPort());
        
      }
      else if (message.equals("#start")) {
        listen();
      }
      else if (message.startsWith("#")) {
        serverUI.display("Invalid command. Please try again.\nPossible commands are:\n#quit\t: to quit the server\n#stop\t: to stop listening for new clients\n#close\t: to stop listening for new clients and disconnect all existing clients\n#start\t: to start the server, only valid if stopped\n#setport\t: to set the port if stopped\n#getport\t: to get the current port");
      }
      else{
        sendToAllClients("SERVER MSG >"+message);
      }
    }
    catch(IOException e)
     {
       serverUI.display
         ("Could not send message to all clients.  Terminating server.");
       quit();
     } 
  }

  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new user has connected to the server.");
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(
    ConnectionToClient client) {
    //System.out.println("A user has disconnected from the server.");

    String loginSTR = (String) client.getInfo("#login");
    sendToAllClients("> "+loginSTR+" has disconnected from the server.");

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
    ConnectionToClient client, Throwable exception) {
    clientDisconnected(client);
  }

  
  

  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println("Server listening for connections on port " + getPort());
    serverIsOpen = true;
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
    serverIsOpen = false;
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  public void quit(){
    try{close();}
    catch(IOException e){}
    System.exit(0);
  }

  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
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
      sv.accept();
    }
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
