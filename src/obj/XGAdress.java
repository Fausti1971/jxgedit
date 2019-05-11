package obj;

import application.InvalidXGAdressException;

public class XGAdress
{	private final int hi, mid, lo;
	private final boolean hiIsValid, midIsValid, loIsValid;

	public XGAdress(int hi, int mid, int lo)
	{	this.hi = hi;
		this.mid = mid;
		this.lo = lo;
		this.hiIsValid = this.midIsValid = this.loIsValid = true;
	}

	public XGAdress(int hi, int mid)
	{	this.hi = hi;
		this.mid = mid;
		this.lo = 0;
		this.hiIsValid = this.midIsValid = true;
		this.loIsValid = false;
	}

	public XGAdress(int hi)
	{	this.hi = hi;
		this.mid = 0;
		this.lo = 0;
		this.hiIsValid = true;
		this.midIsValid = this.loIsValid = false;
	}

	public int getHi() throws InvalidXGAdressException
	{	if(hiIsValid) return hi;
		else throw new InvalidXGAdressException("acces to ivalid HI-Adress");
	}

	public int getMid() throws InvalidXGAdressException
	{	if(midIsValid) return mid;
		else throw new InvalidXGAdressException("access to invalid MID-Adress");
	}

	public int getLo() throws InvalidXGAdressException
	{	if(loIsValid) return lo;
		else throw new InvalidXGAdressException("access to invalid LO-Adress");
	}

	public boolean isPartOf(XGAdress adr)
	{	boolean result = false;
		if(adr.hiIsValid && this.hiIsValid && adr.hi == this.hi) result = true;
		else return adr.hiIsValid == this.hiIsValid;
		if(adr.midIsValid && this.midIsValid && adr.mid == this.mid) result = true;
		else return adr.midIsValid == this.midIsValid;
		if(adr.loIsValid && this.loIsValid && adr.lo == this.lo) result = true;
		else return adr.loIsValid == this.loIsValid;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{	if(!(obj instanceof XGAdress)) return false;
		XGAdress a = (XGAdress)obj;
		try
		{	return(a.getHi() == this.getHi() && a.getMid() == this.getMid() && a.getLo() == this.getLo());}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return false;
		}
	}

	@Override public String toString()
	{	String h = "--", m = "--", l = "--";
		try
		{	h = "" + getHi();
			m = "" + getMid();
			l = "" + getLo();
		}
		catch(Exception e)
		{	//e.printStackTrace();
			//return "(" + h + "/" + m + "/" + l + ")";
		}
		return "(" + h + "/" + m + "/" + l + ")";
	}
}
