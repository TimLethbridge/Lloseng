// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package client;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.util.Scanner;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  private String user; //username


  //Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String user, String host, int port, ChatIF clientUI)
    throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.user = user;
    this.clientUI = clientUI;
    this.openConnection();
    this.sendToServer("#login " + user ); //Automatic ID identification
}


public String getUser() { //getter for username
  return user;
}

public void connectionException(Exception exception) {
     System.out.println("WARNING - The server has stopped listening for connections");
 }



  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message)
  {

    if(message.startsWith("#"))  { //# is for command line

      String nHost;
      int nPort;

      Scanner sc = new Scanner(System.in);


      if(message.equals("#quit")) {
        System.out.print("Shut down");
        quit();
      }

      if(message.equals("#login")) {
        if(!isConnected()) {

          try
          {

            this.openConnection();
            System.out.println("log in...");


          }
          catch (IOException e) {}

        } else {
          System.out.println(" You already connected ! ");
        }

      }

      if(message.equals("#logoff")) {
          try
          {
            closeConnection();
            System.out.println("login off...");

          }
          catch(IOException e) {
            System.out.println("Error");
          }


        }


      if(message.equals("#gethost")) {
        System.out.println("The current host is " + this.getHost());

      }

      if(message.equals("#getport")) {
        System.out.println("The current port is " + this.getPort());
      }

      if(message.equals("#sethost")) {
        if(!this.isConnected()) {
          System.out.println("Please entrer a host name");
          nHost = sc.nextLine();
          this.setHost(nHost);
          System.out.println("The new host is " + nHost);

        } else {

          System.out.println("Sorry, but to setup the host, you must be logged off");

        }

      }

      if(message.equals("#setport")) {
        if(!this.isConnected()) {
          System.out.println("Please entrer a port");
          nPort = sc.nextInt();
          this.setPort(nPort);
          System.out.println("The new port is " + nPort);

        } else {

            System.out.println("Sorry, but to setup the port, you must be logged off");

        }

      }

    } else { //if the message isn't a command line

      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display("Could not send message to server.  Terminating client.");
        quit();
      }

    }

  }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
