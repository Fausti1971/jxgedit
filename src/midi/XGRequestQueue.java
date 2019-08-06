package midi;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;
import msg.XGMessage;
import msg.XGRequest;

public class XGRequestQueue implements Runnable
{	private static Logger log = Logger.getAnonymousLogger();

	private final Queue<XGRequest> queue = new LinkedList<>();
	private final Set<XGRequest> unresponsedRequests = new HashSet<>();
	private Midi midi;
	private XGRequest request;
	private long startTime, endTime, timeout, time;
	private volatile boolean cancel = false;

	public XGRequestQueue(Midi midi)
	{	this.midi = midi;
		this.timeout = this.midi.getXGDevice().getTimeout();
	}

	@Override public void run()
	{	while(!this.cancel)
		{	while(!queue.isEmpty())
			{	this.request = queue.poll();
				this.midi.transmit((XGMessage)this.request);
				this.startTime = System.currentTimeMillis();
				this.time = 0;
				while(true)
				{	try
					{	Thread.sleep(10);}
					catch(InterruptedException e)
					{	e.printStackTrace();}
					if(this.request.isResponsed()) break;
					if((this.time += 10) > this.timeout)
					{	this.timeOut(System.currentTimeMillis() - this.startTime);
						break;
					}
				}
	//			listener.queueSizeChanged(this);
	//			listener.queueTextChanged(this);
			}
			try
			{	Thread.sleep(50);}
			catch(InterruptedException e)
			{	e.printStackTrace();}
		}
		System.out.println(this.getOutputName() + ": " + this.size());
	}

	public synchronized void add(XGRequest request)
	{	this.queue.add(request);
//		listener.queueSizeChanged(this);
	}

	public synchronized void cancel()
	{	this.cancel = true;}

	public synchronized void setResponsed(XGMessage msg)
	{	if(request.setResponsedBy(msg))
		{	this.endTime = System.currentTimeMillis();
			try
			{	Thread.sleep(5);}	//erforderlich, sonst werden requests verschluckt
			catch(InterruptedException e)
			{	e.printStackTrace();}
//			log.info(this.request.getClass().getSimpleName() + this.request + " responsed after " + (this.endTime - this.startTime) + " ms");
		}
	}

	public int size()
	{	return this.queue.size();}

	public XGRequest getRequest()
	{	return this.request;}

	public String getOutputName()
	{	return this.midi.getOutputName();}

	public void timeOut(long time)
	{	this.unresponsedRequests.add(this.request);
		log.info("unresponsed request " + this.unresponsedRequests.size() + " " + this.request + " after timeout: " + time + " ms");
	}
}
