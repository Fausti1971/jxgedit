package obj;

public class XGAdress
{	private final int hi, mid, lo;

	public XGAdress(int hi, int mid, int lo)
	{	this.hi = hi;
		this.mid = mid;
		this.lo = lo;
	}

	public int getHi()
	{	return hi;}

	public int getMid()
	{	return mid;}

	public int getLo()
	{	return lo;}

	@Override public String toString()
	{	return "(" + getHi() + "/" + getMid() + "/" + getLo() + ")";}
}
