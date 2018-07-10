// This file contains material supporting section 10.9 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

/*
 * ServerDriver.java   2001-02-08
 *
 * Copyright (c) 2001 Robert Laganiere and Timothy C. Lethbridge.
 * All Rights Reserved.
 *
 */
package ocsftester;
import ocsf.server.*;
import ocsftester.*;

/**
* The <code> ServerDriver </code> class is a driver
* of the <code> ServerFrame </code> class. It automatically
* changes the states of the server a follows:
* listen for 10 sec, stop for 5 sec, close for 5 sec,
* listen for 20 sec, stop for 10 sec, listen for 10 sec,
* stop for 5 sec. This cycle is indefinitely repeated. <p>
* Type <code>java ocsftester.ServerDriver port_number</code> to start
* the server.<p>
* The window is red
* when the server is closed, yellow when the server is stopped
* and green when open.
*
* @author Dr. Robert Lagani&egrave;re
* @version February 2001
* @see ocsftester.SimpleServer
*/
public class ServerDriver extends Thread
{
  private ServerFrame sf;

  public ServerDriver(int p) {

    sf = new ServerFrame(p);
    start();
  }

  public void run()
  {
    while (true)
    {
      try {
        sleep(5000);
        sf.listen();
        sleep(10000);
        sf.stop();
        sleep(5000);
        sf.close();
        sleep(5000);
        sf.listen();
        sleep(20000);
        sf.stop();
        sleep(10000);
        sf.listen();
        sleep(10000);
        sf.stop();
      } catch (Exception ex) { }
    }
  }

  /**
   * Starts the server. The default port is 12345.
   */
  public static void main(String[] arg)
  {
    if (arg.length==0)
      new ServerDriver(12345);
    if (arg.length==1)
      new ServerDriver(Integer.parseInt(arg[0]));
  }
}
