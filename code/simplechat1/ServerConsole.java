import java.io.*;
import common.*;

/**
 * This class (ServerConsole) implements ChatIF and adds a console to Echoserver
 * 
 */


/**
 * This class constructs the UI for a console client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 *
 * @author Morris Cai : 300067686
 * @version June 2020
 */
public class ServerConsole implements ChatIF {
    // Instance variables
    private boolean run = true;
    private EchoServer server;

    /**
     * Constructs an instance of the ServerConsole UI
     * 
     * @param sv The server that the console is acting upon
     */
    public ServerConsole(EchoServer sv) {
        server = sv;
    }

    /**
     *  Displays a message on the console with a ">>" prefix
     * 
     * @param message The message to be displayed
     */
    public void display(String message) {
        System.out.println(">> " + message);
    }

    /**
     * Starts the console and starts listening for connections and commands
     */
    public void startConsole() {
        try {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (run) {
                message = fromConsole.readLine();
                if (message != null) {
                    if (message.charAt(0) == '#') {
                        command(message.substring(1));
                    }

                    else {
                        display(message);
                        server.sendToAllClients("SERVER MSG> " + message);
                    }
                } else {
                    display(message);
                    server.sendToAllClients("SERVER MSG> " + message);
                }
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }


    /**
     * Method which takes commands and executes them
     * 
     * Commands usually take the form of "#COMMAND", but should be fed into this method
     * without the #.  
     * 
     * @param command The command that should be executed, without "#"
     */
    public void command(String command) {

        //As switch/case cases do not account for substrings, the substring method of setport ____ must be handled separately
        if (command.length() > 7 && command.substring(0, 7).equals("setport")) {
            if (server.isListening()) {
                System.out.println("Server currently listening. Please stop listening before changing port");
            } else {
                try {
                    int changePort = Integer.parseInt(command.substring(8));
                    server.setPort(changePort);
                } catch (Exception e) {
                    System.out.println("Invalid port number");

                }
            }
        }

        else {
            switch (command) {

                //#quit closes all connections, then quits gracefully
                case ("quit"):
                    run = false;
                    if (server.isListening()) {
                        server.stopListening();
                    }
                    try {
                        server.sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING");
                        server.close();

                    } catch (Exception ex) {
                        System.out.println("Unexpected error while quitting server");
                    }
                    break;
                
                //#stop stops listening for new connections, but existing connections stay open
                case ("stop"):
                    if (server.isListening()) {
                        server.stopListening();
                        server.sendToAllClients("WARNING - Server has stopped listening for connections.");
                    } else {
                        System.out.println("Server is not currently listening");
                    }
                    break;
                
                //#close disconnects all connected clients and stop listening, but doesn't quit, allow for #start to restart the server
                case ("close"):
                    try {
                        server.sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING");
                        server.close();
                    } catch (Exception ex) {
                        System.out.println("Unexpected error while closing server");
                    }
                    break;

                //#start restarts the server and starts listening
                case ("start"):
                    try {
                        server.listen();
                    } catch (Exception ex) {
                        System.out.println("Unexpected error when starting server");
                    }
                    break;

                //If command is not found, print out this error
                default:
                    System.out.println("Command unknown, please try again");
            }
        }
    }


    /**
   * This method is responsible for the creation of the ServerConsoleUI
   *
   * @param args[0] The port to connect to.
   */
    public static void main(String[] args) {
        int port = 5555; // Port to listen on

        try {
            port = Integer.parseInt(args[0]); // Get port from command line
        } catch (Exception ex) {
            
        }
        EchoServer sv = new EchoServer(port);

        try {
            sv.listen(); // Start listening for connections
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }

        // Initiates and starts a serverConsole to allow for server communications.
        ServerConsole console = new ServerConsole(sv);
        console.startConsole();
    }
}
