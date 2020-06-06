// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept(String login) 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;
	String cmd = ("#login " + login);
	client.handleMessageFromClientUI(cmd);

      while (true) 
      {
	message = fromConsole.readLine();
	if (message.equals("#sethost")) {
		// User didn't enter full command for changing host
		System.out.println("No host specified. Using default host...");
		client.handleMessageFromClientUI("#sethost localhost");
	}
	else 
        	client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
	System.out.println("Could not read from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string fto be displayed.
   */
  public void display(String message) 
  {
    System.out.println(message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] Login-id of the user to connect to a server.
   */
  public static void main(String[] args) 
  {
	String clientID = "";
    String host = "localhost";
    int port = 0;  //The port number

    try
    {
      clientID = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
	System.exit(1);
    }
    ClientConsole chat= new ClientConsole(host, DEFAULT_PORT);
    chat.accept(clientID);  //Wait for console data
  }
}
//End of ConsoleChat class
