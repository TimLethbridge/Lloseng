public class ThreadExample implements Runnable
{
  private int counterNumber;  // Identifies the thread
  private int counter;  // How far the thread has executed
  private int limit;    // Where counter will stop
  private long delay;   // Pause in execution of thread in milisecs

  // Constructor
  private ThreadExample(int countTo, int number, long delay)
  {
    counter = 0;
    limit = countTo;
    counterNumber = number;
    this.delay = delay;
  }
	
  //The run method; when this finishes, the thread teminates
  public void run()
  {
    try
    {
      while (counter <= limit)
      {
        System.out.println("Counter " 
          + counterNumber + " is now at " + counter++);
        Thread.sleep(delay);
      }
    }
    catch(InterruptedException e) {}
  }

  // The main method: Executed when the program is started
  public static void main(String[] args)
  {
    //Create 3 threads and run them
    Thread firstThread = new Thread(new ThreadExample(5, 1, 66));
    Thread secondThread = new Thread(new ThreadExample(5, 2, 45));
    Thread thirdThread = new Thread(new ThreadExample(5, 3, 80));

    firstThread.start();
    secondThread.start();
    thirdThread.start();
  }
}
