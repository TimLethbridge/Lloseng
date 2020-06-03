// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  private String info; // Client's login id 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }




  
  //Instance methods ************************************************

  /**
  * This method retrive the id from the string that came from a client.
  *
  **/
  private String retriveID(String str){

    String id= "";
    int start = 8;
    int end = str.length();

    try{



      for(int i =start ; i < end-1 ; i++){

        id += str.charAt(i);

      }

    }catch(Exception e){

      System.out.println("Check the ID.");

    }

    return id;

  }
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
    String message = "";


    try{

      message = (String) msg;

    }catch(Exception e){
      System.out.println("The message recieved but the type is not acceptable.");
    }

    if(client.getInfo("firstConnection") == null && message.startsWith("#login <") == false ){

      try{
        client.sendToClient("You need to provide an ID to use the system.");
        client.close();          
        }
        catch(Exception e){
            System.out.println("Error: Sending message to a new client.");
        }

    }

      if(message.startsWith("#login <") == true){

        if(client.getInfo("firstConnection") == null ){// meaning the first connection

          System.out.println("Message received #login <loginID> from " +client.getInfo("LoginID"));
          client.setInfo("LoginID", retriveID(message));
          client.setInfo("firstConnection","YES") ; // meaning the user is already connected

          try{
            this.sendToAllClients("<" + client.getInfo("LoginID") + "> logged on.");
            System.out.println("<" + client.getInfo("LoginID") + "> logged on.");
          }
          catch(Exception e){
            System.out.println("Error: Sending message to client.");
          }
          
          
        }else if(client.getInfo("firstConnection") == "YES"){
          try{
            client.sendToClient("You alraedy have a username.");
          }
          catch(Exception e){
            System.out.println("Error: Sending message to client.");
          }
      }

      }else{

      System.out.println("Message received: " + msg + " from " + client.getInfo("LoginID"));

      this.sendToAllClients(client.getInfo("LoginID") +">"+ message);
    
  

    
  }
}




  /**
  * This methos handles messages that enters on the server console.
  *
  **/
  public void handleMessageFromServer(Object message){

  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  protected void clientConnected(ConnectionToClient client) {

    System.out.println(" A new client is attempting to connect to the server");
  }


  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(
    ConnectionToClient client) {

    sendToAllClients(client.getInfo("LoginID") + " is disconnected.");
    System.out.println(client.getInfo("LoginID") + " is disconnected.");

  }

  /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {

    sendToAllClients(client.getInfo("LoginID") + " is disconnected.");
    System.out.println(client.getInfo("LoginID") + " is disconnected.");
  }


  private Object messageFromServer(Object message){
    try{
      return (Object) ("SERVER MSG>" + (String) message);
    }
    catch(Exception e){
      System.out.println("The message type which send is not acceptable. Try again.");
    }

    return "Error has occurd!";
  }

  /**
   * Sends a message to every client connected to the server.
   * This is merely a utility; a subclass may want to do some checks
   * before actually sending messages to all clients.  This method
   * can be overriden, but if so it should still perform the general
   * function of sending to all clients, perhaps after some kind
   * of filtering is done. Any exception thrown while
   * sending the message to a particular client is ignored.
   *
   * @param msg   Object The message to be sent
   */

  public void sendToAllClientsFromServer(Object msg)
  {
    Thread[] clientThreadList = getClientConnections();

    for (int i=0; i<clientThreadList.length; i++)
    {
      try
      {
        ((ConnectionToClient)clientThreadList[i]).sendToClient( messageFromServer(msg));
      }
      catch (Exception ex) {}
    }
  }




  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
