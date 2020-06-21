import java.io.*;
import client.*;
import common.*;

public class ServerConsole implements ChatIF{

// class variables
//default port number
  final public static int DEFAULT_PORT = 5555;

//EchoServer object and server status
// "false" represents the server is open
// "true" represents the server is closed
private EchoServer sc;
private boolean serverstatus = false;

//Constructor
public ServerConsole(int port){
  sc = new EchoServer(port,this);
}
public EchoServer get(){
  return sc;
}


// Message handle server
public void handleMessageFromServer(String message){
  String temp2[] = message.split(" ");
  if(message.equals("#quit")){
    display("Client has quit");
    System.out.println("Closing Connection");
    try{
      sc.close();
      System.exit(0);
    }catch (Exception e) {
      System.out.println("ERROR!");
    }
  }else if(message.equals("#stop")){
    display("Server has stopped listening for connections");
    System.out.println("Closing Connection");
    try{
      sc.stopListening();
      serverstatus = true;
    }catch (Exception e) {
      System.out.println("ERROR!");

    }
  }else if(message.equals("#close")){
    display("Server has stopped connection");
    System.out.println("Closing Connections");
    try{
      sc.close();
      serverstatus = true;
    }catch (Exception e) {
      System.out.println("ERROR!");
    }
  }else if(temp2[0].equals("#setport")&& serverstatus){
    display("Set port");
    try{
      String temp[] = message.split(" ");
      int newport = Integer.parseInt(temp[1]);
      System.out.println("Setting the port to" + newport);
      sc.setPort(newport);
      sc.listen();
      serverstatus =false;

  }catch (Exception e) {
    System.out.println("ERROR!");
  }
}else if(message.equals("#start")&& serverstatus){
  display("Server is listening for new clients");
    System.out.println("Opening Connections");
    try{
      sc.listen();
      serverstatus = false;
    }catch (Exception e) {
        System.out.println("ERROR!");
    }
}else if(message.equals("#getport")){
  try{
      System.out.println(sc.getPort());
  }catch (Exception e) {
      System.out.println("ERROR!");
  }
}else if(message.charAt(0) == '#'){
    System.out.println("ERROR! Command invalid");
}else{
  display(message);
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
      handleMessageFromServer(message);
    }
  }
  catch (Exception ex)
  {
    System.out.println
      ("Unexpected error while reading from console!");
  }
}
public void display(String message)
{
  sc.sendToAllClients("SERVER MSG>" + message);
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
    sc.get().listen(); //Start listening for connections
    sc.accept();
  }
  catch (Exception ex)
  {
    System.out.println("ERROR - Could not listen for clients!");
  }
}



}
