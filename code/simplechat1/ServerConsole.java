
import java.io.*;
import common.*;

public class ServerConsole implements ChatIF{
  final public static int DEFAULT_PORT = 5555;
  EchoServer server;

  public ServerConsole( int port)
  {
    try
    {
    server=new EchoServer(port,this);
    server.listen();
    }
    catch(IOException exception)
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating !");
      System.exit(1);
    }
  }
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
        //ADDING HERE!!
        if(message.startsWith("#")){

          switch(message){
            case "#quit":
            System.exit(0);
            break;

            case "#stop":
           server.stopListening();
           server.sendToAllClients("WARNING - Server has stopped listening for connections.");
            break;

            case "#close":
            server.close();

            break;

            case "#start":
            if( server.isListening()){
              System.out.println("Server is already listening!");
            }else{
              server.listen();
            }
            break;

            case "#getport":
            System.out.println(server.getPort());
            break;
            default:
            if(message.startsWith("#setport")){
            String [] elements=message.split(" ");
              if(server.isListening()){
               System.out.println("Please logoff before attempting to set port ");}
               else{
               server.setPort(Integer.valueOf(elements[1]));
              System.out.println("Port set to : "+ server.getPort());
             }
           }else{
            System.out.println("Please enter a valid command!");
}}

        }
        else{


        this.display("SERVER MSG>" + message);
        server.sendToAllClients("SERVER MSG>" + message);}


      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  public  void display(String message){
    System.out.println("> " + message);
  }




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

  ServerConsole sc = new ServerConsole(port);

    try
    {
      sc.server.listen(); //Start listening for connections
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients");
    }
    sc.accept();
  }

}
