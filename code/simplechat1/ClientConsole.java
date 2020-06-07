// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF
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
  ChatClient client;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginId, String host, int port)
  {

    try
    {
      client= new ChatClient(loginId, host, port, this);
      client.sendToServer("#Login "+loginId);
    }
    catch(IOException exception)
    {
      display("Cannot open connection.  Awaiting "
      +"command.");
    }

  }


  //Instance methods ************************************************

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the client's message handler.
   * It also handles commands from the client
   */
  public void accept()
  {

    try
    {

      BufferedReader fromConsole =
        new BufferedReader(new InputStreamReader(System.in));
      String message;
      String command;
      int index;

      while (true)
      {
        message = fromConsole.readLine();
        //Processes commands from the user

        try
        {
          index = message.indexOf(' ');
          command = message.substring(0, index);
        }

        catch (Exception e)
        {
          index = 0;
          command = message;
        }

          switch (command)
          {

            case ("#sethost"):

              try
              {
                String newHost = message.substring(index+1, message.length());

                if (client.isConnected())
                {
                  display("The client must be disconnected before" +
                  " changing the host");
                }

                else
                {
                  client.setHost(newHost);
                  display("Host set to "+newHost);

                }
              }

              catch (Exception e) {}

              break;

            case "#setport":

              try
              {
                String newPort = message.substring(index+1, message.length());

                if (client.isConnected())
                {
                  display("The client must be disconnected before" +
                  " changing the port");
                }

                else
                {
                  client.setPort(Integer.parseInt(newPort));
                  display("Port set to "+newPort);
                }
              }

              catch(Exception e) {}

              break;

            case "#quit":
              display("The Client has been terminated");
              client.quit();
              break;

            case "#logoff" :

                try
                {
                  client.sendToServer(client.getID()+ " has been disconnected from the server");
                  client.closeConnection();
                }

                catch (IOException ex)
                {
                  display(client.getID()+" is not connected to the server");
                }
              break;

            case "#login":
              if (!client.isConnected())
              {
                try
                {
                  client.openConnection();
                  client.sendToServer("#login "+client.getID());
                }

                catch (Exception e) {display("Can't connect to the server");}
              }
              break;

            case "#gethost":
              display(client.getHost());
              break;

            case "#getport":
              int port = client.getPort();
              display(Integer.toString(port));
              break;

            default:
              client.handleMessageFromClientUI(message);
              break;
          }


      }
    }


    catch (Exception ex)
    {
      display("Unexpected error while reading from console!");
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
   * @param args[1] The port to connect to.
   */
  public static void main(String[] args)
  {

    ClientConsole chat = null;
    String host = "";
    int port = 0;  //The port number
    String loginId = "";

    try
    {
      loginId = args[0];
    }

    catch (ArrayIndexOutOfBoundsException e)
    {
      System.out.println("ERROR- No login ID specified.  Connection aborted");
      System.exit(0);
    }

    try
    {
      host = args[1];
    }
    catch(Exception e)
    {
      host = "localhost";

    }

    try
    {
      port = Integer.parseInt(args[2]);
      chat = new ClientConsole(loginId, host, port);
    }
    catch (Exception e)
    {
      chat = new ClientConsole(loginId, host, DEFAULT_PORT);
    }

    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
