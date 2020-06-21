
import java.io.*;

import common.*;

public class ServerConsole implements ChatIF {

	EchoServer server;
	final public static int DEFAULT_PORT = 5555;

	//Constructors ****************************************************

	public ServerConsole() {
		server = new EchoServer(DEFAULT_PORT);
	}

	public ServerConsole(int port) {
		server = new EchoServer(port);
	}

	//Instance methods ************************************************

	public void display(String message) {
		System.out.println(message);
	}

	//Class methods ************************************************

	public static void main(String[] args) 
	{
		int port = 0;

		try
		{
			port = Integer.parseInt(args[0]);
		}
		catch(Throwable t)
		{
			port = DEFAULT_PORT;
		}

		ServerConsole console = new ServerConsole(port);

		try 
		{
			console.server.listen(); 
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR.");
		}


		try {
			console.server.listen();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			while (true){
				String message = input.readLine();
				console.server.processServerMessage(message);
			}
		} 
		catch (IOException error) 
		{
			System.out.println("ERROR.");
			System.exit(0);
		}
	}

}
