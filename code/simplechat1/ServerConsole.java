
import java.io.*;
import common.*;

public class ServerConsole implements ChatIF
{
  //Class variables *************************************************
  
    private EchoServer server;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param port The port to connect on.
   */
    public ServerConsole(EchoServer server){

        this.server = server;
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
            if(message.charAt(0)==('#')){
                if(message.equals("#quit")){
                    System.out.println("server quit");
                    server.close();
                }
                else if(message.equals("#setport")){
                    if (server.isListening()){
                        System.out.println("The serveris already connected.");
                               }
                    else{
                        String[] cmd = message.split(" ");
                        try
                        {
                             server.setPort(Integer.parseInt(cmd[1]));
                        }
                        catch(NumberFormatException e)
                        {
                            System.out.println("invaild input");
                        }
                        }
                      }
                else if(message.equals("#stop")){
                    server.sendToAllClients("server stop listening");
                    server.stopListening();
                }
                else if(message.equals("#close")){
                    server.sendToAllClients("server stop listening and stop connecting");
                    server.close();
                }
                else if(message.equals("#start")){
                    if (server.isListening()){
                        System.out.println("The serveris already connected.");
                    }
                    else{
                        server.listen();
                        }
                }
                else if(message.equals("#getport")){
                    System.out.println(server.getPort());
                }
        }
            server.sendToAllClients("Server MSG> " + message);
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
}
//End of ConsoleChat class
