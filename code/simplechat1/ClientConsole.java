// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package simplechat1;

import java.io.*;
import simplechat1.client.*;
import simplechat1.common.*;

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
  public ClientConsole(String loginid, String host, int port)
  {
    try 
    {
      //System.out.println(host+"  :"+ port);
      client= new ChatClient(loginid, host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection! Terminating client.");
      //System.exit(1);
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
    //Objeect id=client.getInfo("loginId");
    System.out.println( message);
  }

  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] login id
   * @param args[1] host
   * @param args[2] port
   */
  public static void main(String[] args) 
  {
    String loginId ="";
    int port = 0;
    String host = "";


    try {
      loginId = args[0];

    } catch (Exception e) {
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
      System.exit(0);
    }


    try
    {
      host = args[1];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    try{
      port = Integer.valueOf(args[2]);
    }
    catch(ArrayIndexOutOfBoundsException e){
      port = DEFAULT_PORT;
    }
    //System.out.println(loginId);
   // System.out.println(host);
    //System.out.println(port);
    ClientConsole chat= new ClientConsole(loginId,host, port);
    chat.accept();  //Wait for console data

  }

}

//End of ConsoleChat class