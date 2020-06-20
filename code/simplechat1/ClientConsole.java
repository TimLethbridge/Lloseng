/*
* This is the edited version of the ClientConsole class for the Assignment1. 
* All edits made specifically for the question number will be added as a comment
*/

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
   * new edit #1 E6 a)
   * @param loginID The client login ID
   */
  public ClientConsole(String loginID, String host, int port) 
  {
    try 
    {
      client= new ChatClient(loginID, host, port, this);
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
  public void accept(){
    boolean loginStatus = true;
    try{
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;
      while (true) {
        message = fromConsole.readLine();
        //new edit #2 E6 a)
        if(message.contains("#")){
          if (message.equals("#quit")){
            System.exit(0);
          }else if(message.equals("#gethost")){
            System.out.println(client.getHost());
          }else if (message.equals("#getport")){
            System.out.println(client.getPort());
          }else if (message.equals("#logoff")){
            loginStatus = false;
            client.handleMessageFromClientUI(client.getLogin()+" has logged off");
            System.out.println("the connection has closed.");
          }else if (message.equals("#login")){
            if(loginStatus==true){
              System.out.println("You are logged in");
            }
            loginStatus = true;
          }else if (message.contains("#sethost")){
            if (loginStatus==false){
              client.setHost(message.split(" ")[1]);
            }else{
              System.out.println("Cannot set host while logged in.");
            }
          }else if (message.contains("#setport")){
            if (loginStatus==false) {
              String tmp =message.split(" ")[1];
              int tmp1 = Integer.parseInt(tmp);
              client.setPort(tmp1);
            }else{
              System.out.println("Cannot set port while logged in.");
            }
          }else{
            System.out.println("This function does not exist.");
          }
        }else{
          if(loginStatus==true){
            client.handleMessageFromClientUI(message);
          }else{
            System.out.println("You are logged out, please log in.");
          }
        }
      }
    }catch (Exception ex){
      System.out.println ("Unexpected error while reading from console!");
    }
  }
  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message){
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   * new edit#3 E6a)
   */
  public static void main(String[] args){
    String host = "";
    String loginID="";
    int port = 0;  //The port number
    try{
      host = args[1];
    }catch(ArrayIndexOutOfBoundsException e){
      host = "localhost";
    }
    try{
      port = Integer.parseInt(args[2]);
    }catch(ArrayIndexOutOfBoundsException e){
      //new edit #2 E5b)
      port = DEFAULT_PORT; 
    }
    //new edit #3 E5b)
    if (args.length !=0){
      loginID = args[0];
      ClientConsole chat=new ClientConsole(loginID,host,port);
      chat.accept();  //Wait for console data
    }else{
      System.out.println("You didn't login, please login.");
    }
  }
}