package simplechat1;

import java.io.*;
import simplechat1.client.*;
import simplechat1.common.*;


//some part copy from ClientConsole

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
    //ChatClient client;
    EchoServer server;

    public ServerConsole(int port) {
        //System.out.println("00");
        server = new EchoServer(port);
        //System.out.println(port);
        try {
            server.listen();
            //System.out.println("02");
        } catch (Exception e) {
            System.out.println("error: Can not listen form EchoServer");
            System.exit(0);
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
    public void display(String message) {
        System.out.println("SERVER MSG>" + message);
    }

    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of the Server UI.
     *
     * @param args[0] port
     */
    public static void main(String[] args) {
        int port = 0;
        try {
            port = Integer.valueOf(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            port = DEFAULT_PORT;
        }
        ServerConsole chat = new ServerConsole(port);
        chat.accept();  //Wait for console data

    }
}

