
/*Modified for E6-b Allowing user input
 * 
 * Similar to ClientConsole with minor changes to reflect how the server handle messages
-------------*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import common.ChatIF;
import server.EchoServer;

public class ServerConsole implements ChatIF {

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	EchoServer server;

	public ServerConsole(int port) {
		server = new EchoServer(port);
		try {
			server.listen();
		} catch (IOException e) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	// Instance methods ************************************************

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to all of the clients
	 */
	public void accept() {
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message;

			while ((message = fromConsole.readLine()) != null) {
				server.handleMessageFromServerConsole(message);
				this.display(message);
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the Client UI.
	 *
	 * @param args The port to use for connection
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		ServerConsole serv = new ServerConsole(port);
		serv.accept();
	}

	@Override
	public void display(String message) {
		// TODO Auto-generated method stub

	}

}
