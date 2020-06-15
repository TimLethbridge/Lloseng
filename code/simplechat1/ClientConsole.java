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
  public ClientConsole(String id, String host, int port) 
  {
    try 
    {
      client = new ChatClient(id, host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
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
        char first = message.charAt(0);
        char command = '#';
        if (first == command) {
          String msg = message.substring(1);
          String[] cmd = msg.split(" ");
          if (cmd[0].equals("quit")) {
            System.out.println("The client is quit.");
            client.quit();
          }else if (cmd[0].equals("logoff")) {
            System.out.println("The client is logoff.");
            client.closeConnection();
          }else if (cmd[0].equals("sethost")) {
            if (!client.isConnected()) {
              client.setHost(cmd[1]);
              System.out.println("The host set to: " + cmd[1]);
            } else {
              System.out.println("The client is still connected, so host cannot be set!");
            }
          }else if (cmd[0].equals("setport")) {
            if (!client.isConnected()) {
              client.setPort(Integer.parseInt(cmd[1]));
              System.out.println("The port set to: " + cmd[1]);
            } else {
              System.out.println("The client is still connected, so port cannot be set!");
            }
          }else if (cmd[0].equals("login")) {
            if (!client.isConnected()) {
              try {
                System.out.println(cmd[1] + " is login.");
                client.openConnection();
                client.handleMessageFromClientUI("#login " + client.getId());
              } catch (Exception e) {
                System.out.println("No id! Connection cannot be established.");
              }
            } else {
              System.out.println("It is already connected, so the client cannot login!");
            }
          }else if (cmd[0].equals("gethost")) {
            System.out.println("The host is: " + client.getHost());
          }else if (cmd[0].equals("getport")) {
            System.out.println("The port is: " + client.getPort());
          }
        }else {
          client.handleMessageFromClientUI(message);
        }
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
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number
    String id = "";

    try
    {
      id = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
      System.exit(0);
    }

    try
    {
      host = args[1];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }

    try
    {
      port = Integer.parseInt(args[2]);
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      port = DEFAULT_PORT;
    }

    ClientConsole chat = new ClientConsole(id, host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
