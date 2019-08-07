package midi;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;
import msg.XGMessage;
import msg.XGRequest;

public class XGRequestQueue extends Thread
{	private static Logger log = Logger.getAnonymousLogger();

/************************************************************************************/

	private final Queue<XGRequest> queue = new LinkedList<>();
	private final Set<XGRequest> unresponsedRequests = new HashSet<>();
	private final Midi midi;
	private final int timeout;
	private long time = 0;
	private XGRequest request;
	private boolean waitForQueue = true, waitForResponse = false, quit = false;

	public XGRequestQueue(Midi midi)
	{	this.midi = midi;
		this.timeout = midi.getXGDevice().getTimeout();
	}

	@Override public synchronized void run()
	{	log.info("MIDI queue started...");
		while(!quit)
		{	try
			{	this.wait(time);}
			catch(InterruptedException e)
			{	this.quit = true;}

			if(!this.queue.isEmpty())
			{	synchronized(this.queue)
				{	this.request = this.queue.poll();
					((XGMessage)this.request).transmit();
					this.waitForResponse = true;
					this.waitForQueue = false;
					this.time = this.timeout;
				}
			}
			else
			{	this.waitForQueue = true;
				this.time = 0;
			}
		}
	}

	public synchronized void add(XGRequest request)
	{	synchronized(this.queue)
		{	this.queue.add(request);
			if(waitForQueue) this.notify();
		}
	}

	public synchronized void setResponsed(XGMessage msg)
	{	if(request.setResponsedBy(msg))
		{	log.info(this.request + " responsed after " + (System.currentTimeMillis() - ((XGMessage)this.request).getTimeStamp()) + " ms");
			if(this.waitForResponse) this.notify();
			
			try
			{	Thread.sleep(5);}	//erforderlich, sonst werden requests verschluckt oder responds werden erst nach timeout erkannt !?
			catch(InterruptedException e)
			{	e.printStackTrace();}
		}
		else 
		{	this.unresponsedRequests.add(this.request);
			log.info("unresponsed request " + this.unresponsedRequests.size() + " " + this.request + " after timeout: " + time + " ms");
		}

	}

	public int size()
	{	return this.queue.size();}

	public XGRequest getRequest()
	{	return this.request;}

	public String getOutputName()
	{	return this.midi.getOutputName();}
}
