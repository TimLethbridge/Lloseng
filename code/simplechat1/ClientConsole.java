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
  public ClientConsole(String host, int port)
  {
    try
    {
      client= new ChatClient(host, port, this);
    }
    catch(IOException exception)
    {
      connectionClosed();
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
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true)
      {
        message = fromConsole.readLine();

        switch (message){
          case "#quit":
            System.out.println("Quit...");
            connectionClosed();
            System.exit(1);
            break;
          case "#logoff":
            //code
            break;
          case "#setPort": //setting the Port number *************************************************
            System.out.println("Enter the new Port: ");
            int num;
            String numberString;
            try
            {
              BufferedReader numberFromConsole = new BufferedReader(new InputStreamReader(System.in));
              numberString = numberFromConsole.readLine();
              num = Integer.parseInt(numberString);
            }
            catch (Exception numException)
            {
              num = DEFAULT_PORT;
              System.out.println("Number is invalid, using default...");
            }
            client.setPort(num);
            break;
          case "#setHost": //setting the Host ********************************************************
            System.out.println("Enter the new Host: ");
            String messageString;
            try
            {
              BufferedReader messageFromConsole = new BufferedReader(new InputStreamReader(System.in));
              messageString = messageFromConsole.readLine();
              client.setHost(messageString);
            }
            catch (Exception numException)
            {
              System.out.println("Host name is invalid, using default...");
            }
            break;
          case "#getHost": //get host ****************************************************************
            System.out.println("The host is: " + client.getHost());
            break;
          case "#getPort": //get port ****************************************************************
            System.out.println("The port number is: "+client.getPort());
            break;
          default:
            client.handleMessageFromClientUI(message);
            break;
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
    String host;
    host = "";
    int port ;
    port = 0;
    try
    {
      host = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    System.out.println("Enter the Port Number: ");

    //now we have to allow the client to chose the port
    try
    {
    BufferedReader portFromConsole = new BufferedReader(new InputStreamReader(System.in));
    String portString;
    portString = portFromConsole.readLine();
      port = Integer.parseInt(portString);
    }
    catch (Exception numException)
    {
      // if we find an error, we will take the default port: 5555
      port = DEFAULT_PORT;
      System.out.println("Number is invalid, using default...");
    }


    ClientConsole chat= new ClientConsole(host, port);
    chat.accept();  //Wait for console data

  }



  protected void connectionClosed(){
    if ( client.isConnected() == false) {
      System.out.println("The serever has been disconnected...");
      System.exit(1);
    }
  }

  protected void connectionException(java.lang.Exception exception){
    System.out.println(exception.toString());
  }




}
//End of ConsoleChat class
