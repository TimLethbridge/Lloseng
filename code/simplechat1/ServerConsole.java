//ServerConsole.java

//Similar to ClientConsole, but with modification to allow for server inputs
import java.io.*;
import client.*;
import common.*;

public class ServerConsole implements ChatIF{

  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  EchoServer server;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port){
  	server = new EchoServer(port);
    try 
    {
      server.listen();
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Couldnt listen for clients");
      System.exit(0);
    }
  }

  
  //Instance methods ************************************************

  /**
 	 * Hook method called after the connection has been closed. The default
 	 * implementation does nothing. The method may be overriden by subclasses to
 	 * perform special processing such as cleaning up and terminating, or
 	 * attempting to reconnect.
 	 */
 	protected void connectionClosed() {
 		System.out.println("The server has shutdown.");
 	}

  	/**
 	 * Hook method called each time an exception is thrown by the client's
 	 * thread that is waiting for messages from the server. The method may be
 	 * overridden by subclasses.
 	 * 
 	 * @param exception
 	 *            the exception raised.
 	 */
 	protected void connectionException(Exception exception) {
 		connectionClosed();
 	}
  
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
    System.out.println("SERVER MSG> " + message);
  	}

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    int port = 0;  //The port number

    try
    {
      port = Integer.parseInt(args[0]);
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT;
    }
    ServerConsole server= new ServerConsole(port);
    server.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
