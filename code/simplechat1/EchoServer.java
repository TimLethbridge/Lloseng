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
public class EchoServer extends AbstractServer {
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
  public EchoServer(int port) {
    super(port);
  }
  
  //Instance methods ************************************************
	
	// When an exception of a client occurs
	public void clientException(ConnectionToClient client) {
		System.out.println("An exception occured.");
		clientDisconnected(client);
	}
	
	// When a client connects
	public void clientConnected(ConnectionToClient client) {
		System.out.println("A client is now connected.");
	}
  
	// When a client disconnects
	public void clientDisconnected(ConnectionToClient client) {
		System.out.println("A client is now disconnected.");
	}
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    String stringMsg = (String) msg;
	String[] splitMsg = stringMsg.split(" ");
	if (splitMsg[0].equals("#login")) {
		if (splitMsg.length > 1) {
			// loginID is not yet defined
			if (client.getInfo("loginID") == null) {
				client.setInfo("loginID", splitMsg[1]);
				sendToAllClients(splitMsg[1] + " logged in.");
			}
			// loginID is already defined
			else {
				try {
					client.sendToClient("Error. " + splitMsg[1] + " is already logged in.");
					client.close();
				}
				catch (IOException e) {
					System.out.println("Unexpected error while attempting to log in.");
				}
			}
		} else {
			try {
				client.sendToClient("Error. Must define login ID.");
			}
			catch (IOException e) {
				System.out.println("Unexpected error while attempting to log in.");
			}
		}
	}
	// Server echoes each message to all clients
	else {
		System.out.println("Message received: " + msg + " from " + client);
		this.sendToAllClients(client.getInfo("loginID") + " : " + msg);
	}
  }
  
	/**
	* This method handles any messages received from the server.
	*
	* @param msg The message received from the server.
	*/
	public void handleMessageFromServer(String message) {
		// Anything starting with "#" is considered to be a command
		if (message.startsWith("#")) {
			String[] splitMsg = message.split(" ");
			switch (splitMsg[0]) {
				// Server terminates gracefully
				case "#quit":
					System.out.println("Commanded to quit.");
					System.exit(0); // Exit is intentional, so error code of 0
					break;
				// Server stops listening for new clients
				case "#stop":
					System.out.println("Commanded to stop.");
					stopListening();
					break;
				// Server stops listening for new clients and disconnects all existing clients
				case "#close":
					System.out.println("Commanded to close.");
					stopListening();
					try {
						close();
					}
					catch (IOException e) {}
					break;
				// Sets server port number for next connection, but only if server is closed
				case "#setport":
					if (!isListening()) {
						System.out.println("Commanded to set port.");
						setPort(Integer.parseInt(splitMsg[1]));
					} else {
						System.out.println("Error. Cannot set port because the server is not closed.");
					}
					break;
				// Server starts listening for new clients, but only if server is stopped
				case "#start":
					if (!isListening()) {
						System.out.println("Commanded to start.");
						try {
							listen();
						}
						catch (IOException e) {}
					} else {
						System.out.println("Error. Cannot start because the server is not stopped.");
					}
					break;
				// Display current port number
				case "#getport":
					System.out.println("Port number: " + getPort());
					break;
				// Command not recognized
				default:
					System.out.println("Command not recognized");
					break;
			}
		}
	}  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()  {
    System.out.println("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()  {
    System.out.println("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) {
    int port = 0; //Port to listen on

    try {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t) {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    ServerConsole sc = new ServerConsole(port);
    
    try {
      sc.accept();
    } 
    catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
