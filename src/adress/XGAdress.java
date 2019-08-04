package adress;

import java.util.logging.Logger;
import org.w3c.dom.Node;
import application.Rest;
public class XGAdress implements XGAdressConstants, Comparable<XGAdress>, XGAdressable
{	private static Logger log =Logger.getAnonymousLogger();

	private final XGAdressField hi, mid, lo;

	public XGAdress(String hi, String mid, String lo)
	{	this.hi = new XGAdressField(hi);
		this.mid = new XGAdressField(mid);
		this.lo = new XGAdressField(lo);
	}

	public XGAdress(int hi, int mid, int lo)
	{	this.hi = new XGAdressField(hi);
		this.mid = new XGAdressField(mid);
		this.lo = new XGAdressField(lo);
	}

	public XGAdress(XGAdressField h, XGAdressField m, XGAdressField l)
	{	if(h != null) this.hi = h;
		else this.hi = new XGAdressField();
		if(m != null) this.mid = m;
		else this.mid = new XGAdressField();
		if(l != null) this.lo = l;
		else this.lo = new XGAdressField();
	}

	public XGAdress(Node item)
	{	this.hi = new XGAdressField(Rest.getFirstNodeChildTextContentByTagAsString(item, TAG_HI));
		this.mid = new XGAdressField(Rest.getFirstNodeChildTextContentByTagAsString(item, TAG_MID));
		this.lo = new XGAdressField(Rest.getFirstNodeChildTextContentByTagAsString(item, TAG_LO));
	}

	public int getHi() throws InvalidXGAdressException
	{	return this.hi.getValue();}

	public int getMid() throws InvalidXGAdressException
	{	return this.mid.getValue();}

	public int getLo() throws InvalidXGAdressException
	{	return this.lo.getValue();}

	public boolean isValueAdress()
	{	return this.isHiValid() && this.isMidValid() && this.isLoValid();}

	public boolean isHiValid()
	{	return this.hi.isValid();}

	public boolean isMidValid()
	{	return this.mid.isValid();}

	public boolean isLoValid()
	{	return this.lo.isValid();}

	public XGAdress complement(XGAdress adr)
	{	if(this.isValueAdress()) return this;
		try
		{	return new XGAdress(this.hi.complement(adr.hi), this.mid.complement(adr.mid), this.lo.complement(adr.lo));
		}
		catch(InvalidXGAdressException e)
		{	log.info("can't complement adress " + this + " with " + adr);
			return this;
		}
	}

	public boolean equalsValidFields(XGAdress adr)
	{	if(!(this.hi.equalsValid(adr.hi))) return false;
		if(!(this.mid.equalsValid(adr.mid))) return false;
		if(!(this.lo.equalsValid(adr.lo))) return false;
		return true;
	}

	@Override public boolean equals(Object obj)
	{	if(!(obj instanceof XGAdress)) return false;
		XGAdress adr = (XGAdress)obj;
		try
		{	return this.hi.equals(adr.hi) && this.mid.equals(adr.mid) && this.lo.equals(adr.lo);
		}
		catch(NullPointerException e)
		{	e.printStackTrace();
			return false;
		}
	}

	@Override public String toString()
	{	return "(" + this.hi + "/" + this.mid + "/" + this.lo + ")";}

	public int compareTo(XGAdress o)
	{	int temp = 0;
		if(this.isHiValid() && o.isHiValid()) temp = this.hi.compare(o.hi);
		if(temp == 0 && this.isMidValid() && o.isMidValid()) temp = this.mid.compare(o.mid);
		if(temp == 0 && this.isLoValid() && o.isLoValid()) temp = this.lo.compare(o.lo);
		return temp;
	}

	public XGAdress getAdress()
	{	return this;}

	public String getInfo()
	{	return this.getClass().getSimpleName() + " " + this.toString();}
}
