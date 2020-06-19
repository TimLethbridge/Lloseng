import java.io.*;
import client.*;
import common.*;

public class ServerConsole implements ChatIF {

  private EchoServer server;
  final public static int DEFAULT_PORT = 5555;
  private boolean closed = false;

  public ServerConsole(int port) {
    server = new EchoServer(port, this);
  }

  public void display(String message) {
    server.sendToAllClients("SERVER MESSAGE> " + message);
  }

  public EchoServer get() {
    return server;
  }

  public void handleMessageFromConsole(String message) {
    String[] messageS = message.split(" ");
    if (message.charAt(0) == '#') {
      if (message.equals("#quit")) {
        try {
          server.close();
          System.exit(0);
        } catch (Exception ex) {
          System.out.println("Error");
        }
      } else if (message.equals("#stop")) {
        server.stopListening();
      } else if (message.equals("#close")) {
        try {
          server.close();
          closed = true;
        } catch (Exception ex) {
          System.out.println("Error");
        }
      } else if (messageS[0].equals("#setport")) {
        if (closed) {
          server.setPort(Integer.parseInt(messageS[1]));
        } else {
          System.out.println("Command not avalible, server is open.");
        }
      } else if (message.equals("#start")) {
        try {
          server.listen();
          closed = false;
        } catch (Exception ex) {
          System.out.println("Error");
        }
      } else if (message.equals("#getport")) {
        System.out.println(server.getPort());
      } else {
        System.out.println("Invalid Command");
      }
    } else {
      display(message);
    }
  }

  public void accept() {
    try {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) {
        message = fromConsole.readLine();
        handleMessageFromConsole(message);
      }
    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  public static void main(String[] args) {
    int port = 0; // Port to listen on

    try {
      port = Integer.parseInt(args[0]); // Get port from command line
    } catch (Throwable t) {
      port = DEFAULT_PORT; // Set port to 5555
    }

    ServerConsole server = new ServerConsole(port);

    try {
      server.get().listen(); // Start listening for connections
      server.accept();
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}