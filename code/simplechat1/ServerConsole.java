import common.*;
import java.io.*;

public class ServerConsole implements ChatIF {
  //Instance variables **********************************************
  
  /**
   * The instance of the server that created this EchoServer.
   */
  EchoServer sv;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param port The port that server listens to.
   */
   public ServerConsole(int port) 
   {
      sv = new EchoServer(port);
   } 
  
  //Instance methods ************************************************
  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string fto be displayed.
   */
  public void display(String message) 
  {
    System.out.println("SERVER MSG> " + message);
  }

	/**
	 * This method allows the user of console for commands and 
	 * messages to the client
	 */
	public void console() {
		try
	    	{
	      		BufferedReader fromConsole = 
			  new BufferedReader(new InputStreamReader(System.in));
	      		String input;
			sv.listen();

	      		while (true) 
	      		{
			  input = fromConsole.readLine();
			  sv.handleInputFromServerConsole(input);
	      		}
	    	} 
	    	catch (Exception ex) 
	    	{
	      	System.out.println
			("Unexpected error while reading from console!");
	    	}
	}

  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
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
	
    ServerConsole serv = new ServerConsole(port);
    
    try 
    {
	serv.console(); // Allow access to server console
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
	
}
