// This file contains material supporting section 10.9 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

/*
 * SimpleServer.java   2001-02-08
 *
 * Copyright (c) 2000 Robert Laganiere and Timothy C. Lethbridge.
 * All Rights Reserved.
 *
 */
package ocsftester;

import java.awt.List;
import java.awt.Color;
import ocsf.server.*;

/**
* The <code> SimpleServer </code> class is a simple subclass
* of the <code> ocsf.server.AbstractServer </code> class.
* It allows testing of the functionalities offered by the
* OCSF framework. The <code> java.awt.List </code> instance
* is used to display informative messages. This list is red
* when the server is closed, yellow when the server is stopped
* and green when open.
*
* @author Dr. Robert Lagani&egrave;re
* @version February 2001
* @see ocsf.server.AbstractServer
*/
public class SimpleServer extends AbstractServer
{
  private List liste;

  /**
   * Creates a simple server. The default port is 12345.
   *
   * @param liste the liste on which information will be displayed.
   */
  public SimpleServer(List liste)
  {
    super(12345);
    this.liste = liste;
  }

  /**
   * Creates a simple server.
   *
   * @param port the port on which the server will listen.
   * @param liste the liste on which information will be displayed.
   */
  public SimpleServer(int port, List liste)
  {
    super(port);
    this.liste = liste;
  }

  /**
   * Hook method called each time a new client connection is
   * accepted.
   *
   * @param client the connection connected to the client.
   */
  synchronized protected void clientConnected(ConnectionToClient client)
  {
    liste.add("Client connected: " + client);
    liste.makeVisible(liste.getItemCount()-1);
  }

  /**
   * Hook method called each time a client disconnects.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client)
  {
    liste.add("Client disconnected: " + client);
    liste.makeVisible(liste.getItemCount()-1);
  }

  /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(ConnectionToClient client,
                                        Throwable exception)
  {
    liste.add("Client exception: " + exception + " with " + client);
    liste.makeVisible(liste.getItemCount()-1);
  }

  /**
   * Hook method called when the server stops accepting
   * connections because an exception has been raised.
   *
   * @param exception the exception raised.
   */
  protected void listeningException(Throwable exception)
  {
    liste.add("Listening exception: " + exception);
    liste.makeVisible(liste.getItemCount()-1);
    liste.setBackground(Color.red);
  }

  /**
   * Hook method called when the server stops accepting
   * connections.
   */
  protected void serverStopped()
  {
    liste.add("Server stopped");
    liste.makeVisible(liste.getItemCount()-1);
    liste.setBackground(Color.yellow);
  }

  /**
   * Hook method called when the server is clased.
   */
  protected void serverClosed()
  {
    liste.add("Server closed");
    liste.makeVisible(liste.getItemCount()-1);
    liste.setBackground(Color.red);
  }

  /**
   * Hook method called when the server starts listening for
   * connections.
   */
  protected void serverStarted()
  {
    liste.add("Server started : " + this.getPort());
    liste.makeVisible(liste.getItemCount()-1);
    liste.setBackground(Color.green);
  }

  /**
   * Handles a command sent from one client to the server.
   *
   * @param msg   the message sent.
   * @param client the connection connected to the client that
   *  sent the message.
   */
  protected void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
    liste.add(msg.toString() + ":" + client);
    liste.makeVisible(liste.getItemCount()-1);
    sendToAllClients(msg);
  }
}
