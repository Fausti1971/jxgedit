package adress;

public class XGAdress implements XGAdressConstants, Comparable<XGAdress>
{	private final XGAdressField hi, mid, lo;

	public XGAdress(String hi, String mid, String lo)
	{	this.hi = new XGAdressField(hi, null);
		this.mid = new XGAdressField(mid, null);
		this.lo = new XGAdressField(lo, null);
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

	public boolean equalsMaskedValidFields(XGAdress adr)
	{	if(!(this.hi.equalsMaskedValid(adr.hi))) return false;
		if(!(this.mid.equalsMaskedValid(adr.mid))) return false;
		if(!(this.lo.equalsMaskedValid(adr.lo))) return false;
		return true;
	}

	@Override public boolean equals(Object obj)
	{	if(!(obj instanceof XGAdress)) return false;
		try
		{	return this.hi.equals(((XGAdress)obj).hi) && this.mid.equals(((XGAdress)obj).mid) && this.lo.equals(((XGAdress)obj).lo);
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
