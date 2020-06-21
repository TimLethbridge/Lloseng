// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import ocsf.server.*;
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
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  private EchoServer sv;
  private boolean serverClosed = false;
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {
	sv = new EchoServer(port, this);
  }
  
	public EchoServer get() {
		return sv;
	}
	
	public void handleMessageFromServer(String message) {
		String temp[] = message.split(" ");
		
		if(message.equals("#quit")) {
			try {
				displayMessage("client has quit");
				System.out.println("Closing Connection");
				sv.close();
				System.exit(0);
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(message.equals("#stop")) {
			try {
				displayMessage("Server has stopped listening for new clients");
				System.out.println("Closing Connection");
				sv.stopListening();
				serverClosed = true;
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(message.equals("#close")) {
			try {
				sv.close();
				serverClosed = true;
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(temp[0].equals("#setport") && serverClosed) {
			try {
				System.out.println("Setting port to: " + Integer.parseInt(temp[1]));
				sv.setPort(Integer.parseInt(temp[1]));
				sv.listen();
				serverClosed = false;
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(message.equals("#start")) {
			try {
				displayMessage("client is logging in");
				System.out.println("Opening connection to server");
				sv.listen();
				serverClosed = false;
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(message.equals("#getport")) {
			try {
				System.out.println("Port is currently: " + sv.getPort());
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else if(message.charAt(0) == '#') {
			try {
				System.out.println("ERROR! Command not valid");
			} catch(Exception e) {
				System.out.println("ERROR!");
			}
		} else
			displayMessage(message);
	}

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
   public void displayMessage(String msg) {
	   sv.sendToAllClients("SERVER MSG> " + msg);
   }
   
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
		handleMessageFromServer(message);
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
	
    ServerConsole sv = new ServerConsole(port);
    
    try 
    {
      sv.get().listen(); //Start listening for connections
      sv.accept();
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of ConsoleChat class
