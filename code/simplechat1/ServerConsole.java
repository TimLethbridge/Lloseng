// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
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
     * @param host The host to connect to.
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
            System.out.println("Couldn't listen for clients!\n Terminating server.");
            System.exit(1);
        }
    }

    //Instance methods ************************************************
    /**
     * This method waits for input from the console.  Once it is
     * received, it sends it to the clients.
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
            System.out.println("Unexpected error while reading user input in the server console!");
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

        if (message.startsWith("#")){
            return;
        }
        System.out.println("SERVER MSG> " + message);
    }
    //Class methods ***************************************************

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
        ServerConsole sv = new ServerConsole(port);

        try
        {
            sv.server.listen(); //Start listening for connections
        }
        catch (Exception ex)
        {
            System.out.println("Could not initiate listen for clients!");
        }
        sv.accept();
    }
}
//End of ServerConsole class