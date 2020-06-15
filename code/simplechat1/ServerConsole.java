import java.io.*;
import common.*;

public class ServerConsole implements ChatIF {

	EchoServer server;

	public ServerConsole(EchoServer server) {
		this.server = server;
	}

	public void accept() {
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
     		String message;
     	    while (true) {
        		message = fromConsole.readLine();
        		char first = message.charAt(0);
        		char command = '#';
        		if (first == command) {
          			String msg = message.substring(1);
          			String[] cmd = msg.split(" ");
          			if (cmd[0].equals("quit")) {
            			System.out.println("The server is quit.");
            			try {
            				server.close();
            			}
            			catch(IOException e) {}
            			System.exit(0);
            		} else if (cmd[0].equals("stop")) {
            			server.sendToAllClients("WARNING - Server has stopped listening for connections.");
            			server.stopListening();
            		} else if (cmd[0].equals("close")) {
            			server.sendToAllClients("WARNING - The server has stopped listening for connections" +
            			"\n" + "SERVER SHUTTING DOWN! DISCONNECTING!");
            			server.close();
            		} else if (cmd[0].equals("setport")) {
            			if (server.serverIsClosed()) {
              				server.setPort(Integer.parseInt(cmd[1]));
              				System.out.println("The port set to: " + cmd[1]);
            			} else {
              				System.out.println("The server is not closed, so port cannot be set!");
            			}
            		} else if (cmd[0].equals("start")) {
            			if (!server.isListening()) {
            				server.listen();
            				System.out.println("The server is started.");
            			} else {
            				System.out.println("The server is not stopped, so it cannot start!");
            			}
            		}else if (cmd[0].equals("getport")) {
            			System.out.println("The port is: " + server.getPort());
            		}
            	}else {
            		server.sendToAllClients( "SERVER MSG>" + message);
            	}
            }
        }
        catch (Exception ex) {
        	System.out.println("Unexpected error while reading from console!");
    	}
  	}

	public void display(String message) {
		System.out.println("> " + message);
	}

	public static void main(String[] args) {
		EchoServer server = new EchoServer(0);
		try
    	{
      		server.setPort(Integer.parseInt(args[0]));
    	}
    	catch(ArrayIndexOutOfBoundsException e)
    	{
      		server.setPort(5555);
    	}
	}
}