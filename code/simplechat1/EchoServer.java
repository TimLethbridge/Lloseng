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
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
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
		input += ("#server"); // Add identifier
		// Send everything to the clients
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
  
}
//End of EchoServer class
