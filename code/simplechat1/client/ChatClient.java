// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

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
  String loginId;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
   public ChatClient(String loginId,String host, int port, ChatIF clientUI)
     throws IOException
   {

       super(host, port); //Call the superclass constructor
       this.loginId=loginId;
       this.clientUI = clientUI;
       openConnection();
       sendToServer("#loginId: "+ this.loginId + " has logged in");


   }
   public ChatClient(String host, int port) {
     super(host, port);

     BufferedReader fromConsole =
     new BufferedReader(new InputStreamReader(System.in));
   try{  String message= fromConsole.readLine();
     handleMessageFromClientUI(message); }
     catch (Exception ex)
     {
       System.out.println
         ("Unexpected error while reading from console!");
     }
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
   public void handleMessageFromClientUI (String message)
   {
     if(message.charAt(0)=='#'){

                     switch(message){
                       case "#quit":
                         this.quit();
                           break;

                       case "#logoff":
                       try{
                          this.closeConnection();


                        }
                          catch (IOException exception) {
                         System.out.println("ERROR!!");
                     }
                          break;

                   case "#gethost":
                   System.out.println(this.getHost());
                   break;

                   case "#getport":
                   System.out.println(getPort());
                   break;




                   default:

                   String [] elements=message.split(" ");
                   if(message.startsWith("#sethost")){
                     if(!this.isConnected()){
                         this.setHost(elements[1]);
                         System.out.println("Host set to : "+ this.getHost());}
                      else{
                      System.out.println("Please logoff before attempting to set host");
                     }
                    } else{
                     if(message.startsWith("#setport")){
                       if(!this.isConnected()){
                       this.setPort(Integer.valueOf(elements[1]));
                         System.out.println("Port set to : "+ this.getPort());}
                     else{
                      System.out.println("Please logoff before attempting to set port ");
                    }
                  }

                       else{
                         if(message.startsWith("#login")){

                            if(!this.isConnected()){
                                 try{
                                 this.loginId=elements[1];
                                  openConnection();
                               sendToServer("#loginId: "+ this.loginId + " has logged in");


                               }catch(IOException exception){
                             System.out.println("ERROR!");}
                             }
                         else{
                       System.out.println("Login only allowed if the client isn't already connected!");
                         }
                         }
                         else{
                      System.out.println("Please enter another command!");}
                    }

                   break;
                  }
                }

               }

 else{
     try
     {
       sendToServer(message);
     }
     catch(IOException e)
     {
       clientUI.display
         ("Could not send message to server.  Terminating client.");
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
  public void connectionClosed(){
   System.out.print("The connection has been closed!");



  }
  public  void connectionException(java.lang.Exception exception){
    System.out.println("Server has shut down connection!");
    quit();


  }
}
//End of ChatClient class
