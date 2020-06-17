import java.io.*;
import common.*;

/**
 * This class (ServerConsole) implements ChatIF and adds a console to Echoserver
 * 
 */

public class ServerConsole implements ChatIF {
    // Instance variables
    private boolean run = true;
    private EchoServer server;

    // Constructor
    public ServerConsole(EchoServer sv) {
        server = sv;
    }

    // Displays a message with a ">>" prefix
    public void display(String message) {
        System.out.println(">> " + message);
    }

    //
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

    public void command(String command) {

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

                case ("stop"):
                    if (server.isListening()) {
                        server.stopListening();
                        server.sendToAllClients("WARNING - Server has stopped listening for connections.");
                    } else {
                        System.out.println("Server is not currently listening");
                    }
                    break;

                case ("close"):
                    try {
                        server.sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING");
                        server.close();
                    } catch (Exception ex) {
                        System.out.println("Unexpected error while closing server");
                    }
                    break;

                case ("start"):
                    try {
                        server.listen();
                    } catch (Exception ex) {
                        System.out.println("Unexpected error when starting server");
                    }
                    break;

                default:
                    System.out.println("Command unknown, please try again");
            }

        }
    }

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
