import common.ChatIF;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerConsole implements ChatIF {

    private EchoServer echoServer;

    public ServerConsole(int port) {
        echoServer = new EchoServer(port,this);
        try
        {
            echoServer.listen(); //Start listening for connections
        }
        catch (Exception ex)
        {
            System.out.println("ERROR - Could not listen for clients!");
            System.exit(0);
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
                echoServer.handleMessageFromServerUI(message);
            }
        }
        catch (Exception ex)
        {
            System.out.println
                    ("Unexpected error while reading from console!");
        }
    }

    @Override
    public void display(String message) {
        System.out.println(message);
    }

}
