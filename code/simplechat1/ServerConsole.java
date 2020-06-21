import java.io.*;
import common.*;
public class ServerConsole implements ChatIF{
	private EchoServer server;
	final public static int DEFAULT_PORT = 5555;

	public ServerConsole(int port) {
		server = new EchoServer (port);
	}
	public void display(String message) {
		server.sendToAllClients("SERVER MSG>: " + message);
	}
	public void handleMessageFromConsole(String message) {
		display(message);
	}
	public void accept() {
		
	    try
	    {
	      BufferedReader fromConsole = 
	        new BufferedReader(new InputStreamReader(System.in));
	      String message;
	      boolean server_status=true;

	      while (true) 
	      {
	    	  message = fromConsole.readLine();
	          char first = message.charAt(0);
	        if (first == '#') {//do the functions base on #
	        	if (message.equals("#quit")) {
	    			System.out.println("The Server has quit");
	    			System.exit(1);
	    		}
	        	else if (message.equals("#stop")) {
	            	server.serverStopped();
	            	server_status = false;
	            	
	            }
	        	else if (message.equals("#close")) {
	            	server.close();
	            	server_status=false;
	            }
	           
	        	else if (message.split(" ")[0].equals(("#setport"))) {
	            	if (server_status==false) {
	            		int holder = Integer.parseInt(message.split(" ")[1]);
	            		server.setPort(holder);
	            	}
	            	else {
	            		System.out.println("You can set port when connections are haulted");
	            	}
	            }
	        	else if (message.equals("#start")) {
	            	server.serverStarted();
	            	server_status=true
	            			;
	            }
	           
	        	else if (message.equals("#getport")) {
	            	System.out.println(server.getPort());
	            }
	            else {
	            	System.out.println("function does not exist");
	            }
	         }
	        
	        else {
	        	server.sendToAllClients("Server MSG>" + message);
	        }
	        
	       }
	    } 
	      catch (Exception ex) {
	    	  System.out.println("Unexpected Error");
	      }
	     }
public EchoServer get() {
	return server;
}
public static void main (String [] args) {
	int port =0;
	try {
		port = Integer.parseInt(args[0]);
	}
	catch (Throwable t) {
		port = DEFAULT_PORT;
	}
	ServerConsole server;
	server = new ServerConsole(port);
	try {
		server.get().listen();
		server.accept();
	}
	catch (Exception ex) {
		System.out.println("ERROR - could not listen for clients");
	}
}

	
	
	

}
