// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import ocsf.server.*;



/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Laganiegrave;re
 * @author Franccedil;ois Beacute;langer
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

    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }

  public void clientDisconnected(ConnectionToClient client){
    
    String msg = "Client disconnet at " + client;
    System.out.println(msg);
    this.sendToAllClients(msg);
  }

  public void clientConnected(ConnectionToClient client) {
    

    String msg = "Client connected at " + client;
    System.out.println(msg);
    this.sendToAllClients(msg);
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
    this.sendToAllClients(null);
  }

  public void handleMessageFromServerUI(String message) {
        if (message.startsWith("#")) {
            String[] index = message.split(" ");
            String command = index[0];
            switch (command) {
                case "#quit":
                    //closes the server and then exits it
                    try {
                        this.close();
                    } catch (IOException e) {
                        System.exit(1);
                    }
                    System.exit(0);
                    break;
                case "#stop":
                    this.sendToAllClients("#stop");
                    this.stopListening();
                    break;
                case "#close":
                    try {
                        this.close();
                    } catch (IOException e) {
                    }
                    break;
                case "#setport":
                    if (!this.isListening() && this.getNumberOfClients() < 1) {
                        super.setPort(Integer.parseInt(index[1]));
                        System.out.println("Port set to " + Integer.parseInt(index[1]));
                    } else {
                        System.out.println("Connection already Established.");
                    }
                    break;
                case "#start":
                    if (!this.isListening()) {
                        try {
                            this.listen();
                        } catch (IOException e) {
                            //error listening for clients
                        }
                    } else {
                        System.out.println("Server is up and running.");
                    }
                    break;
                case "#getport":
                    System.out.println("Current port is " + this.getPort());
                    break;
                default:
                    System.out.println("The command '" + command+ "' is not recognized.");
                    break;
            }
        } else {
            this.sendToAllClients(message);
        }
    }

  public void accept(){     //method for taking input

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
        ("Terminating.");
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
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }

    sv.accept();  
    //serverClient.accept();
  }








}
//End of EchoServer class
