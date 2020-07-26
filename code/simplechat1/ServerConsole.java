// ServerConsole class allows user input directly into the server's console, then
// echoes the input to all clients

import java.io.*;
import common.*;

public class ServerConsole implements ChatIF{
	final public static int DEFAULT_PORT = 5555;
	EchoServer server;
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
	public void display(String message) {
		System.out.println("SERVER MSG > " + message);
	}
	public static void main(String[] args) {
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		}
		catch(Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}
		ServerConsole sc = new ServerConsole(port);
		sc.accept();
	}
}
// End of ServerConsole class