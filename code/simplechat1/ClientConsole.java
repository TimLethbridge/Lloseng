// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version June 2020, edited by Morris Cai from Version. July 2000
 * 
 * 
 */
public class ClientConsole implements ChatIF {
  // Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  // Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  private boolean run = true;

  private String internalLoginID;
  private String internalHost;
  private int internalPort;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginID, String host, int port) {
    internalLoginID = loginID;
    internalHost = host;
    internalPort = port;
    try {
      client = new ChatClient(loginID, host, port, this);
      System.out.println(loginID + " has logged on.");
    } catch (IOException exception) {
      System.out.println("Error: Can't open connection.  Awaiting Command");
    }
  }

  // Instance methods ************************************************

  /**
   * This method waits for input from the console. Once it is received, it sends
   * it to the client's message handler.
   */
  public void accept() {
    try {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;

      // loops infinitely, until #quit command is called, which sets run = false, and
      // stops the loop
      while (run) {
        message = fromConsole.readLine();
        if (message != null) {
          if (message.charAt(0) == '#') {
            command(message.substring(1));
          } else {
            client.handleMessageFromClientUI(message);
          }
        } else {
          client.handleMessageFromClientUI(message);
        }
      }
    }

    // Exceptions where the reader encounters an error
    catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
      System.out.println(ex.getMessage());
    }
  }

  /**
   * Method which takes commands and executes them
   * 
   * Commands usually take the form of "#COMMAND", but should be fed into this
   * method without the #.
   * 
   * @param command The command that should be executed, without "#"
   */
  public void command(String command) {

    // Determines if client is connected
    boolean isConnected = true;
    try {
      isConnected = client.isConnected();
    } catch (Exception notCnnected) {
      isConnected = false;
    }

    // Switch/case/default statement to determine command
    switch (command) {

      case ("quit"):
        if (isConnected) {
          try {
            client.closeConnection();
            run = false;
          } catch (Exception ex) {
            System.out.println("Unexpected error while terminating connection");
          }
        }
        break;

      case ("logoff"):
        if (!isConnected) {
          System.out.println("Client already logged off");
        }

        else {
          try {
            client.closeConnection();
          } catch (Exception ex) {
            System.out.println("Unexpected error while terminating connection");
          }
        }
        break;

      case ("login"):
        if (isConnected) {
          System.out.println("Client already logged in");
        } else {
          ClientConsole chat = new ClientConsole(internalLoginID, internalHost, internalPort);
          chat.accept();
        }
        break;

      case ("getport"):
        System.out.println(internalPort);
        break;

      case ("gethost"):
        System.out.println(internalHost);
        break;

      default:

        if (command.length() > 8 && command.substring(0, 7).equals("sethost")) {
          // Checks to see if client is connected, if so, disallow host changed.
          // This is in a try/catch statement to allow host change before connection is
          // established

          if (isConnected) {
            System.out.println("Client currently logged in. Please log off before changing host.");
          } else {
            internalHost = command.substring(8);
            System.out.println("Host set to: " + internalHost);
          }
        }

        else if (command.length() > 8 && (command.substring(0, 7)).equals("setport")) {

          // Iniitalize changeport as DEFAULT_PORT, incase something goes wrong.
          int changePort = DEFAULT_PORT;

          // Confirms that port change is valid.
          try {
            changePort = Integer.parseInt(command.substring(8));

            // Checks to see if client is connected, if so, disallow port changed.
            // This is in a try/catch statement to allow port change before connection is
            // established
            if (isConnected) {
              System.out.println("Client currently logged in. Please log off before changing port.");
            } else {
              internalPort = changePort;
              System.out.println("Port set to: " + internalPort);
            }
          }

          catch (Exception e) {
            System.out.println("Invalid port number");
          }

        }

        else {
          System.out.println("Command unknown, please try again");
        }
    }

  }

  /*
   * private void setLoginID(String identification) { loginID = identification; }
   * 
   * private String getLoginID() { return loginID; }
   */

  /**
   * This method overrides the method in the ChatIF interface. It displays a
   * message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) {
    System.out.println(message);
  }

  public void connectionException(Exception exception) {
    System.out.println("Abnormal termination of connection.");
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) {

    String host = "";
    int port = DEFAULT_PORT; // The port number

    try {
      host = args[1];
      port = Integer.parseInt(args[2]);
    } catch (Exception ex) {
    }

    // Getting the login id

    // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    // String identification = new String();

    try {
      String clientID = args[0];
      ClientConsole chat = new ClientConsole(clientID, host, port);
      chat.accept();
    } catch (Exception Ex) {
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
    }

    /*
     * System.out.
     * println("Please specify port number (If blank, default port 5555 will be used)"
     * ); try { String input = reader.readLine(); port = Integer.parseInt(input); }
     * 
     * catch (Exception ex) {
     * System.out.println("Invalid port number. Default port of 5555 will be used");
     * }
     * 
     * if (port > 65535 || port < 0) { System.out.println("Invalid Port Number"); }
     * else { System.out.println("Connection to server at port " + port); try { host
     * = args[0]; } catch (ArrayIndexOutOfBoundsException e) { host = "localhost"; }
     * 
     * ClientConsole chat = new ClientConsole(identification, host, port);
     * chat.accept(); // Wait for console data
     */
  }
}

// End of ConsoleChat class
