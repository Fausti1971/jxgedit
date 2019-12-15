package device;

public class TimeoutThread implements Runnable
{
/**********************************************************************************************/

	private long timeout;
	private TimeoutListener listener;
	private long startTime;

	public TimeoutThread(TimeoutListener l, long time)
	{	this.listener = l;
		this.timeout = time;
	}

	public void run()
	{	this.startTime = System.currentTimeMillis();
		try
		{	Thread.sleep(this.timeout);
		}
		catch(InterruptedException e)
		{
		}
	}
}
