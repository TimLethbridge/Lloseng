// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;
import common.*;
import java.util.Scanner;

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
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {



    if (msg.toString().startsWith("#")) {
    String[] pa = msg.toString().substring(1).split(" ");
    if (pa[0].equalsIgnoreCase("login") && pa.length > 1) {
        if (client.getInfo("user") == null) {
            client.setInfo("user", pa[1]);
            System.out.println("Message received: " + msg + " from " + client.getInfo("user"));
            this.sendToAllClients(client.getInfo("user") + " has logged on ! ");

        } else {
            try {
                client.sendToClient("Your name has already been set ");
            } catch (IOException e) {}
        }

    }
  }    else {
    if (client.getInfo("user") == null) {
        try {
            client.sendToClient(" Please set a username before messaging the server ! ");

        } catch (IOException e) {}


  }

  System.out.println("Message received: " + msg + " from " + client.getInfo("user"));

  this.sendToAllClients(client.getInfo("user") + " > " + msg);

}

}

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */

   public void handleMessageFromServerConsole(String message) {
     if(message.startsWith("#"))  {

       int nPort;

       Scanner sc = new Scanner(System.in);


       if(message.equals("#quit")) {
         try
         {
           this.close();
         } catch (IOException e) {
           System.exit(1);
         }
         System.exit(0);
       }

       if(message.equals("#stop")) {
         this.stopListening();
       }

       if(message.equals("#close")) {
           try
           {
             this.close();
           }
           catch(IOException e) {}
         }


       if(message.equals("#setport")) {
         if(!this.isListening() ) {
           System.out.println("Please enter a port");
           nPort = sc.nextInt();
           super.setPort(nPort);
           System.out.println("The current host is " + this.getPort());
         } else {
           System.out.println("Sorry, but to setup the port, you must be logged off");
           }

       }

       if(message.equals("#getport")) {
         System.out.println("The current port is " + this.getPort());
       }

       if(message.equals("#start")) {
         if (!this.isListening()) {
                    try {
                        this.listen();
                    } catch (IOException e) {}
                } else {
                    System.out.println("We are already started and listening for clients!.");

         }



       }



       }

     }
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

  protected void clientConnected(ConnectionToClient client) {
    System.out.println(" A new client is attempting to connect to the server. Say hello ! ");
    System.out.println(client.getInfo("user") + " has logged on ! ");
    

  }

  synchronized protected void clientDisconnected(ConnectionToClient client) {

    System.out.println(" Oh no ! Someone leave ! Say byebye ! ");
    System.out.println(client.getInfo("user") + " has logged off ! ");
    this.sendToAllClients(client.getInfo("user") + " has logged off ! ");

  }

  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
      String message = client + " disconnect ";
      System.out.println(client + " disconnect ");
      this.sendToAllClients(message);
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

//End of EchoServer class
