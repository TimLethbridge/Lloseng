import java.io.*;
import client.*;
import common.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class constructs the UI for a Echo Server.  It implements the
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

  //Instance variables **********************************************

  /**
   * The instance of the server that created this ServerConsole.
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
    try
    {
      server= new EchoServer(port, this);
    }
    catch(IOException exception)
    {
      System.out.println("Error: Can't setup server!"
                + " Terminating.");
      System.exit(1);
    }
  }


  //Instance methods ************************************************

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the Server's message handler.
   */
  public void accept()
  {
    try{
      server.listen();
    }catch (Exception ex){
      System.out.println("ERROR - Could not listen for clients!");
    }

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
    System.out.println("> " + message);
  }


  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Server UI.
   *
   * @param args[0] The port to connect to
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

    ServerConsole serv= new ServerConsole(port);

    serv.accept();  //Wait for console data
  }
}
//End of ServerConsole class
