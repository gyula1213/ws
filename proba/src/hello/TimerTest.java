package hello;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
public class TimerTest {
	  public static void main(String[] args) {
	    Timer t = new Timer();
	    TimerTask task = new TimerTask() {
	      public void run()
	      {
	        System.out.println("Timer executed at time: "+ new Date());
	      }
	    };
	    System.out.println("Current time: " + new Date());
	    t.schedule(task, 5000);
	  }
}
