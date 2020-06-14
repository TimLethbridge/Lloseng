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

  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the server.
   */
  ChatIF serverUI;

  
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
	String mes = (String) msg;
	// Don't display the command to the server interface and send it to other clients
	if (mes.contains("#login")) 
	{
		// Log the user into the system
		int ind = mes.indexOf(" "); // Start of identification string
		int len = mes.length(); // End of identiification string
		String ID = mes.substring(ind+1, len);
		client.setInfo("Login ID", ID);

		// Indicate who connected to the administrator
		System.out.println("A new client is attempting to connect to the server.");
		System.out.println("Message received #login " + client.getInfo("Login ID") + " from null.");
		System.out.println(client.getInfo("Login ID") + " has logged on.");
		try {
			// Add > so that client's console does not mistake the login as a message
			client.sendToClient(client.getInfo("Login ID") + " has logged on.");
		}
		catch (IOException e) {
			System.out.println("Could not notify the client!");
		}

		
	}
	else {
	    System.out.println("Message received: " + msg + " from " + client.getInfo("Login ID"));
	    this.sendToAllClients(client.getInfo("Login ID") + " " + msg);
	}
  }

  /** 
   * This method reads user input's from the console
   *
   * @param in Input from the console
   */
   public void handleInputFromServerConsole(String input) 
   {
	// Check if message is a command
	if (input.charAt(0) == '#') {
	// Split the message into two
	String[] mes = input.split(" ", 2);
	// Select appropriate function
	switch(mes[0]) {
		case "#close":
			try {
				close();
			}
			catch(IOException e) 
			{
				System.out.println(": " + e);
			}
			break;
		case "#stop": 
			stopListening();
			break;
		case "#quit": 
			try {
				close();
				System.exit(0);
			}
			catch(IOException e) 
			{
				System.out.println(": " + e);
			}
			break;
		case "#setport": 
			try {
				setPort(Integer.parseInt(mes[1]));
			}
			catch (Exception e) {
				System.out.println("No port specified. Using default port...");
				setPort(5555);
			}
			break;
		case "#start": 
			try {
				listen();
			}
			catch(IOException e) 
			{
				System.out.println(": " + e);
			}
			break;
		case "#getport": 
			System.out.println(getPort());
			break;
		default:
			System.out.println("Command not valid!");
			break;
		}
	}
	else
      	{
		
		// Echo to the server console
		System.out.println("SERVER MESSAGE> " + input);
		// Send to the clients
		input += ("#server"); // Add identifier
		this.sendToAllClients(input);
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

  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
	sendToAllClients("WARNING - Server has stopped listening for connections.");
  }

  protected void serverClosed()
  {
    System.out.println("Server is closed");
    sendToAllClients("WARNING - The server has stopped listening for connections. SERVER SHUTTING DOWN! DISCONNECTING!");
  }
  
}
//End of EchoServer class
