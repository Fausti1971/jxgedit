package device;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;
import msg.XGMessage;
import msg.XGMessageListener;
import msg.XGRequest;
import msg.XGResponse;

public class XGRequestQueue extends Thread implements XGMessageListener
{	private static Logger log = Logger.getAnonymousLogger();

/************************************************************************************/
//TODO aufgrund #notify() kann noch nicht zwischen responsed und timeout unterschieden werden; denke über handshake nach....

	private final Queue<XGRequest> queue = new LinkedList<>();
	private final Set<XGRequest> unresponsedRequests = new HashSet<>();
	private final XGDevice device;
	private XGRequest request;
	private boolean quit = false;

	public XGRequestQueue(XGDevice dev)
	{	this.device = dev;
	}

	@Override public synchronized void run()
	{	log.info("MIDI queue started...");
		while(!quit)
		{	try
			{	this.wait();
			}
			catch(InterruptedException e)
			{	this.quit = true;}

			if(!this.queue.isEmpty())
			{	synchronized(this.queue)
				{	this.request = this.queue.poll();
					this.request.request();
				}
			}
		}
	}

	public synchronized void add(XGRequest request)
	{	synchronized(this.queue)
		{	boolean empty = this.queue.isEmpty();
			this.queue.add(request);
			if(empty) this.notify();
		}
	}

	public int size()
	{	return this.queue.size();
	}

	public XGRequest getRequest()
	{	return this.request;
	}

	public String getOutputName()
	{	return this.device.getOutputName();
	}

	public void newXGMessage(XGMessage msg)
	{	if(request.setResponsedBy((XGResponse)msg));
		{	log.info(this.request + " responsed after " + (System.currentTimeMillis() - (this.request).getTimeStamp()) + " ms");
			this.notify();
		}
	}
}
