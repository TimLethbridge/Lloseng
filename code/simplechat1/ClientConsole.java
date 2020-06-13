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
 * @version July 2000
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

  private String loginID;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginID, String host, int port) {
    try {
      client = new ChatClient(loginID, host, port, this);
    } catch (IOException exception) {
      System.out.println("Error: Can't setup connection!" + " Terminating client.");
      System.exit(1);
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
    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  public void command(String command) {

    System.out.println("Command: " + command);
    System.out.println("Status: " + client.isConnected());

    if (command.equals("quit")) {
      if (client.isConnected()) {
        try {
          client.closeConnection();
          run = false;
        } catch (Exception ex) {
          System.out.println("Unexpected error while terminating connection");
        }
      }

    } else if (command.equals("logoff")) {

      if (!client.isConnected()) {
        System.out.println("Client already logged off");
      }

      else {
        System.out.println("Attempting to logoff");
        try {
          client.closeConnection();
        } catch (Exception ex) {
          System.out.println("Unexpected error while terminating connection");
        }
      }
    }

    else if (command.equals("login")) {
      if (client.isConnected()) {
        System.out.println("Client already logged in");
      } else {
        ClientConsole chat = new ClientConsole(client.getLoginID(), client.getHost(), client.getPort());
        chat.accept();
      }
    }

    else if (command.substring(0, 7).equals("sethost")) {
      if (client.isConnected()) {
        System.out.println("Client currently logged in. Please log off before changing host.");
      } else {
        client.setHost(command.substring(8));
        System.out.println("SetHost" + client.getHost());
      }
    }

    else if ((command.substring(0, 7)).equals("setport")) {
      System.out.println("Attempting to changeport");
      if (client.isConnected()) {
        System.out.println("Client currently logged in. Please log off before changing port.");
      } else {
        try {
          int changePort = Integer.parseInt(command.substring(8));
          client.setPort(changePort);
          System.out.println("SetPort" + client.getPort());
        } catch (Exception e) {
          System.out.println("Invalid port number");

        }
      }
    }

    else if (command.substring(0, 7).equals("gethost")) {
      System.out.println(client.getHost());
    }

    else if (command.substring(0, 7).equals("getport")) {
      System.out.println(client.getPort());
    }

    else {
      System.out.println("Command unknown, please try again");
    }

  }
  /*
  private void setLoginID(String identification) {
    loginID = identification;
  }

  private String getLoginID() {
    return loginID;
  }
  */

  /**
   * This method overrides the method in the ChatIF interface. It displays a
   * message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) {
    System.out.println("> " + message);
  }

  public void connectionException(Exception exception) {
    System.out.println("Server has shut down. Terminating session");
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args){
    
    String host = "";
    int port=DEFAULT_PORT;  //The port number

    //Getting the login id
  
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String identification = new String();

    while(true){
      System.out.println("Please enter your login ID");
      try{
        identification = reader.readLine();
      
      }
      catch (Exception ex){
        System.out.println("Error getting login ID.");
      }

      if(identification != null){
        break;
      }
    }

    System.out.println("Please specify port number (If blank, default port 5555 will be used)");
    try{
      String input = reader.readLine();
      port = Integer.parseInt( input );
    }

    catch (Exception ex){
      System.out.println
        ("Invalid port number. Default port of 5555 will be used");
    }
  
    if(port > 65535 || port < 0){
      System.out.println("Invalid Port Number");
    }
    else{
      System.out.println("Connection to server at port "+port);
      try
      {
        host = args[0];
      }
      catch(ArrayIndexOutOfBoundsException e)
      {
        host = "localhost";
      }
      ClientConsole chat= new ClientConsole(identification, host, port);
      chat.accept();  //Wait for console data
    }
  }
}
// End of ConsoleChat class
