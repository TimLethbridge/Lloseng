
import java.io.*;
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

    /**
     * Constructs an instance of the ClientConsole UI.
     *
     * @param port The port to connect on.
     */
    public ServerConsole(int port)
    {
        server = new EchoServer(port);
        try
        {
            server.listen();
        }
        catch(IOException exception)
        {
            System.out.println("Error!!!!: Can't setup connection!"
                    + " Terminating server.");
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
                server.handleMessageFromServer(message);
                this.display(message);
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
        System.out.println("SERVER MSG > " + message);
    }


    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of the Client UI.
     *
     * @param args[0] The host to connect to.
     */
    public static void main(String[] args)
    {
        int port;
    
        // set port
        try
        {
            port = Integer.parseInt(args[0]);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            port = DEFAULT_PORT;
        }

        ServerConsole chat= new ServerConsole(port);
        chat.accept();  //Wait for console data
    }
}
//End of ServerConsole class
