// This file contains material supporting section 10.9 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

/*
 * ServerFrame.java   2001-02-08
 *
 * Copyright (c) 2001 Robert Laganiere and Timothy C. Lethbridge.
 * All Rights Reserved.
 *
 */
package ocsftester;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import ocsf.server.*;

/**
* The <code> ServerFrame </code> class is a simple interactive
* application made to exercise the OCSF framework.<p>
* Type <code>java ocsftester.ServerFrame port_number</code> to start
* the server.<p>
* The window is red
* when the server is closed, yellow when the server is stopped
* and green when open.
*
* @author Dr. Robert Lagani&egrave;re
* @version February 2001
* @see ocsftester.SimpleServer
*/
public class ServerFrame extends Frame
{
  private Button closeB =     new Button("Close");
  private Button listenB =    new Button("Listen");
  private Button stopB =      new Button("Stop");
  private Button quitB =      new Button("Quit");
  private TextField port =    new TextField("12345");
  private TextField backlog = new TextField("5");
  private TextField timeout = new TextField("500");
  private Label portLB =      new Label("Port: ", Label.RIGHT);
  private Label timeoutLB =   new Label("Timeout: ", Label.RIGHT);
  private Label backlogLB =   new Label("Backlog: ", Label.RIGHT);
  private List liste =        new List();
  private SimpleServer server;

  public ServerFrame(int p)
  {
    super("OCSF Server");

    server = new SimpleServer(p, liste);
    port.setText(String.valueOf(p));

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      {
        quit();
      }
    });

    quitB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        quit();
      }
    });

    closeB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        close();
      }
    });

    listenB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        listen();
      }
    });

    stopB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        stop();
      }
    });

    Panel bottom = new Panel();
    bottom.setLayout(new GridLayout(5,2,5,5));

    bottom.add(portLB);
    bottom.add(port);
    bottom.add(backlogLB);
    bottom.add(backlog);
    bottom.add(timeoutLB);
    bottom.add(timeout);
    bottom.add(listenB);
    bottom.add(stopB);
    bottom.add(closeB);
    bottom.add(quitB);

    setLayout(new BorderLayout(5,5));
    add("Center", liste);
    add("South", bottom);
    setSize(300,400);
    setVisible(true);
  }

  private void readFields()
  {
    int p = Integer.parseInt(port.getText());
    int t = Integer.parseInt(timeout.getText());
    int b = Integer.parseInt(backlog.getText());

    server.setPort(p);
    server.setBacklog(b);
    server.setTimeout(t);
  }

  public void close()
  {
    System.out.println("Number of clients = " + server.getNumberOfClients());
    try {
      readFields();
      server.close();
    }
    catch (Exception ex)
    {
      liste.add(ex.toString());
      liste.makeVisible(liste.getItemCount()-1);
      liste.setBackground(Color.red);
    }
  }

  public void listen()
  {
    System.out.println("Number of clients = " + server.getNumberOfClients());
    try {
      readFields();
      server.listen();
    }
    catch (Exception ex)
    {
      liste.add(ex.toString());
      liste.makeVisible(liste.getItemCount()-1);
      liste.setBackground(Color.red);
    }
  }

  public void stop()
  {
    System.out.println("Number of clients = " + server.getNumberOfClients());
    readFields();
    server.stopListening();
  }

  public void quit()
  {
    System.exit(0);
  }

  /**
   * Starts the server. The default port is 12345.
   */
  public static void main(String[] arg)
  {
    ServerFrame sf;
    if (arg.length==0)
      sf = new ServerFrame(12345);
    if (arg.length==1)
      sf = new ServerFrame(Integer.parseInt(arg[0]));
  }
}
