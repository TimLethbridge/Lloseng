import common.*;
import java.io.*;
public class ServerConsole implements ChatIF{
  EchoServer server;
  public ServerConsole(EchoServer server){
    this.server = server;
  }
  public void display(String message) 
  {
    System.out.println("> " + message);
  }
  public void accept(){
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true){
        message = fromConsole.readLine();
        char messagecheck = message.charAt(0);
        if (messagecheck == '#'){
          String[] cmessage = message.split(" ");
          String command = cmessage[0];
          System.out.println(command);
          if (command.equals("#quit")){
            System.out.println("The server has quit");
            try
            {
              server.close();
            }
            catch(IOException e) {}
            System.exit(0);
          }
          else if (command.equals("#stop")){
            server.stopListening();
            server.sendToAllClients("WARNING - Server has stopped listening for connections.");
          }
          else if (command.equals("#close")){
            server.sendToAllClients("WARNING - The server has stopped listening for connections"+
            "\n"+"SERVER SHUTTING DOWN! DISCONNECTING!");
            server.close();
          }
          else if (command.equals("#setport")){
            if (!server.serverisClosed()){
              System.out.println("The server is still connected.");
            }
            else{
              int newport = Integer.parseInt(cmessage[1]);
              server.setPort(newport);
            }
          }
          else if (command.equals("#start")){
              if(server.isListening()){
                System.out.println("The server is still listening")
              }
              else{
                server.listen();
              }
          }  
        }
        server.sendToAllClients("SERVER MSG> " + message);
      }
    }
    catch(Exception ex){
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
}