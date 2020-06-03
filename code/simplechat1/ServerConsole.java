

/**
 * This class constructs the UI for a chat server.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Zahra Manochehri
 * @version May 2020
 */


import java.io.*;
import client.*;
import common.*;


public class ServerConsole implements ChatIF{

	// Class variables:
	/**
    * The default port to connect on.
    */

  final public static int DEFAULT_PORT = 5555;

  private EchoServer server;


  	/**
  	*Constructor
  	**/
  public ServerConsole(int port){

  	try{
  		server = new EchoServer(port);

  	}
  	catch(Exception exception){
  		System.out.println("Cannot establish a server. Please try again.");
  		System.exit(1);

  	}
  	
  }
  /**
  *This method handles commands form Server Concole.
  *
  **/

  private void handleCommandFromServer(String message){
  	 try{

      switch(message){

        case "#quit": //Causes the server to quit gracefully.

          System.out.println("You are quiting.");
          server.close();
          System.exit(0);
          
          break;

        case "#stop": //Causes the server to stop listening for new clients. 

          try{
            server.sendToAllClientsFromServer("WARNING - Server has stopped listening for connections.");
            server.stopListening();
            
          }
          catch(Exception e) {
            System.out.println("Something went wrong in #stop command");
          }
          break;

        case "#close": //Causes the server not only to stop listening for new clients, but also to disconnect all existing clients.

          System.out.println("You are closing.");
          server.sendToAllClientsFromServer("SERVER SHUTTING DOWN! DISCONNECTING!");

          server.close();
          
          break;

        case "#setport <port>": //Calls the setPort method in the server. Only allowed if the server is closed.

          if(server.isListening()== false){
            System.out.println("Setting port to 5555.");
            server.setPort(5555);
          }else{
            System.out.println("Setting port error : You need to close all existing clients first.");
          }

          break;

        case "#start": //Causes the server to start listening for new clients. Only valid if the server is stopped.
          if(server.isListening()== false){
            System.out.println("You are going online for listening to a new client.");
            server.listen();
          }else{

            System.out.println("Start error : You are listening to possible clients.");

          }

          break;

        case "#getport": // Display the current port number

          System.out.println(server.getPort());
          break;

        default :
          System.out.println("Not a correct command, try again.");


      }

    }
    catch(Exception e){
      System.out.println("There is something wrong with the UI message.");
    }


  }

  /**
  * This methos handles message from UI.
  *
  **/

  private void HandleMessageFromServerUI(String message){

  	if(message.length()==0){

  		System.out.println("You need to enter a command or a message and then press enter.");

  	}else{

  	if(message.charAt(0) == '#'){
  		handleCommandFromServer(message);
  	}else{

  		server.sendToAllClientsFromServer(message);

  	}

  	}

  }


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

        server.handleMessageFromServer(message);

        HandleMessageFromServerUI(message);
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
    int port = 0;  //The port number

    try{

      port = Integer.parseInt(args[0]);

    }
    catch(ArrayIndexOutOfBoundsException e2){

      port = DEFAULT_PORT;

    }
    
    ServerConsole chat= new ServerConsole(port);

    


    try 
    {
      chat.server.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }

    chat.accept();  //Wait for console data
  }
}



	
