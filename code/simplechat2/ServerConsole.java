import java.io.*;
import common.*;
import ocsf.server.*;

public class ServerConsole implements ChatIF{
	
  /*INSTANCE VARIABLES*/
  EchoServer server; //instance of a server

  /*CLASS VARIABLES*/
  final public static int DEFAULT_PORT = 5555; //default port value


  /*CONSTRUCTORS*/
  /**
  * @param port The port the server starts on.
  */
	public ServerConsole(int port)throws IOException{
    server = new EchoServer(port, this);
  }

  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the server's message handler.
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

  public void display(String message){
		System.out.println("SERVER MSG> "+message);
	}

   public static void main(String[] args) throws IOException 
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
      sv.accept(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}