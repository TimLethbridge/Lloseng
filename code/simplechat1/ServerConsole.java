//Code written for Question 6b. Creates a ServerConsole which implements EchoServer
//And allows it to communicate with the other users



import java.io.*;
import common.*;

/**
 * This class constructs the UI for a server.  It implements the
 * chat interface in order to activate the display() method.
*/

public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the Server that created this ConsoleChat.
   */
  EchoServer server;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {
      server= new EchoServer(port);
	  try 
    {
      server.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
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
        server.handleMessageFromServerUI(message);
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
	//I don't actually need to call this method in EchoServer but it is implemented for future use
	//makes the messages comming from the server begin with 'SERVER MSG>'
    System.out.println("SERVER MSG> " + message); 
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Server UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number

	try
    {
      port = Integer.valueOf(args[0]);
    }
	//If there's no 1st argument, it will use the DEFAULT_PORT
    catch(ArrayIndexOutOfBoundsException e)
    {
      port = DEFAULT_PORT;
    }
    ServerConsole chat= new ServerConsole(port); //will use the port variable instead of DEFAULT_PORT
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
