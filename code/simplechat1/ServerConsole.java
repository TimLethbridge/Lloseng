import java.io.*;
import ocsf.server.*;
import common.*;


public class ServerConsole implements ChatIF {

  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables **********************************************

  /**
   * The instance variable of the echoserver.
   */
  EchoServer server;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param server The server to connect to.
   */
  public ServerConsole(EchoServer server) {
    this.server = server;
  }


  //Instance methods ************************************************

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the server's message handler.
   */
  public void accept() {
    try
    {
      BufferedReader fromConsole =
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true)
      {
        message = fromConsole.readLine();
        server.handleMessageFromServer(message);
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
   * This method is responsible for the creation of
   * the server instance (there is a UI in this phase).
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

    EchoServer sv = new EchoServer(port);

    try
    {
      sv.listen(); //Start listening for connections
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }

    ServerConsole chat = new ServerConsole(sv);
    chat.accept();
  }
}
