// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import common.ChatIF;
import java.io.*;
import client.*;
import common.*;
/**
 * This class constructs the UI for a chat server, followed ClientConsole.java.  
 * It implements the chatIF interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ServerConsole implements ChatIF {

//Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  EchoServer echoServer;

  
//Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {
      echoServer = new EchoServer(port,this);
    try 
    {
      echoServer.listen(); 
    } 
    catch(IOException exception) 
    {
      System.out.println("Could not listen for clients!"
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
        client.handleMessageFromClientUI(message);
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
   * This method is responsible for the creation of the Server UI.
   *
   * @param args[0] The port to listen to.
   */
  public static void main(String[] args) 
  {

    int port = 0;  //The port number

    try
    {
      port = Integer.parseInt(args[0]); 
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      port = DEFAULT_PORT;
    }
       
    ServerConsole sv = new ServerConsole(port);
    sv.accept();  

}
//End of ServerConsole class



