import java.io.*;
import client.*;
import common.*;


public class ServerConsole implements ChatIF 
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
  EchoServer server;

  
  //Constructors ****************************************************
  public ServerConsole( int port) 
  {
    try 
    {
      server= new EchoServer(port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }
  /*works similarly to the one in clientconsole*/
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

  /*works similarly to the one in clientconsole*/
  public void display(String message) 
  {
    System.out.println("> " +message);
  }

  

  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  /*moved main method from echoserver to here and changed it so it would create a server console instead, serverconsole constructor calls echoserver constructor*/
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
	
    ServerConsole sc = new ServerConsole(port);
    
    try 
    {
      sc.server.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    sc.accept();
  }
  	
}
