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

/**
* The <code> ClientFrame </code> class is a simple interactive
* application made to exercise the OCSF framework.<p>
* Type <code>java ocsftester.ClientFrame host port_number</code> to start
* one client.<p>
* The window is
* pink when the connection has been closed, red
* when an exception is received,
* and green when connected to the server.
*
* @author Dr. Robert Lagani&egrave;re
* @version February 2001
* @see ocsftester.SimpleClient
*/
public class ClientFrame extends Frame
{
  private Button closeB =     new Button("Close");
  private Button openB =      new Button("Open");
  private Button sendB =      new Button("Send");
  private Button quitB =      new Button("Quit");
  private TextField port =    new TextField("12345");
  private TextField host =    new TextField("localhost");
  private TextField message = new TextField();
  private Label portLB =      new Label("Port: ", Label.RIGHT);
  private Label hostLB =      new Label("Host: ", Label.RIGHT);
  private Label messageLB =   new Label("Message: ", Label.RIGHT);
  private List liste =        new List();
  private SimpleClient client;

  public ClientFrame(String h, int p)
  {
    super("OCSF Client");

    client = new SimpleClient(h, p, liste);
    port.setText(String.valueOf(p));
    host.setText(h);

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

    openB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        open();
      }
    });

    sendB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        send();
      }
    });

    Panel bottom = new Panel();
    bottom.setLayout(new GridLayout(5,2,5,5));

    bottom.add(hostLB);
    bottom.add(host);
    bottom.add(portLB);
    bottom.add(port);
    bottom.add(messageLB);
    bottom.add(message);
    bottom.add(openB);
    bottom.add(sendB);
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

    client.setPort(p);
    client.setHost(host.getText());
  }

  public void close()
  {
    try {
      readFields();
      client.closeConnection();
    }
    catch (Exception ex)
    {
      liste.add(ex.toString());
      liste.makeVisible(liste.getItemCount()-1);
      liste.setBackground(Color.red);
    }
  }

  public void open()
  {
    try {
      readFields();
      client.openConnection();
    }
    catch (Exception ex)
    {
      liste.add(ex.toString());
      liste.makeVisible(liste.getItemCount()-1);
      liste.setBackground(Color.red);
    }
  }

  public void send()
  {
    try {
      readFields();
      client.sendToServer(message.getText());
    }
    catch (Exception ex)
    {
      liste.add(ex.toString());
      liste.makeVisible(liste.getItemCount()-1);
      liste.setBackground(Color.yellow);
    }
  }

  public void quit()
  {
    System.exit(0);
  }

  /**
   * Starts the client. The default host is localhost.
   * The default port is 12345.
   */
  public static void main(String[] arg)
  {
    ClientFrame sf;
    if (arg.length==0)
     sf = new ClientFrame("localhost",12345);
    if (arg.length==1)
     sf = new ClientFrame("localhost",Integer.parseInt(arg[0]));
    if (arg.length==2)
     sf = new ClientFrame(arg[0],Integer.parseInt(arg[1]));
  }
}
