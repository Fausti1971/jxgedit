package adress;

public class XGAdress implements XGAdressConstants, Comparable<XGAdress>
{	private final XGAdressField hi, mid, lo;

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

	public XGAdress(int hi, int mid)
	{	this.hi = new XGAdressField(hi);
		this.mid = new XGAdressField(mid);
		this.lo = new XGAdressField();
	}

	public XGAdress(int hi)
	{	this.hi = new XGAdressField(hi);
		this.mid = new XGAdressField();
		this.lo = new XGAdressField();
	}

	public XGAdress(XGAdressField h, XGAdressField m, XGAdressField l)
	{	if(h != null) this.hi = h;
		else this.hi = new XGAdressField();
		if(m != null) this.mid = m;
		else this.mid = new XGAdressField();
		if(l != null) this.lo = l;
		else this.lo = new XGAdressField();
	}

	public int getHi() throws InvalidXGAdressException
	{	return this.hi.getValue();}

	public int getMid() throws InvalidXGAdressException
	{	return this.mid.getValue();}

	public int getLo() throws InvalidXGAdressException
	{	return this.lo.getValue();}

	public boolean isValueAdress()
	{	return this.isHiValdi() && this.isMidValdi() && this.isLoValdi();}

	private boolean isHiValdi()
	{	return this.hi.isValid();}

	private boolean isMidValdi()
	{	return this.mid.isValid();}

	private boolean isLoValdi()
	{	return this.lo.isValid();}

	public XGAdress complement(XGAdress adr) throws InvalidXGAdressException
	{	if(this.isValueAdress()) return this;
		return new XGAdress(this.hi.complement(adr.hi), this.mid.complement(adr.mid), this.lo.complement(adr.lo));
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
		if(this.isHiValdi() && o.isHiValdi()) temp = this.hi.compare(o.hi);
		if(temp == 0 && this.isMidValdi() && o.isMidValdi()) temp = this.mid.compare(o.mid);
		if(temp == 0 && this.isLoValdi() && o.isLoValdi()) temp = this.lo.compare(o.lo);
		return temp;
	}
}
