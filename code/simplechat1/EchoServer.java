// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.ChatIF;
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
public class EchoServer extends AbstractServer {
    //Class variables *************************************************

    /**
     * The default port to listen on.
     */
    final public static int DEFAULT_PORT = 5555;
    private ChatIF serverUI;
    private boolean closed;
    //Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to connect on.
     */
    public EchoServer(int port, ChatIF serverUI) {
        super(port);
        this.serverUI = serverUI;
    }


    //Instance methods ************************************************

    /**
     * This method handles any messages received from the client.
     *
     * @param msg    The message received from the client.
     * @param client The connection from which the message originated.
     */
    public void handleMessageFromClient
    (Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));
        String message = (String) msg;
        if(message.startsWith("#login")) {
            try {
                String loginId = message.split(" ")[1];
                if(client.getInfo("loginId") != null) {
                    client.sendToClient("Error - You has already logined.");
                } else {
                    client.setInfo("loginId",loginId);
                    message = String.format("%s has logged on.",loginId);
                    System.out.println(message);
                    client.sendToClient(message);
                }
            }catch (Throwable e) {}
        } else {
            Object temp = client.getInfo("loginId");
            if (temp == null) {
                try {
                    client.close();
                } catch (IOException e) {
                }
            } else {
                this.sendToAllClients((String) temp + "> " + msg);
            }
        }
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println
                ("Server listening for connections on port " + getPort());
        closed = false;
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped() {
        System.out.println
                ("Server has stopped listening for connections.");
    }

    public void handleMessageFromServerUI(String message) {
        if (message.startsWith("#quit")) {
            try {
                close();
                System.out.println("The server quits.");
            } catch (IOException e) {
            }
            System.exit(0);
        } else if (message.startsWith("#stop")) {
            stopListening();
            sendToAllClients("WARNING - Server has stopped listening for connections.");
        } else if (message.startsWith("#close")) {
            try {
                sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
                close();
            } catch (IOException e) {
            }
        } else if (message.startsWith("#setport")) {
            if (!closed) {
                System.out.println("Only allowed if the server is closed");
                return;
            }
            try {
                int port = Integer.parseInt(message.split("\\s+")[1]);
                setPort(port);
                System.out.printf("port set to: %d.\n",port);
            } catch (Exception e) {
                serverUI.display("Invalid port.");
            }
        } else if (message.startsWith("#start")) {
            try {
                listen();
            } catch (IOException e) {
            }
        } else if (message.startsWith("#getport")) {
            serverUI.display("current port: " + getPort());
        } else if (message.startsWith("#")) {
            serverUI.display("Unknown command.");
        } else {
            message = "SERVER MSG> " + message;
            serverUI.display(message);
            sendToAllClients(message);
        }
    }

    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of
     * the server instance (there is no UI in this phase).
     *
     * @param args[0] The port number to listen on.  Defaults to 5555
     *                if no argument is entered.
     */
//  public static void main(String[] args)
//  {
//    int port = 0; //Port to listen on
//
//    try
//    {
//      port = Integer.parseInt(args[0]); //Get port from command line
//    }
//    catch(Throwable t)
//    {
//      port = DEFAULT_PORT; //Set port to 5555
//    }
//
//    EchoServer sv = new EchoServer(port);
//
//    try
//    {
//      sv.listen(); //Start listening for connections
//    }
//    catch (Exception ex)
//    {
//      System.out.println("ERROR - Could not listen for clients!");
//    }
//  }
    public static void main(String[] args) {
        int port = 0; //Port to listen on

        try
        {
            port = Integer.parseInt(args[0]); //Get port from command line
        }
        catch(Throwable t)
        {
            port = 5555; //Set port to 5555
        }
        ServerConsole serverConsole = new ServerConsole(port);
        serverConsole.accept();
    }


    @Override
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("A new client is attempting to connect to the server.");
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        System.out.println(client.getInfo("loginId") + " has disconnected.");
        sendToAllClients(client.getInfo("loginId") + " has disconnected.");
    }

    @Override
    protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
        System.out.println(client.getInfo("loginId") + " has disconnected.");
        sendToAllClients(client.getInfo("loginId") + " has disconnected.");
    }

    @Override
    protected void serverClosed() {
        closed = true;
    }
}
//End of EchoServer class
