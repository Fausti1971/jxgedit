package obj;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import device.TimeoutException;
import msg.XGMessageDumpRequest;
import msg.XGMessenger;
import msg.XGRequest;
import xml.XMLNode;

public class XGBulkDumpSequence implements XGTypeConstants
{	private static Logger log = Logger.getAnonymousLogger();

	private final XGAddress min, max;

	public XGBulkDumpSequence(XGAddress min, XGAddress max) throws InvalidXGAddressException
	{	if(!min.isValidAdress()) throw new InvalidXGAddressException("invalid value-adress (min): " + min);
		if(!max.isValidAdress()) throw new InvalidXGAddressException("invalid value-adress (max): " + max);
		this.min = min;
		this.max = max;
	}

	public XGBulkDumpSequence(XGAddress adr) throws InvalidXGAddressException
	{	this(adr, adr);
	}

	public XGBulkDumpSequence(XMLNode seq)
	{	String hiMin, hiMax, midMin, midMax, loMin, loMax, temp;

		temp = seq.getChildNode(TAG_HI).getTextContent();
		StringTokenizer t = new StringTokenizer(temp, "-");
		hiMin = hiMax = t.nextToken();
		while(t.hasMoreTokens()) hiMax = t.nextToken();

		temp = seq.getChildNode(TAG_MID).getTextContent();
		t = new StringTokenizer(temp, "-");
		midMin = midMax = t.nextToken();
		while(t.hasMoreTokens()) midMax = t.nextToken();

		temp = seq.getChildNode(TAG_LO).getTextContent();
		t = new StringTokenizer(temp, "-");
		loMin = loMax = t.nextToken();
		while(t.hasMoreTokens()) loMax = t.nextToken();

		this.min = new XGAddress(hiMin, midMin, loMin);
		this.max = new XGAddress(hiMax, midMax, loMax);
	}

	public boolean contains(XGAddress adr)
	{	if(adr.compareTo(this.min) < 0) return false;
		if(adr.compareTo(this.max) > 0) return false;
		return true;
	}

	@Override public String toString()
	{	return min + "..." + max;
	}

	public void requestAll(XGMessenger src, XGMessenger dest)
	{	try
		{	int hi, mid, lo = this.min.getLo();
			for(hi = this.min.getHi(); hi <= this.max.getHi(); hi++)
				for(mid = this.min.getMid(); mid <= this.max.getMid(); mid++)
				{	XGRequest r = new XGMessageDumpRequest(src, dest, new XGAddress(hi, mid, lo));
					src.request(r);
				}
		}
		catch(InvalidXGAddressException | InvalidMidiDataException | TimeoutException e)
		{	e.printStackTrace();
		}
	}

	public int maxInstanceCount()
	{	try
		{	return this.max.getMid() - this.min.getMid();
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
			return 0;
		}
	}
}