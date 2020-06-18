import java.io.*;
import ocsf.server.*;
import client.*;
import common.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Laith Grira
 */

public class ServerConsole implements ChatIF {

    //Class variables *************************************************

    /**
     * The default port to connect on.
     */
    final public static int DEFAULT_PORT = 5555;

    //Instance variables **********************************************

    /**
     * The instance of the client that created this ConsoleChat.
     */
    ChatClient server;
    public static String host;
    public static int port;  //The port number

    //Constructors ****************************************************

    /**
     * Constructs an instance of the ClientConsole UI.
     *
     * @param host The host to connect to.
     * @param port The port to connect on.
     */
    public ServerConsole(String serverID, String host, int port)
    {
        try
        {
            server= new ChatClient(serverID, host, port, this);
        }
        catch(IOException exception)
        {
            connectionClosed();
            System.out.println("Error: Can't setup connection!"
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
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while (true)
            {
                message = fromConsole.readLine();

                switch (message){
                    case "#quit":
                        System.out.println("Server is Quitting...");
                        server.handleMessageFromClientUI("Quit");
                        break;
                    case "#close":
                        System.out.println("Server is Quitting...");
                        server.handleMessageFromClientUI("Close");
                        break;
                    case "#stop":
                        System.out.println("Server will stop listening for new clients...");
                        server.handleMessageFromClientUI("Stop");
                        break;
                    case "#start":
                        System.out.println("Server is listening for new clients...");
                        server.handleMessageFromClientUI("Start");
                        break;
                    case "#setPort": //setting the Port number
                        System.out.println("Enter the new Port in the server cmd: ");
                        server.handleMessageFromClientUI("#setPort");
                        break;
                    case "#getPort": //get port
                        server.handleMessageFromClientUI("#getPort");
                        break;
                    default:
                        server.handleMessageFromClientUI("SERVER MSG>"+message);
                        break;
                }
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
     * This method is responsible for the creation of the Client UI.
     *
     * @param args[0] The host to connect to.
     */
    public static void main(String[] args)
    {
        String serverID = "TheServer";
        String host = "";
        int port = 0;

        try
        {
            host = args[0];
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            host = "localhost";
        }
        System.out.println("Enter the Port Number: ");

        //now we have to allow the client to chose the port
        try
        {
            BufferedReader portFromConsole = new BufferedReader(new InputStreamReader(System.in));
            String portString;
            portString = portFromConsole.readLine();
            port = Integer.parseInt(portString);
        }
        catch (Exception numException)
        {
            // if we find an error, we will take the default port: 5555
            port = DEFAULT_PORT;
            System.out.println("Number is invalid, using default...");
        }


        ServerConsole chatServer = new ServerConsole(serverID, host, port);
        chatServer.accept();  //Wait for console data
    }



    protected void connectionClosed(){
        if ( server.isConnected() == false) {
            System.out.println("The serever has been disconnected...");
            System.exit(1);
        }
    }

    protected void connectionException(java.lang.Exception exception){
        System.out.println(exception.toString());
    }



}