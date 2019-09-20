package obj;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import application.Rest;
import device.TimeoutException;
import msg.XGMessageDumpRequest;
import msg.XGMessenger;
import msg.XGRequest;

public class XGBulkDumpSequence implements XGObjectConstants
{	private static Logger log = Logger.getAnonymousLogger();


	private final XGAdress min, max;

	public XGBulkDumpSequence(XGAdress min, XGAdress max) throws InvalidXGAdressException
	{	if(!min.isValidAdress()) throw new InvalidXGAdressException("invalid value-adress (min): " + min);
		if(!max.isValidAdress()) throw new InvalidXGAdressException("invalid value-adress (max): " + max);
		this.min = min;
		this.max = max;
	}

	public XGBulkDumpSequence(XGAdress adr) throws InvalidXGAdressException
	{	this(adr, adr);}

	public XGBulkDumpSequence(Node seq)
	{	String hiMin, hiMax, midMin, midMax, loMin, loMax, temp;

		temp = Rest.getFirstNodeChildTextContentByTagAsString(seq, TAG_HI);
		StringTokenizer t = new StringTokenizer(temp, "-");
		hiMin = hiMax = t.nextToken();
		while(t.hasMoreTokens()) hiMax = t.nextToken();

		temp = Rest.getFirstNodeChildTextContentByTagAsString(seq, TAG_MID);
		t = new StringTokenizer(temp, "-");
		midMin = midMax = t.nextToken();
		while(t.hasMoreTokens()) midMax = t.nextToken();

		temp = Rest.getFirstNodeChildTextContentByTagAsString(seq, TAG_LO);
		t = new StringTokenizer(temp, "-");
		loMin = loMax = t.nextToken();
		while(t.hasMoreTokens()) loMax = t.nextToken();

		this.min = new XGAdress(hiMin, midMin, loMin);
		this.max = new XGAdress(hiMax, midMax, loMax);
	}

	public boolean include(XGAdress adr)
	{	if(adr.compareTo(this.min) < 0) return false;
		if(adr.compareTo(this.max) > 0) return false;
		return true;
	}

	@Override public String toString()
	{	return min + "..." + max;}

	public void requestAll(XGMessenger src, XGMessenger dest)
	{	try
		{	int hi, mid, lo = this.min.getLo();
			for(hi = this.min.getHi(); hi <= this.max.getHi(); hi++)
				for(mid = this.min.getMid(); mid <= this.max.getMid(); mid++)
				{	XGRequest r = new XGMessageDumpRequest(src, new XGAdress(hi, mid, lo));
					r.setDestination(dest);
					src.take(r.request());
				}
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
		}
		catch(TimeoutException e)
		{	log.info(e.getMessage());
		}
	}

	public int maxInstanceCount()
	{	try
		{	return this.max.getMid() - this.min.getMid();
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return 0;
		}
	}
}