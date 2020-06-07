

import java.io.*;
import client.*;
import common.*;


public class ServerConsole implements ChatIF {



  //Initializes value to hold the server
  private EchoServer server;

  final public static int DEFAULT_PORT = 5555;

  public ServerConsole(EchoServer s) {

    server = s;

  }

  public ServerConsole(int port, EchoServer s) {
    
    server = new EchoServer(port);
  }

  public void accept()
  {

    try
    {
      BufferedReader fromConsole =
        new BufferedReader(new InputStreamReader(System.in));
        String message, command;
        int index;

        while (true) {

          message = fromConsole.readLine();


          try
          {
            index = message.indexOf(' ');
            command = message.substring(0, index);

            switch(command)
            {
              case "#setport":

              int newPort = Integer.parseInt(message.substring(index+1, message.length()));

                if(!server.isListening()) {
                  server.setPort(newPort);
                }

                else {
                  System.out.println("The server must close its connection"+
                  ", try command #stop and then attempt to set a new port");
                }
            }
          }

          catch (Exception e)
          {
            switch (message) {

              case "#quit":
                server.close();
                System.exit(0);
                break;

              case "#stop":
                if (server.isListening()) {
                  server.sendToAllClients("WARNING- Server has stopped listening"
                  +" for connections");
                  server.stopListening();
                }

                else {
                  System.out.println("The server has already stopped listening "+
                  "for connections");
                }
                break;

              case "#close":
                server.close();
                server.stopListening();
                break;

              case "#start":
                if (!server.isListening()) {
                  server.listen();
                }
                break;

              case "#getport":
                System.out.println(server.getPort());
                break;

              default:
                server.handleMessageFromServerUI(message);
                server.sendToAllClients(message);
                break;
            }
          }
        }
    }

    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  public void display(String message) {
    System.out.println("SERVER MSG> "+message);
  }

  public static void main(String[] args) {

    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    EchoServer sv = new EchoServer(port);
    ServerConsole sc = new ServerConsole(port, sv);

    try
    {
      sv.listen(); //Start listening for connections
      sc.accept();//Waits for input from the server console
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }


}
