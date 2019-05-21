package adress;

public class XGAdress implements XGAdressConstants
{	private final int hi, mid, lo;
	private final int mask;

	public XGAdress(String hi, String mid, String lo)
	{	int temp = 0;

		if(hi != null)
		{	this.hi = Integer.parseInt(hi);
			temp |= ADR_HI;
		}
		else this.hi = 0;

		if(mid != null)
		{	this.mid = Integer.parseInt(mid);
			temp |= ADR_MID;
		}
		else this.mid = 0;

		if(lo != null)
		{	this.lo = Integer.parseInt(lo);
			temp |= ADR_LO;
		}
		else this.lo = 0;
		this.mask = temp;
	}

	public XGAdress(int hi, int mid, int lo)
	{	this.hi = hi;
		this.mid = mid;
		this.lo = lo;
		this.mask = ADR_HI | ADR_MID | ADR_LO;
	}

	public XGAdress(int hi, int mid)
	{	this.hi = hi;
		this.mid = mid;
		this.lo = 0;
		this.mask = ADR_HI | ADR_MID;
	}

	public XGAdress(int hi)
	{	this.hi = hi;
		this.mid = 0;
		this.lo = 0;
		this.mask = ADR_HI;
	}

	public int getHi() throws InvalidXGAdressException
	{	if(this.isHiValdi()) return this.hi;
		else throw new InvalidXGAdressException("acces to ivalid HI-Adress");
	}

	public int getMid() throws InvalidXGAdressException
	{	if(this.isMidValdi()) return this.mid;
		else throw new InvalidXGAdressException("access to invalid MID-Adress");
	}

	public int getLo() throws InvalidXGAdressException
	{	if(this.isLoValdi()) return this.lo;
		else throw new InvalidXGAdressException("access to invalid LO-Adress");
	}

	public boolean isHiValdi()
	{	return (this.mask & ADR_HI) != 0;}

	public boolean isMidValdi()
	{	return (this.mask & ADR_MID) != 0;}

	public boolean isLoValdi()
	{	return (this.mask & ADR_LO) != 0;}

	public boolean equalsValidFields(XGAdress adr)
	{	if(this.isHiValdi() && adr.isHiValdi())
			if(this.hi != adr.hi) return false;
		if(this.isMidValdi() && adr.isMidValdi())
			if(this.mid != adr.mid) return false;
		if(this.isLoValdi() && adr.isLoValdi())
			if(this.lo != adr.lo) return false;
		return true;
	}

	@Override
	public boolean equals(Object obj)
	{	if(!(obj instanceof XGAdress)) return false;
		XGAdress a = (XGAdress)obj;
		return(a.mask == this.mask && a.hi == this.hi && a.mid == this.mid && a.lo == this.lo);
	}

	@Override public String toString()
	{	String h = "-", m = "-", l = "-";
		if(this.isHiValdi()) h = "" + this.hi;
		if(this.isMidValdi()) m = "" + this.mid;
		if(this.isLoValdi()) l = "" + this.lo;
		return "(" + h + "/" + m + "/" + l + ")";
	}
}
