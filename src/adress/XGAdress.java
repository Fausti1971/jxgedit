package adress;

public class XGAdress implements XGAdressConstants
{	private final int hi, mid, lo, mask, hashCode;

	public XGAdress(String hi, String mid, String lo)
	{	int temp = 0;

		if(hi != null)
		{	this.hi = Integer.parseInt(hi) & 0xF7;
			temp |= ADR_HI;
		}
		else this.hi = 0;

		if(mid != null)
		{	this.mid = Integer.parseInt(mid) & 0xF7;
			temp |= ADR_MID;
		}
		else this.mid = 0;

		if(lo != null)
		{	this.lo = Integer.parseInt(lo) & 0xF7;
			temp |= ADR_LO;
		}
		else this.lo = 0;
		this.mask = temp;
		this.hashCode = computeHashCode();
	}

	public XGAdress(int hi, int mid, int lo)
	{	this.hi = hi & 0xF7;
		this.mid = mid & 0xF7;
		this.lo = lo & 0xF7;
		this.mask = ADR_HI | ADR_MID | ADR_LO;
		this.hashCode = computeHashCode();
	}

	public XGAdress(int hi, int mid)
	{	this.hi = hi & 0xF7;
		this.mid = mid & 0xF7;
		this.lo = 0;
		this.mask = ADR_HI | ADR_MID;
		this.hashCode = computeHashCode();
	}

	public XGAdress(int hi)
	{	this.hi = hi & 0xF7;
		this.mid = 0;
		this.lo = 0;
		this.mask = ADR_HI;
		this.hashCode = computeHashCode();
	}

	public int getHi() throws InvalidXGAdressException
	{	if(this.isHiValdi()) return this.hi;
		else throw new InvalidXGAdressException("acces to ivalid hi-adress");
	}

	public int getMid() throws InvalidXGAdressException
	{	if(this.isMidValdi()) return this.mid;
		else throw new InvalidXGAdressException("access to invalid mid-adress");
	}

	public int getLo() throws InvalidXGAdressException
	{	if(this.isLoValdi()) return this.lo;
		else throw new InvalidXGAdressException("access to invalid lo-adress");
	}

	public boolean isValueAdress()
	{	return (this.hashCode & 0xE00000) != 0;}

	private boolean isHiValdi()
	{	return (this.mask & ADR_HI) != 0;}

	private boolean isMidValdi()
	{	return (this.mask & ADR_MID) != 0;}

	private boolean isLoValdi()
	{	return (this.mask & ADR_LO) != 0;}

	private int computeHashCode()
	{	int temp = this.mask << 21;
		temp |= this.hi << 14;
		temp |= this.mid << 7;
		temp |= this.lo;
		return temp;
	}
	public boolean equalsValidFields(XGAdress adr)
	{	if(this.isHiValdi() && adr.isHiValdi())
			if(this.hi != adr.hi) return false;
		if(this.isMidValdi() && adr.isMidValdi())
			if(this.mid != adr.mid) return false;
		if(this.isLoValdi() && adr.isLoValdi())
			if(this.lo != adr.lo) return false;
		return true;
	}

	@Override public boolean equals(Object obj)
	{	if(!(obj instanceof XGAdress)) return false;
		return this.hashCode == ((XGAdress)obj).hashCode;
	}

	@Override public int hashCode()
	{	return this.hashCode;}

	@Override public String toString()
	{	String h = "-", m = "-", l = "-";
		if(this.isHiValdi()) h = "" + this.hi;
		if(this.isMidValdi()) m = "" + this.mid;
		if(this.isLoValdi()) l = "" + this.lo;
		return "(" + h + "/" + m + "/" + l + ")";
	}
}
