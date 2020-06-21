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
  public static boolean hasConnected = false;
  public static int port;

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
  public void handleMessageFromClient (Object msg, ConnectionToClient client)
  {
    String serverMSG = "";
    String messageSent = msg.toString();
    String loginValid = "";
    String setPortValid = "";
    loginValid = messageSent.substring(0,6);

    if (messageSent.length()>=11){
      serverMSG = messageSent.substring(0,11);
    }
    if (messageSent.length() >= 8) {//verifie if the user has used #setPort XXXX and #setHost
      setPortValid = messageSent.substring(0, 8);
    }


    if (loginValid.equals("#login") && !hasConnected){
      System.out.println("Message received "+messageSent);
      client.setInfo("client ID: ",msg);
      hasConnected = true;
    }else if(loginValid.equals("#login") && hasConnected){
      handleMessageFromClient("You are already logged in!",client);
    }else {
      switch (messageSent) {
        case "Quit.....":
          System.out.println("Server is quitting...");
          System.exit(0);
          break;
        case "Stop":
          stopListening();
          break;
        case ": #login is not detected, Terminating client":
          try {
            client.close(); //closing the client from the server
            handleMessageFromClient("#login is not detected, terminating client!", client);
          } catch (IOException exp) {
            System.out.println("IOException occured.");
          }
          break;
        case "Close":
          stopListening();
          try {
            client.close();
          } catch (IOException ex) {
            System.out.println("IOException occured.");
          }
          break;
        case "#getPort":
          handleMessageFromClient("the port number is " + getPort(), client);
          break;
        case "Start":
          if (!isListening()) {
            System.out.println("Server will start listening for new clients...");
            serverStarted();
          } else {
            System.out.println("Server is already listening for clients!");
          }
          break;
        case "Client has logged off":
          System.out.println("Client has logged off");
          try {
            client.close();
          } catch (IOException ex) {
            System.out.println("IOException occured.");
          }
          break;
        default:
          if (setPortValid.equals("#setPort")){
              int portToReturn = 0;
              System.out.println("Setting the new port... ");
              String newPortNumber = messageSent.substring(10, messageSent.length() - 1);
              portToReturn = Integer.parseInt(newPortNumber);
              System.out.println("Your Port is: " + portToReturn);
              System.out.println(" ");
              setPort(portToReturn);
              EchoServer sv = new EchoServer(getPort() );

//            try
//            {
//              sv.listen(); //Start listening for connections
//            }
//            catch (Exception ex)
//            {
//              System.out.println("ERROR - Could not listen for clients!");
//            }
          }else{
            System.out.println(msg);
            this.sendToAllClients(msg);
          }
          break;


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

  //client connected ************************************************
  protected void clientConnected(ConnectionToClient client)
  {
    //System.out.println("The client "+ client +" has connected to the server");
    System.out.println("A new client is attempting to connect to the server.");
  }


  //client disconnected ********************************************
  synchronized protected void clientDisconnected(ConnectionToClient client)
  {
    System.out.println("The client "+ client +" has disconnected from the server");
  }

  //server closed****************************************************
//  public void close() {
//    try {
//      System.out.println("Server quit...");
//    } catch (IOException e) {
//      System.out.println("There was an error while quitting");
//    }
//  }

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
    port = 0; //Port to listen on

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
