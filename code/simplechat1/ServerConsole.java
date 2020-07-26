// ServerConsole class allows user input directly into the server's console, then
// echoes the input to all clients

import java.io.*;
import common.*;

public class ServerConsole implements ChatIF {
	
	// Class variables *************************************************
  
	/**
	* The default port to connect on.
	*/
	final public static int DEFAULT_PORT = 5555;
	
	// Instance variables **********************************************
  
	/**
	* The instance of the server that created this ServerConsole.
	*/
	EchoServer server;
	
	// Constructor ****************************************************
	
	/**
	* Constructs an instance of the ServerConsole UI.
	*
	* @param port The port to connect on.
	*/
	public ServerConsole(int port) {
		server = new EchoServer(port);
		try {
			
			server.listen();
		} 
		catch(IOException exception) {
			System.out.println("Error: Can't setup connection!"
			+ " Terminating client.");
			System.exit(1);
		}
	}
	
	// Instance methods ************************************************
  
	/**
	* This method waits for input from the console.  Once it is 
	* received, it sends it to the server's message handler.
	*/
	public void accept() {
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message;
			while (true) {
				message = fromConsole.readLine();
				server.handleMessageFromServer(message);
				display(message);
			}
		} 
		catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}
	
	/**
	* This method overrides the method in the ChatIF interface.  It
	* displays a message onto the screen.
	*
	* @param message The string to be displayed.
	*/
	public void display(String message) {
		System.out.println("SERVER MSG > " + message);
	}
	
	// Class methods ***************************************************
  
	/**
	* This method is responsible for the creation of the server instance.
	*
	* @param args[0] The port number to listen on.  Defaults to 5555 if no argument is entered.
	*/
	public static void main(String[] args) {
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		}
		catch(Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}
		ServerConsole sc = new ServerConsole(port);
		/* try {
			sc.server.listen(); // Start listening for connections
		} 
		catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		} */
		sc.accept();
	}
}
// End of ServerConsole class