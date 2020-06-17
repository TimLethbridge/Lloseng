// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
	//Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	ChatIF server;
	boolean serverStatus;

	//Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) 
	{
		super(port);
	}

	//Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param message The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient
	(Object message, ConnectionToClient client)
	{
		if(((String) message).charAt(0) == '#') {
			if((boolean)(client.getInfo("connected"))){	
				String[] input = ((String)message).split(" ", 0);
				client.setInfo("connected", false);

				//check to see if message is login message
				if(input[0].equals("#login")) {
					client.setInfo("login", input[1]);
				}
				else {
					try {
						client.sendToClient("You must log in to start session.");
						client.close();
					}
					catch(IOException e){
						System.exit(0);
					}
				}
			}
			else{
				if(((String)message).startsWith("#login")){
					try{
						client.sendToClient("You are already logged in.");
					}
					catch(IOException e){
						System.exit(0);
					}
				}
			}

			System.out.println("Message received: " + message + " from " + client.getInfo("login"));
			this.sendToAllClients("["+ client.getInfo("login") + "] " + message);
		}
	}

	public void processServerMessage(String message) throws IOException {
		//create string array to handle setHost and setPort
		String[] input = message.split(" ", 2);

		if(message.charAt(0) == '#') {
			try {
				switch (input[0]){ 
				case "#quit": System.exit(0);
				break;	

				case "#stop": stopListening();
				break;

				case "#close": close();
				break;

				case "#setport":
					if(!serverStatus) {
						setPort(Integer.parseInt(input[1]));
					}
					break;

				case "#start":
					if(!isListening()) {
						this.listen();
					}
					break;

				case "#getport":
					server.display("Port: "+ getPort());
					break;

				default:
					throw new IOException("Invalid Command"); 

				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		else {
			server.display("SERVER MSG>" + message);
			sendToAllClients("SERVER MSG>" + message);
		}

	}

	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server starts listening for connections.
	 */
	protected void serverStarted()
	{
		System.out.println
		("Server listening for connections on port " + getPort());
		serverStatus = true;
	}

	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server stops listening for connections.
	 */
	protected void serverStopped()
	{
		System.out.println
		("Server has stopped listening for connections.");
		serverStatus = false;
	}

	//handle client connection and disconnection
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("The client, " + client + ", has successfully connected.");
		client.setInfo("connected", true);
	}

	protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("The client, " + client + ", has successfully disconnected.");
	}

	protected void clientException(ConnectionToClient client, Throwable exception) {
		clientDisconnected(client);
	}  

	//Class methods ***************************************************

	/**
	 * This method is responsible for the creation of 
	 * the server instance (there is no UI in this phase).
	 *
	 * @param args[0] The port number to listen on.  Defaults to 5555 
	 *          if no argument is entered.
	 */
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

		EchoServer sv = new EchoServer(port);

		try 
		{
			sv.listen(); //Start listening for connections
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

}
//End of EchoServer class
