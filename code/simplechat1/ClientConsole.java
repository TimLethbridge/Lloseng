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
  public ClientConsole(String host, int port, String userId)
  {
  
      client= new ChatClient(host, port,this, userId);
        
  
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
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;
      while (true) 
      {
        message = fromConsole.readLine();
          String[] cmd = message.split(" ");
          if(message.charAt(0)==('#')){
              if(message.equals("#quit")){
                  System.out.println("client quit");
                  client.quit();
              }
              else if((cmd[0].equals("#setport"))||(cmd[0].equals("#sethost"))){
                  if (client.isConnected()){
                      System.out.println("The client is already connected.");
                             }
                  else{
                      if(cmd[0].equals("#sethost")){
                          client.setHost(cmd[1]);
                          System.out.println("Host set to :"+cmd[1]);
                      }
                      else{
                          System.out.println("Port set to :"+cmd[1]);
                          client.setPort(Integer.parseInt(cmd[1]));
                      }
                    }
              }
              else if(message.equals("#logoff")){
                  client.closeConnection();
              }
              else if(message.equals("#login")){
                  if (client.isConnected()){
                     System.out.println("The client is already connected.");
                }
                  else{
                      client.openConnection();
                      client.handleMessageFromClientUI( client.getuserId()+" has logged on");
                  }
              }
              else if(message.equals("#getport")){
                  System.out.println(client.getPort());
              }
              else if(message.equals("#gethost")){
                  System.out.println(client.getHost());
              }
              else{
                  System.out.println("invaild command");
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
      System.out.println(client.getuserId()+"> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   *                       arg[1] port
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number
    String userId = "";
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
      try
        {
            userId = args[0];
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            userId = "";
        }
    ClientConsole chat= new ClientConsole(host, port,userId);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
