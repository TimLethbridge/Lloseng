/*
* This is the ServerConsole calss for the Assignment1.
* Helps Echoserver access multiple methods and variables specific to the class
*/

import java.io.*;
import client.*;
import common.*;

public class ServerConsole implements ChatIF {
    private EchoServer server;
    final public static int DEFAULT_PORT = 5555;
    public ServerConsole(int port) {
        server = new EchoServer(port,this);
    }
    public void display(String message) {
        server.sendToAllClients("ServerMSG> " + message);
    }
    public void handleMessageFromConsole(String message){
        display(message);  
    }
    public void accept(){
    try{
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;
      boolean status = true;
      while (true) {
        message = fromConsole.readLine();
        if(message.contains("#")){
        	if(message.equals("#quit")){
        		System.exit(0);
        	}else if(message.equals("#stop")){
        		server.serverStopped();
        		status = false;
        	}else if(message.equals("#start")){
        		server.serverStarted();
        		status= true;
        	}else if(message.equals("#close")) {
        		server.close();
        	}else if(message.contains("#setport")){
        		if(status==false){
        			String temp =message.split(" ")[1];
        			int temp1 = Integer.parseInt(temp);
        			server.setPort(temp1);
        		}else{
        			System.out.println("Error, you can only set port when connections are stopped.");
        		}
        	}else if (message.equals("#getport")) {
        		System.out.println(server.getPort());
        	}else{
        		System.out.println("Error, #'s are functions, this function does not exist.");
        	}
        }else{
        	handleMessageFromConsole(message);
        }
      }
    } 
    catch (Exception ex){
        System.out.println("Unexpected error while reading from console!");
    }
}

public EchoServer get(){
    return server;
}

public static void main(String[] args){
    int port = 0; //Port to listen on
    try {
        port = Integer.parseInt(args[0]); //Get port from command line
    }catch(Throwable t) {
        port = DEFAULT_PORT; //Set port to 5555
    }
    ServerConsole server = new ServerConsole(port);
    try{
        server.get().listen(); //Start listening for connections
        server.accept();
    }
    catch (Exception ex){
        System.out.println("ERROR - Could not listen for clients!");
    }
}
}