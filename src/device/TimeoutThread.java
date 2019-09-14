package device;

import java.util.HashSet;
import java.util.Set;

public class TimeoutThread extends Thread
{
/**********************************************************************************************/

	private Set<TimeoutListener> timeoutListeners = new HashSet<>();
	private long timeout;
	private long startTime;

	public TimeoutThread(long time)
	{	this.timeout = time;
	}

	public void run()
	{	this.startTime = System.currentTimeMillis();
		try
		{	Thread.sleep(timeout);
		}
		catch(InterruptedException e)
		{
		}
		this.notifyTimeoutListeners();
	}

	public void addTimeoutListener(TimeoutListener l)
	{	this.timeoutListeners.add(l);
	}

	public void removeTimeoutListener(TimeoutListener l)
	{	this.timeoutListeners.remove(l);
	}

	private void notifyTimeoutListeners()
	{	for(TimeoutListener l : this.timeoutListeners) l.timeOut(System.currentTimeMillis() - this.startTime);
	}
}
