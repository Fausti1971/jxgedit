package adress;

public class XGAddress extends XGAddressRange implements Comparable<XGAddress>, XGAddressable
{
	public XGAddress(int hi,int mid,int lo)
	{	super(hi,mid,lo);
	}

	public int getHiValue(){	return this.hi.getMin();}
	public int getMidValue(){	return this.mid.getMin();}
	public int getLoValue(){	return this.lo.getMin();}

	@Override public int compareTo(XGAddress o)
	{	int temp = this.hi.compareTo(o.hi);
		if(temp == 0) temp = this.mid.compareTo(o.mid);
		if(temp == 0) temp = this.lo.compareTo(o.lo);
		return temp;
	}

	@Override public XGAddress getAddress(){	return this;}

}
