// This file contains material supporting section 10.9 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

/*
 * ClientFrame.java   2001-02-08
 *
 * Copyright (c) 2001 Robert Laganiere and Timothy C. Lethbridge.
 * All Rights Reserved.
 *
 */
package ocsftester;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import ocsf.client.*;
import ocsftester.SimpleClient;

/**
* The <code> ClientsFrame </code> class is an application
* that randomly generates clients, connects them to the server,
* makes them sending messages, and closes them. Up to 25 clients can be
* present at a given time, these occupy one of the 25 panels
* included in the window.
* Type <code>java ocsftester.ClientsFrame host port_number</code> to start
* the application.<p>
* A panel is
* pink when the connection has been closed, red,
* orange or light gray when an exception is received,
* blue when it tries to connect to the server,
* and green when connected to the server.
*
* @author Dr. Robert Lagani&egrave;re
* @version February 2001
* @see ocsftester.SimpleClient
*/
public class ClientsFrame extends Frame implements Runnable
{
  private List[] liste;
  private SimpleClient[] sc;
  private int nclient;
  private Thread th;
  private String host;
  private int port;

  public ClientsFrame(String host, int port)
  {
    super("Test OCSF Client");

    this.host= host;
    this.port= port;

    setLayout(new GridLayout(6,4,5,5));

    liste= new List[24];
    sc= new SimpleClient[24];
    for (int i=0; i<24; i++) {
      liste[i]= new List();
      add(liste[i]);
    }

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });

    setSize(500,500);
    setVisible(true);

    th= new Thread(this);
    th.start();
  }

  public void run()
  {
    int r,s;

    while (true)
    {
      r= (int)(Math.random()*24);
      s= (int)(Math.random()*25);

      if (s%5 ==0)
      {
        if (sc[r]==null || !sc[r].isConnected())
        {
          System.out.println("Connecting client " + r);
          liste[r].removeAll();
          liste[r].setBackground(Color.blue);
          sc[r]= new SimpleClient(host,port,liste[r]);
          try
          {
            sc[r].openConnection();
          }
          catch (Exception ex)
          {
            liste[r].add(ex.toString());
            liste[r].makeVisible(liste[r].getItemCount()-1);
            liste[r].setBackground(Color.orange);
          }
        }
      }
      else {

        if (sc[r]!=null && sc[r].isConnected())
        {
          try
          {
            System.out.println("Sending message from " + r);
            sc[r].sendToServer("Message from " + r);
          }
          catch (Exception ex)
          {
            liste[r].add(ex.toString());
            liste[r].makeVisible(liste[r].getItemCount()-1);
            liste[r].setBackground(Color.lightGray);
          }
        }

        r= (r+1)%24;
        if (sc[r]!=null && sc[r].isConnected())
        {
          try
          {
            System.out.println("Sending message from " + r);
            sc[r].sendToServer("Message from " + r);
          }
          catch (Exception ex)
          {
            liste[r].add(ex.toString());
            liste[r].makeVisible(liste[r].getItemCount()-1);
            liste[r].setBackground(Color.lightGray);
          }
        }

        r= (r+1)%24;
        if (sc[r]!=null && sc[r].isConnected())
        {
          try
          {
            System.out.println("Sending message from " + r);
            sc[r].sendToServer("Message from " + r);
          }
          catch (Exception ex)
          {
            liste[r].add(ex.toString());
            liste[r].makeVisible(liste[r].getItemCount()-1);
            liste[r].setBackground(Color.lightGray);
          }
        }

        r= (r+1)%24;
        if (sc[r]!=null && sc[r].isConnected())
        {
          try
          {
            System.out.println("Closing client " + r);
            sc[r].closeConnection();
          }
          catch (Exception ex)
          {
            liste[r].add(ex.toString());
            liste[r].makeVisible(liste[r].getItemCount()-1);
            liste[r].setBackground(Color.lightGray);
          }
        }

      }

      try { th.sleep(1000); } catch(Exception ex) { }
    }
  }

  /**
   * Starts the client generator. The default host is localhost.
   * The default port is 12345.
   */
  public static void main(String[] arg)
  {
    ClientsFrame sf;
    if (arg.length==0)
     sf = new ClientsFrame("localhost",12345);
    if (arg.length==1)
     sf = new ClientsFrame("localhost",Integer.parseInt(arg[0]));
    if (arg.length==2)
     sf = new ClientsFrame(arg[0],Integer.parseInt(arg[1]));
  }
}
