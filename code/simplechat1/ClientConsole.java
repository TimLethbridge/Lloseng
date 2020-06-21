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
  
  
  public ClientConsole(String loginID, String host, int port) 
  {
    try 
    {
      client= new ChatClient(loginID, host, port, this);
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
  public void accept() 
  {
	
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;
      boolean log_in_out=true;
      while (true) 
      {
        message = fromConsole.readLine();
        char first = message.charAt(0);
        if (first == '#') {
        	if (message.equals("#quit")) {
    			System.out.println("The client has quit");
    			System.exit(1);
    		}
        	else if (message.equals("#logoff")) {
            	log_in_out = false;
            	client.handleMessageFromClientUI(client.getLogin()+" left the server");
            	System.out.println("The connection has closed");
            }
        	else if (message.split(" ")[0].equals(("#sethost"))) {
            	if (log_in_out==false) {
            		client.setHost(message.split(" ")[1]);
            		System.out.println("Host set to: " + client.getHost());
            	}
            	else {
            		System.out.println("You have to be logged out to set the host");
            	}
            }
        	else if (message.split(" ")[0].equals(("#setport"))) {
            	if (log_in_out==false) {
            		int holder = Integer.parseInt(message.split(" ")[1]);
            		client.setPort(holder);
            		System.out.println("Port set to: " + client.getPort());
            	}
            	else {
            		System.out.println("You have to be logged out to set the port");
            	}
            }
        	else if (message.equals("#login")) {
            	if (log_in_out==true) {
            		System.out.println("You are logged in");
            	}
            	else {
            		client.openConnection();
            		log_in_out=true;
            		System.out.println("Welcome Back!");
            	}
            }
        	else if (message.equals("#gethost")) {
            	System.out.println(client.getHost());
            }
        	else if (message.equals("#getport")) {
            	System.out.println(client.getPort());
            }
        	else {
        		System.out.println("Not a proper command");
        	}
        }
        else {
        	client.handleMessageFromClientUI(message);
        }
        
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
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
 * @throws IOException 
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number
    String log_in = "";
    if (args.length>0) {
    	log_in=args[0];
    }
    else {
    	System.out.println("You must have a login ID");
    }
    try {
    	host=args[1];
    }
    catch (ArrayIndexOutOfBoundsException e) {
    	host="localhost";
    }
   
    try{
      port = Integer.parseInt(args[2]);
    }
    catch(ArrayIndexOutOfBoundsException e){
      port = DEFAULT_PORT;
    }
    
   
     ClientConsole chat= new ClientConsole(log_in, host, port);
     chat.accept();  //Wait for console data
     
      //System.out.println(args[1]);
    

    }
  }

//End of ConsoleChat class
