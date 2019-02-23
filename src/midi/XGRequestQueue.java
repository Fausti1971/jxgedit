package midi;

import java.util.LinkedList;
import java.util.Queue;

import msg.XGMessage;
import msg.XGRequest;

public class XGRequestQueue extends Thread
{	private final Queue<XGRequest> queue = new LinkedList<>();
	private Midi midi;
	private int timeout;
	private XGRequest request;
	private volatile boolean isAlive = false;

	public XGRequestQueue(Midi midi, int timeout)
	{	this.midi = midi;
		this.timeout = timeout;
	}

	@Override public void run()
	{	isAlive = true;
		while(isAlive)
		{	try
			{	sleep(timeout);
			}
			catch (InterruptedException e1)
			{
			}
			while(queue.isEmpty())
				try
				{	sleep(10000);
				}
				catch (InterruptedException e)
				{
				}
			request = queue.poll();
			this.midi.transmit((XGMessage)request);
//			listener.queueSizeChanged(this);
//			listener.queueTextChanged(this);
		}
	}

	public synchronized void add(XGRequest request)
	{	queue.add(request);
		notifyAll();
//		listener.queueSizeChanged(this);
	}

	public synchronized void cancel()
	{	isAlive = false;
		notifyAll();
	}

	public synchronized void setResponsed(XGMessage msg)
	{	if(request == null) return;
		if(request.isResponsed(msg)) notify();
		
	}

	public int size()
	{	return queue.size();
	}

	public XGRequest getRequest()
	{	return request;
	}

	public String getOutputName()
	{	return this.midi.getOutputName();
	}
}
