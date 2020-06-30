import java.io.*;
import common.*;

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
    EchoServer server;


    //Constructors ****************************************************

    /**
     * Constructs an instance of the ServerConsole UI.
     *
     * @param port The port to connect on.
     */
    public ServerConsole(int port) {
        server = new EchoServer(port);
        try {
            server.listen();
        } catch (IOException e) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }


    //Instance methods ************************************************

    /**
     * This method waits for input from the console.  Once it is
     * received, it sends it to all of the clients
     */
    public void accept() {
        try {
            BufferedReader fromConsole =
                    new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true) {
              message = fromConsole.readLine();
              server.handleMessageFromServerConsole(message);
              this.display(message);

            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console ");
        }
    }

    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message) {

        System.out.println(" Message du serveur > " + message);
    }


    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of the Client UI.
     *
     * @param args The port to use for connection
     */
    public static void main(String[] args) {
        int port = 0; //Port to listen on

        try {
            port = Integer.parseInt(args[0]); //Get port from command line
        } catch (Throwable t) {
            port = DEFAULT_PORT; //Set port to 5555
        }

        ServerConsole serv = new ServerConsole(port);
        serv.accept();
    }

}
//End of ConsoleChat class
