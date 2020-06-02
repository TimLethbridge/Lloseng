import java.io.*;
import client.*;
import common.*;

public class ServerConsole implements ChatIF {

    EchoServer server;

    public ServerConsole(int port) 
    {
      try 
      {
        server = new EchoServer(port);
      } 
      catch(Exception exception) 
      {
        System.out.println("Could not create server.");
        System.exit(1);
      }
    }

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
          display(message);
        }
      } 
      catch (Exception ex) 
      {
        System.out.println("Unexpected error while reading from console!");
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
      String[] msg = message.split(" ");
      switch(msg[0]) {
        case "#quit":
          try {
            System.out.println("Quitting server..");
            server.close();
            System.exit(0);
          } catch (Exception ex) {}
          break;
        case "#stop":
          server.stopListening();
          server.sendToAllClients("WARNING - Server has stopped listening for connections.");
          break;
        case "#close":
          try {
            System.out.println("Server closed!");
            server.stopListening();
            server.close();
          } catch (Exception ex) {}
          break;
        case "#setport":
          if (server.isListening() == false) {
            try {
              server.setPort(Integer.parseInt(msg[1]));
              System.out.println("Port set to " + msg[1]);
            } catch (NumberFormatException e) {
              System.out.println("Could not set port.");
            }
          } else {
            System.out.println("Server must be closed in order to change port.");
          }
          break;
        case "#start":
          if (server.isListening() == false) {
            try {
              server.listen();
            } catch (Exception ex) {
              System.out.println("ERROR - Could not listen for clients!");
            }
          } else {
            System.out.println("Server is currently listening.");
          }
          break;
        case "#getport":
          System.out.println(server.getPort());
          break;
        default: 
          System.out.println("SERVER MSG> " + message);
          server.sendToAllClients("SERVER MSG> " + message);
          break;
      }
    }

    public static void main(String[] args) 
    {
      int port = 0;  //The port number
  
      try
      {
        port = Integer.parseInt(args[1]);
      }
      catch(Throwable t)
      {
        port = EchoServer.DEFAULT_PORT;
      }
  
      ServerConsole sv = new ServerConsole(port);

      try 
      {
        sv.server.listen(); //Start listening for connections
      } 
      catch (Exception ex) 
      {
        System.out.println("ERROR - Could not listen for clients!");
      }

      sv.accept();  //Wait for console data
    }
}