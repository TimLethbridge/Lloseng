import java.io.*;
import common.ChatIF;

public class ServerConsole implements ChatIF {

    //Instance Variables
    EchoServer server;
    int port;


    //Constructors

    public ServerConsole(int port){

      this.port = port;

        try{
            server = new EchoServer(port, this);
            server.listen();
        }
        catch(IOException e){
            System.out.println("Error: Cannot setup server");
            System.exit(1);
        }

    }

    //Instance Methods

    public void display(String message){
        System.out.println(message);
    }
    
    public void accept() 
    {
      try
      {
        BufferedReader fromServer = 
          new BufferedReader(new InputStreamReader(System.in));
        String message;
  
        while (true) 
        {
          message = fromServer.readLine();
          message = "SERVER MSG> " + message;
          display(message);
          server.sendToAllClients(message);
        }
      } 
      catch (Exception ex) 
      {
        System.out.println("Unexpected error while reading from server!");
      }
    }
  

    //Class methods ***************************************************
    
    /**
     * This method is responsible for the creation of the Client UI.
     *
     * @param args[0] The host to connect to.
     */
    public static void main(String[] args) 
    {
      int DEFAULT_PORT = 5555; //Default Port
      int port = 0; //Port to listen on

      try
      {
        port = Integer.parseInt(args[0]); //Get port from command line
      }
      catch(Throwable t)
      {
        port = DEFAULT_PORT; //Set port to 5555
      }

      ServerConsole chat = new ServerConsole(port);
      chat.accept();  //Wait for server data
    }
  }

