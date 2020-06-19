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
  public ClientConsole(String login, String host, int port) 
  {
    try 
    {
      client= new ChatClient(login, host, port, this);
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
    boolean logStatus = true;
    try
    {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;


      while (true) 
      {
        message = fromConsole.readLine();
        if(message.contains("#")){ 
          if (message.equals("#quit")) {
            System.exit(0);
          }else if(message.equals("#gethost")){
            System.out.println(client.getHost());
          }else if (message.equals("#getport")){
            System.out.println(client.getPort());
          }else if (message.equals("#logoff")){
            logStatus = false;
            client.handleMessageFromClientUI(client.getLogin()+" left the server.");
            System.out.println("Connection closed.");
          }else if (message.equals("#login")){
            if(logStatus==true){
              System.out.println("You are already logged in.");
            }
            logStatus = true;

          }else if (message.contains("#sethost")){
            if (logStatus==false) {
              client.setHost(message.split(" ")[1]);
            }else{
              System.out.println("Error, you can only set host when you are logged out.");
            }
          }else if (message.contains("#setport")){
            if (logStatus==false) {
              String temp =message.split(" ")[1];
              int temp1 = Integer.parseInt(temp);
              client.setPort(temp1);
            }else{
              System.out.println("Error, you can only set port when you are logged out.");
            }
          }else{
            System.out.println("Error, #'s are functions, this function does not exist.");
          } 
        }else{
          if(logStatus==true){
            client.handleMessageFromClientUI(message);
          }else{
            System.out.println("Currently, you are logged out, please log back in.");
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
    String login = "";
    try
    {
      host = args[1];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    try{
      port = Integer.parseInt(args[2]);
    }catch(ArrayIndexOutOfBoundsException e){
      port = DEFAULT_PORT;
    }
    if (args.length !=0){
      login = args[0];
      ClientConsole chat= new ClientConsole(login, host, port);
      chat.accept();  //Wait for console data

    }else{
      System.out.println("You didn't login, please login.");

    }
  }
}
//End of ConsoleChat class
