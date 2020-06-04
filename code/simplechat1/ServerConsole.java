import java.io.*;
import client.*;
import common.*;
import java.util.Scanner;

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
  EchoServer server;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  
  
  public ServerConsole(int port){

	  try{
		  server = new EchoServer(port);
		  server.listen();
	  }
	  catch(IOException e){
		  System.out.println(e);
		  System.exit(0);
	  }
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
        System.out.println(message);
    }
  
  
 public static void main(String[] args) 
  {
	Scanner scanner = new Scanner(System.in);
    String host = "";
	String loginid = "";
    int port = 0;  //The port number
	
    try
    {
      host = args[1];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
	System.out.print("Enter port number: ");
	String user_port_string = scanner.nextLine();
	try{
		int user_port = Integer.parseInt(user_port_string);
		ServerConsole chat = new ServerConsole(user_port);
		chat.accept();
	}
	catch(Exception e){
		System.out.println("Your port was invalid. Now using default port.");
		ServerConsole chat = new ServerConsole(DEFAULT_PORT);
		chat.accept();
	}


  }
  
  
}
