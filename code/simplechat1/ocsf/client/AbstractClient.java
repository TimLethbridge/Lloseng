// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package ocsf.client;

import java.io.*;
import java.net.*;

/**
 * The <code> AbstractClient </code> contains all the methods necessary to set
 * up the client side of a client-server architecture. When a client is thus
 * connected to the server, the two programs can then exchange
 * <code> Object </code> instances.
 * <p>
 * Method <code> handleMessageFromServer </code> must be defined by a concrete
 * subclass. Several other hook methods may also be overriden.
 * <p>
 * Several public service methods are provided to application that use this
 * framework.
 * <p>
 * Project Name: OCSF (Object Client-Server Framework)
 * <p>
 * 
 * @author Dr. Robert Lagani&egrave;re
 * @author Dr. Timothy C. Lethbridge
 * @author Fran&ccedil;ois B&eacutel;langer
 * @author Paul Holden
 * @version February 2001 (2.12)
 */
public abstract class AbstractClient implements Runnable {

	// INSTANCE VARIABLES ***********************************************

	/**
	 * Sockets are used in the operating system as channels of communication
	 * between two processes.
	 * 
	 * @see java.net.Socket
	 */
	private Socket				clientSocket;

	/**
	 * The stream to handle data going to the server.
	 */
	private ObjectOutputStream	output;

	/**
	 * The stream to handle data from the server.
	 */
	private ObjectInputStream	input;

	/**
	 * The thread created to read data from the server.
	 */
	private Thread				clientReader;

	/**
	 * Indicates if the thread is ready to stop. Needed so that the loop in the
	 * run method knows when to stop waiting for incoming messages.
	 */
	private boolean				readyToStop	= false;

	/**
	 * The server's host name.
	 */
	private String				host;

	/**
	 * The port number.
	 */
	private int					port;

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs the client.
	 * 
	 * @param host
	 *            the server's host name.
	 * @param port
	 *            the port number.
	 */
	public AbstractClient(String host, int port) {
		// Initialize variables
		this.host = host;
		this.port = port;
	}

	// INSTANCE METHODS *************************************************

	/**
	 * Opens the connection with the server. If the connection is already
	 * opened, this call has no effect.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when opening.
	 */
	final public void openConnection() throws IOException {
		// Do not do anything if the connection is already open
		if (isConnected())
			return;

		// Create the sockets and the data streams
		try {
			clientSocket = new Socket(host, port);
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			input = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException ex)
		// All three of the above must be closed when there is a failure
		// to create any of them
		{
			try {
				closeAll();
			} catch (Exception exc) {
			}

			throw ex; // Rethrow the exception.
		}

		clientReader = new Thread(this); // Create the data reader thread
		readyToStop = false;
		clientReader.start(); // Start the thread
	}

	/**
	 * Sends an object to the server. This is the only way that methods should
	 * communicate with the server.
	 * 
	 * @param msg
	 *            The message to be sent.
	 * @exception IOException
	 *                if an I/O error occurs when sending
	 */
	final public void sendToServer(Object msg) throws IOException {
		if (clientSocket == null || output == null)
			throw new SocketException("socket does not exist");

		output.writeObject(msg);
	}

	/**
	 * Reset the object output stream so we can use the same
	 * buffer repeatedly. This would not normally be used, but is necessary
    * in some circumstances when Java refuses to send data that it thinks has been sent.
	 */
	final public void forceResetAfterSend() throws IOException {
      output.reset();
	}

	/**
	 * Closes the connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when closing.
	 */
	final public void closeConnection() throws IOException {
		// Prevent the thread from looping any more
		readyToStop = true;

		try {
			closeAll();
		} finally {
			// Call the hook method
			connectionClosed();
		}
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * @return true if the client is connnected.
	 */
	final public boolean isConnected() {
		return clientReader != null && clientReader.isAlive();
	}

	/**
	 * @return the port number.
	 */
	final public int getPort() {
		return port;
	}

	/**
	 * Sets the server port number for the next connection. The change in port
	 * only takes effect at the time of the next call to openConnection().
	 * 
	 * @param port
	 *            the port number.
	 */
	final public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the host name.
	 */
	final public String getHost() {
		return host;
	}

	/**
	 * Sets the server host for the next connection. The change in host only
	 * takes effect at the time of the next call to openConnection().
	 * 
	 * @param host
	 *            the host name.
	 */
	final public void setHost(String host) {
		this.host = host;
	}

	/**
	 * returns the client's description.
	 * 
	 * @return the client's Inet address.
	 */
	final public InetAddress getInetAddress() {
		return clientSocket.getInetAddress();
	}

	// RUN METHOD -------------------------------------------------------

	/**
	 * Waits for messages from the server. When each arrives, a call is made to
	 * <code>handleMessageFromServer()</code>. Not to be explicitly called.
	 */
	final public void run() {
		connectionEstablished();

		// The message from the server
		Object msg;

		// Loop waiting for data

		try {
			while (!readyToStop) {
				// Get data from Server and send it to the handler
				// The thread waits indefinitely at the following
				// statement until something is received from the server
				msg = input.readObject();

				// Concrete subclasses do what they want with the
				// msg by implementing the following method
				handleMessageFromServer(msg);
			}
		} catch (Exception exception) {
			if (!readyToStop) {
				try {
					closeAll();
				} catch (Exception ex) {
				}

				connectionException(exception);
			}
		} finally {
			clientReader = null;
		}
	}

	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
	}

	/**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
	protected void connectionEstablished() {
	}

	/**
	 * Handles a message sent from the server to this client. This MUST be
	 * implemented by subclasses, who should respond to messages.
	 * 
	 * @param msg
	 *            the message sent.
	 */
	protected abstract void handleMessageFromServer(Object msg);

	// METHODS TO BE USED FROM WITHIN THE FRAMEWORK ONLY ----------------

	/**
	 * Closes all aspects of the connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs when closing.
	 */
	private void closeAll() throws IOException {
		try {
			// Close the socket
			if (clientSocket != null)
				clientSocket.close();

			// Close the output stream
			if (output != null)
				output.close();

			// Close the input stream
			if (input != null)
				input.close();
		} finally {
			// Set the streams and the sockets to NULL no matter what
			// Doing so allows, but does not require, any finalizers
			// of these objects to reclaim system resources if and
			// when they are garbage collected.
			output = null;
			input = null;
			clientSocket = null;
		}
	}
}
// end of AbstractClient class