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
  String loginid;
  
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
  public ClientConsole(String host, int port, String loginid) 
  {
	loginid = loginid;
    try 
    {
      client = new ChatClient(host, port, this);
	  client.handleMessageFromClientUI("#login " + loginid);
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

      while (true) 
      {
        message = fromConsole.readLine();
		if (message.toString().charAt(0) == '#'){
			commands(message.split(" "));
			
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
  
  protected void commands(String[] splitMessage) {
		if (splitMessage[0] == "#quit") {
			try{
				client.closeConnection();
				client.quit();
			}
			catch(Exception e) {
			}
		}
		else if (splitMessage[0] == "#logoff"){
			try {
				client.closeConnection();
			}
			catch(Exception e) {
			}
		}
		else if (splitMessage[0] == "#sethost"){
			if(!client.isConnected()){
				client.setHost(splitMessage[1]);
			}
		}
		else if (splitMessage[0] == "#setport"){
			if(!client.isConnected()) {
				client.setPort(Integer.parseInt(splitMessage[1]));
			}
		}
		else if (splitMessage[0] == "#login"){
			try{
				client.openConnection();
			}
			catch(IOException e) {
			}
		}
		else if (splitMessage[0] == "#gethost"){
			System.out.println("Host: "+client.getHost());
		}
		else if (splitMessage[0] == "#getport"){
 			System.out.println("Port: "+client.getPort());
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
    String host = "";
	String loginID = "";
    int port = 0;  //The port number

    if(args.length==0){
	  System.out.println("ERROR - No login ID specified. Connection Aborted.");
	  System.exit(1);
    }
	else if(args.length == 1){
		loginID = args[0];
		host = "localhost";
    }
    ClientConsole chat= new ClientConsole(host, DEFAULT_PORT, loginID);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
