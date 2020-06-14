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
  public ClientConsole(String loginid,String host, int port) 
  {
    client= new ChatClient(loginid,host, port, this);
    try 
    {
      client.openConnection();
    }
    catch(IOException exception) 
    {
      System.out.println("Cannot open connection.  Awaiting command.");
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

        //hml #command
        char messagecheck = message.charAt(0);
        if (messagecheck == '#'){
          String[] cmessage = message.split(" ");
          String command = cmessage[0];
          System.out.println(command);
          if (command.equals("#quit")){
            System.out.println("The client has quit");
            client.quit();
          }
          else if (command.equals("#logoff")){
            client.closeConnection();
          }
          else if (command.equals("#sethost")){
            if (client.isConnected()){
              System.out.println("The client is still connected.");
            }
            else{
              String newhost = cmessage[1];
              client.setHost(newhost);
              System.out.println("Host set to " + newhost);
            }
          }
          else if (command.equals("#setport")){
            if (client.isConnected()){
              System.out.println("The client is still connected.");
            }
            else{
              int newport = Integer.parseInt(cmessage[1]);
              client.setPort(newport);
              System.out.println("Port set to " + newport);
            }
          }
          else if (command.equals("#login")){
            if (client.isConnected()){
              System.out.println("The client is still connected.");
            }
            else{
              client.openConnection();
              client.handleMessageFromClientUI("#login " + client.getloginid());
            }
          }
          else if (command.equals("#gethost")){
            System.out.println(client.getHost());
          }
          else if (command.equals("#getport")){
            System.out.println(client.getPort());
          }
          else{
            System.out.println("Wrong command.");
          }
        }
        else{
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
      id = "";
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
    catch(Throwable e)
    {
      port = DEFAULT_PORT;
    }

    ClientConsole chat= new ClientConsole(id, host, port);

    if(chat.client.isConnected()){
      if (chat.client.getloginid().equals("")){
        System.out.println("ERROR - No login ID specified.  Connection aborted.");
        chat.client.quit();
      }
      chat.client.handleMessageFromClientUI("#login "+chat.client.getloginid());
    }

    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
