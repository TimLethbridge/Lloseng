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
  public ClientConsole(String clientId, String host, int port) 
  {
    try 
    {
      client= new ChatClient(clientId, host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Cannot open connection."
                + " Awaiting command.");
    }
  }

  
  //Instance methods ************************************************
  
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
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
	  //For a message coming from the server to not have a > prepended
	try{
	if (message.charAt(10)=='>'){
		System.out.println(message);
	}
	else{
		System.out.println("> " + message);
	}
	}
	catch(IndexOutOfBoundsException e){
		System.out.println("> " + message);
	}
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number
	String clientId = ""; //The user's loginId 
	
	
	//Question 7a
	//Assigns the first argument as the loginId
	try {
		clientId=args[0];
	}
	//If no loginId is provided an error message is printed and the user is disconnected
	catch(ArrayIndexOutOfBoundsException e0){
		System.out.println("Cannot connect to server without login Id.");
		System.exit(0);
	}
	
    try
    {
		//Question 7 a
		//changed to second arg since adding clientId
      host = args[1]; 
    }
    catch(ArrayIndexOutOfBoundsException e1)
    {
      host = "localhost";
    }
	
	//Question 5b 
	//This try loop checks to see if there is a second argument 
	//If that's the case, it will assign it as the port number 
	try
    {
		//Question 7 a
		//changed to third arg since adding clientId
      port = Integer.valueOf(args[2]); 
    }
	//If there's no 2nd argument, it will use the DEFAULT_PORT
    catch(ArrayIndexOutOfBoundsException e2)
    {
      port = DEFAULT_PORT;
    }
	//will use the port variable defined above instead of DEFAULT_PORT
	//added the loginId to the constructor (Question 7a)
    ClientConsole chat= new ClientConsole(clientId, host, port); 
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
