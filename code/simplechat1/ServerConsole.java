// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

public class ServerConsole implements ChatIF 
{

  final public static int DEFAULT_PORT = 5555;
  

  EchoServer server;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param port The port to connect on.
   */
  public  ServerConsole(int port) 
  {
      server = new EchoServer(port, this);
      try{
          server.listen();
      } catch(Exception ex){
        System.out.println("ERROR - Could not listen for clients!");
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
        //need to check here if the server is running
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
    System.out.println(message);
  }
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number
    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
    try
    {
      host = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    ServerConsole chat= new ServerConsole(port);
    chat.accept();  //Wait for console data
  }
}

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Server UI.
   *
   * @param args[0] The host to connect to.
   */
  
//End of ConsoleChat class
