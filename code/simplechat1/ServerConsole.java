import java.io.*;
import common.*;

/**
* This class (ServerConsole) implements ChatIF and adds a console to Echoserver
* 
*/

public class ServerConsole implements ChatIF {
    //Instance variables
    private boolean run = true;
    private EchoServer server;

    //Constructor
    public ServerConsole(EchoServer sv){
        server = sv;
    }

    //Displays a message with a ">>" prefix
    public void display (String message){
        System.out.println(">> " + message);
    }

    //
    public void startConsole(){
        try{
            BufferedReader fromConsole = 
              new BufferedReader(new InputStreamReader(System.in));
            String message;
            
            while (run){
              message = fromConsole.readLine();
              display(message);
              server.sendToAllClients("SERVER MSG> " + message);
            }
        }
        catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }
}