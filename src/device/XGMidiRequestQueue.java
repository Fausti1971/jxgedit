package device;

import adress.XGAddressableSet;
import msg.XGMessengerException;
import msg.XGRequest;

public class XGMidiRequestQueue extends XGAddressableSet<XGRequest> implements Runnable
{	private boolean exiting = false;
	private final XGMidi midi;

	XGMidiRequestQueue(XGMidi midi)
	{	super();
		this.midi = midi;
	}

	public void run()
	{	LOG.info(this.getClass().getSimpleName() + " started");
		while(!this.exiting)
		{	if(this.isEmpty())
			{	synchronized(this)
				{	try{	this.wait();}
					catch(InterruptedException e){	e.printStackTrace();}
				}
			}
			else
			{	try
				{	this.midi.submit(this.get(0));
					synchronized(this)
					{	this.wait(50);
					}
				}
				catch(XGMessengerException | InterruptedException e){	e.printStackTrace();}
			}
		}
	}

	void stop()
	{	this.exiting = true;
	}

	@Override public boolean add(XGRequest obj)
	{	super.add(obj);
		synchronized(this){	this.notify();}
		LOG.info(obj + " added to Queue, size = " + this.size());
		return true;
	}

	@Override public boolean remove(Object o)
	{	boolean b = super.remove(o);
		if(b)
		{	synchronized(this)
			{	this.notify();
				LOG.info(o + " removed from Queue, size = " + this.size());
			}
		}
		return b;
	}
}
