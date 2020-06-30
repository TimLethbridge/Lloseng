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
   * @param port The port to connect on
   * @param user The username to  connect with.
   */
  public ClientConsole(String user, String host, int port)
  {
    try
    {
      client= new ChatClient(user, host, port, this);
    }
    catch(IOException exception)
    {
      System.out.println("Cannot open connection.  Awaiting command.");

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
    System.out.println("> " + message);
  }

  protected void connectionClosed() {
    System.out.println(" The server is currently shut down.the client quit");
  }

  public void connectionException(Exception exception) {
       System.out.println("WARNING - The server has stopped listening for connections");
   }


  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args)
  {
    String user = "";
    String host = "";
    int port = 0;  //The port number

    try
    {
      user = args[0];

    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      System.err.print("ERROR - No login ID specified.  Connection aborted.");
      System.exit(1);
    }

    try
    {
      host = args[1];
    }
    catch (ArrayIndexOutOfBoundsException e) {

      host = "localhost";

    }

    try
    {
      port = Integer.parseInt(args[2]);
    }
    catch (Throwable t) {

      port = DEFAULT_PORT;

    }


    ClientConsole chat= new ClientConsole(user ,host, port);



    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
