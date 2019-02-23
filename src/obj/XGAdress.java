package obj;

public class XGAdress
{	private int hi, mid, lo;

	public XGAdress(int hi, int mid, int lo)
	{	this.setHi(hi);
		this.setMid(mid);
		this.setLo(lo);
	}

	public XGAdress getXGAdress()
	{	return this;
	}

	public int getHi()
	{	return hi;
	}

	public void setHi(int hi)
	{	this.hi = hi;
	}

	public int getMid()
	{	return mid;
	}

	public void setMid(int mid)
	{	this.mid = mid;
	}

	public int getLo()
	{	return lo;
	}

	public void setLo(int lo)
	{	this.lo = lo;
	}

	@Override public boolean equals(Object o)
	{	if(o instanceof XGAdress)
		{	XGAdress x = (XGAdress)o;
			return(x.getHi() == hi && x.getMid() == mid && x.getLo() == lo);
		}
		return false;
	}

	@Override public String toString()
	{	return "(" + getHi() + "/" + getMid() + "/" + getLo() + ")";
	}

	@Override public int hashCode()
	{	return (this.hi << 16) | (this.mid << 8) | this.mid;
	}
}
