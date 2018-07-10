// This file contains material supporting section 10.9 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

/*
 * SimpleClient.java   2001-02-08
 *
 * Copyright (c) 2001 Robert Laganiere and Timothy C. Lethbridge.
 * All Rights Reserved.
 *
 */
package ocsftester;

import java.awt.List;
import java.awt.Color;
import ocsf.client.*;

/**
* The <code> SimpleClient </code> class is a simple subclass
* of the <code> ocsf.server.AbstractClient </code> class.
* It allows testing of the functionalities offered by the
* OCSF framework. The <code> java.awt.List </code> instance
* is used to display informative messages.
* This list is
* pink when the connection has been closed, red
* when an exception is received,
* and green when connected to the server.
*
* @author Dr. Robert Lagani&egrave;re
* @version February 2001
* @see ocsf.server.AbstractServer
*/
public class SimpleClient extends AbstractClient
{
  private List liste;

  public SimpleClient(List liste)
  {
    super("localhost",12345);
    this.liste = liste;
  }

  public SimpleClient(int port, List liste)
  {
    super("localhost",port);
    this.liste = liste;
  }

  public SimpleClient(String host, int port, List liste)
  {
    super(host,port);
    this.liste = liste;
  }

  /**
   * Hook method called after the connection has been closed.
   */
  protected void connectionClosed()
  {
    liste.add("**Connection closed**");
    liste.makeVisible(liste.getItemCount()-1);
    liste.setBackground(Color.pink);
  }

  /**
   * Hook method called each time an exception is thrown by the
   * client's thread that is waiting for messages from the server.
   *
   * @param exception the exception raised.
   */
  protected void connectionException(Exception exception)
  {
    liste.add("**Connection exception: " + exception);
    liste.makeVisible(liste.getItemCount()-1);
    liste.setBackground(Color.red);
  }

  /**
   * Hook method called after a connection has been established.
   */
  protected void connectionEstablished()
  {
    liste.add("--Connection established");
    liste.makeVisible(liste.getItemCount()-1);
    liste.setBackground(Color.green);
  }

  /**
   * Handles a message sent from the server to this client.
   *
   * @param msg   the message sent.
   */
  protected void handleMessageFromServer(Object msg)
  {
    liste.add(msg.toString());
    liste.makeVisible(liste.getItemCount()-1);
  }
}
