package demo;

public class Clock extends Thread
{
    private int sec=0;
    private boolean gameStop;   // Erteke true, ha nem kell, hogy menjen az ora
    private LedCanvas clockCanvas;  // itt latszik az ora
    
    public Clock(int sec)       // innen indul az ora
    {
        this.sec = sec;
    }

    public Clock()
    {
        this(0);
    }

    public void setLedCanvas(LedCanvas lc)
    {
        clockCanvas = lc;
    }

    public void showClock()
    {
        if ( clockCanvas != null )
            clockCanvas.drawClock( sec );
    }
    
    public int getSec()
    {
        return sec;
    }
    
    public void run()
    {
	    while (true)
	    {
	        if ( gameStop )
	            break;
	            
	        showClock();
	        if ( sec % 60 == 0 )
	        {
		        //Toolkit.getDefaultToolkit().beep();
		        System.out.println( this + " (" + sec + ")" );
	        }
	        sec++;

	        try
	        {
		        Thread.sleep(1000);
	        }
	        catch (InterruptedException e)
	        {
	        }
	    }
    }
    
    public void stopClock()
    {
        gameStop = true;
    }
    public void startClock()
    {
        gameStop = false;
        sec = 0;
        start();
    }
}
