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
                    } else {
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

        if (command.equals("quit")) {
            if (server.isListening()) {
                server.stopListening();
            }
            try {
                server.close();
            } catch (Exception ex) {
                System.out.println("Unexpected error while quitting server");
            }
        }

        else if (command.equals("stop")) {
            if (server.isListening()) {
                server.stopListening();
            } else {
                System.out.println("Server is not currently listening");
            }
        }

        else if (command.equals("close")) {
            try {
                server.close();
            } catch (Exception ex) {
                System.out.println("Unexpected error while closing server");
            }
        }

        else if (command.substring(0, 7).equals("setport")) {
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

        else if (command.equals("start")) {
            try {
                server.listen();
            } catch (Exception ex) {
                System.out.println("Unexpected error when starting server");
            }
        }

        else {
            System.out.println("Command unknown, please try again");
        }
    }

}