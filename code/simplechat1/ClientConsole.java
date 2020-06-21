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
  public static String clientName;
  public static String clientNameString;
  public static boolean correctID;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginid, String host, int port)
  {
    try
    {
      client= new ChatClient(loginid, host, port, this);
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
      if (!correctID){
          System.out.println("You did not enter your id correctly!");
          client.handleMessageFromClientUI(": #login is not detected, Terminating client");
          System.exit(0);

      }
    try
    {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;
      String loginValid = "";
      String setPortHostValid ="";

      client.handleMessageFromClientUI(getClientId());
      client.handleMessageFromClientUI(" "+getClientId()+" has logged on.");

      while (true)
      {
        message = fromConsole.readLine();
        if (message.length()>= 6) { //for login verification
          loginValid = message.substring(0, 6);
        }
        if(message.length()>=8) {//verifie if the user has used #setPort XXXX and #setHost
            setPortHostValid = message.substring(0,8);
            switch (setPortHostValid){
                case "#setHost":
                    System.out.println("Error, you have to log out first!");
                    break;
                case "#setPort":
                    System.out.println("Error, you have to log out first!");
                    break;
                default:
                    setPortHostValid = setPortHostValid;
            }
        }

        switch (message){
          case "#quit":
            System.out.println("Quit...");
            client.quit();
            break;
          case "#logoff":
              System.out.println("You have logged off, if you want to login again, Please choose your new Host and Port, or press enter to chose the default: ");
            client.handleMessageFromClientUI("Client has logged off");
            client.setHost(setHost());
            client.setPort(setPort());
            ClientConsole chat= new ClientConsole(getClientId(), client.getHost(), client.getPort());
            chat.accept();  //Wait for console data

            break;

          case "#getHost": //get host
            System.out.println("The host is: " + client.getHost());
            break;
          case "#getPort": //get port
            System.out.println("The port number is: "+client.getPort());
            break;
          default:
              client.handleMessageFromClientUI(message + " from <" + getClientName() + ">");
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
    String logingID = "";
    String host = "";
    String firstCommand = "";
    int port = 0;
    correctID = true; //to verifie that the #login command inserted

      /**
       ************************************************************************
       *************            Set the client ID          ********************
       ************************************************************************
       **/

      System.out.println("Please Login and enter your ID: #login <...id...>");
      try {
        BufferedReader idFromConsole = new BufferedReader(new InputStreamReader(System.in));
        logingID = idFromConsole.readLine();
        clientName = logingID;
        if (logingID.length()>9){
            clientNameString = logingID.substring(8,logingID.length()-1); // to get the name without < >
        }else {
            clientNameString = "Unknown"; //if client doesn't insert his name
        }

        //we need to make sure that this is the first commend made by the client, otherwise terminate him
        firstCommand = logingID.substring(0, 6);

      }
      catch (Exception ex)
      {
          System.out.println("Unexpected error while reading from console!");
      }
      if (!logingID.equals("") && !firstCommand.equals("#login")){
          correctID = false;
      }
        // now we set up the host and port
      host = setHost();
      port = setPort();

    ClientConsole chat= new ClientConsole(logingID, host, port);
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


  /**
  * Getters for the client name and id
  * */
  public static String getClientId(){
    return clientName;
  }
  public static String getClientName(){
      return clientNameString;
  }

    /**
     ************************************************************************
     *************            Set the Host          *************************
     ************************************************************************
     **/
  public static String setHost(){
      String hostToReturn= "";
      System.out.println("Set the Host: #setHost <.....>");
      try {
          BufferedReader hostFromConsole = new BufferedReader(new InputStreamReader(System.in));
          String hostString = hostFromConsole.readLine();
          String setHostValid = hostString.substring(0,8);
          if (setHostValid.equals("#setHost")){
              hostToReturn = hostString.substring(10, hostString.length() - 1);
              System.out.println("Your Host is: " + hostToReturn);
              System.out.println(" ");
          }else{
              hostToReturn = "localhost";
              System.out.println("Name is invalid, using default...");
              System.out.println(" ");
          }
      } catch (Exception numException) {
          hostToReturn = "localhost";
          System.out.println("Error occured, using default...");
          System.out.println(" ");
      }
      return hostToReturn;
  }

    /**
     ************************************************************************
     *************            Set the Port          *************************
     ************************************************************************
     **/
    public static int setPort() {
        int portToReturn= 0;
        System.out.println("Set the Port: #setPort <XXXX> ");
        try {
            BufferedReader portFromConsole = new BufferedReader(new InputStreamReader(System.in));
            String portString = portFromConsole.readLine();
            String setPortValid = portString.substring(0, 8);
            if (setPortValid.equals("#setPort")) {
                String newPortNumber = portString.substring(10, portString.length() - 1);
                portToReturn = Integer.parseInt(newPortNumber);
                System.out.println("Your Port is: " + portToReturn);
                System.out.println(" ");
            } else {
                portToReturn = DEFAULT_PORT;
                System.out.println("Number is invalid, using default...");
                System.out.println(" ");
            }
        } catch (Exception numException) {
            // if we find an error, we will take the default port: 5555
            portToReturn = DEFAULT_PORT;
            System.out.println("Error occured, using default...");
            System.out.println(" ");
        }
        return portToReturn;
    }

}
//End of ConsoleChat class
