package adress;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * eines der drei Felder einer XGAddress, kann fix, und variabel (Range) sein, was dem einstmaligen Status "invalid" entspricht
 * @author thomas
 *
 */
public class XGAddressField implements XGAddressConstants, Comparable<XGAddressField>, Iterable<Integer>
{
	private static final Logger log = Logger.getAnonymousLogger();

/******************************************************************************************************************/

	private final int min, max, mask;

	public XGAddressField(int v)
	{	this(v, v, DEF_MASK);
	}

	public XGAddressField(int vMin, int vMax, int mask)
	{	this.min = vMin;
		this.max = vMax;
		this.mask = mask;
	}

	public XGAddressField(String v, XGAddressField def)
	{	if(v == null)
		{	this.min = def.min;
			this.max = def.max;
			this.mask = def.mask;
		}
		else
		{	String vMin, vMax;
			StringTokenizer t = new StringTokenizer(v, "-");
			vMin = vMax = t.nextToken();
			while(t.hasMoreTokens()) vMax = t.nextToken();
			this.min = Integer.parseInt(vMin);
			this.max = Integer.parseInt(vMax);
			this.mask = DEF_MASK;
		}
	}

/**
 * 
 * @return true, wenn sowohl min und max definiert als auch gleich sind
 */
	public boolean isFix()
	{	return this.getMin() == this.getMax();
	}

/**
 * negiertes isFix(); nur aus Lesbarkeitsgründen
 */
	public boolean isRange()
	{	return this.getMin() != this.getMax() ;
	}

	public int getMin()
	{	return this.min & this.mask;
	}

	public int getMax()
	{	return this.max & this.mask;
	}

	public int getMask()
	{	return this.mask;
	}

	public int getSize()
	{	return (this.max - this.min) + 1;
	}

/**
 * 
 * @return einen eventuellen festen Wert
 * @throws InvalidXGAddressException wenn der Wert variabel (Range) ist
 */
	public int getValue() throws InvalidXGAddressException
	{	if(this.isFix()) return this.getMin();
		else throw new InvalidXGAddressException("acces to variable addressfield: " + this);
	}

/**
 * versucht die Kastration von this durch f zu einem möglichst konkreten Wert;
 * eine Range this wird durch eine Range f auf die Schnittmenge beider reduziert;
 * ein fixes this wird unverändert returniert;
 * ein fixes f wird unver
 * @param f	das Addressfeld, durch das this komplettiert werden soll
 * @return ein neues Addressfeld als Komplement
  */
	public XGAddressField complement(final XGAddressField f)
	{	if(this.isFix()) return this;
		if(f.isFix()) return f;
		return this.intersection(f);
	}

/**
 * testet, ob Field f eine Teilmenge von Field this ist
 * @param f	Addressfeld
 * @return	true, wenn Field f eine Teilmenge von this ist
 */
	public boolean contains(XGAddressField f)
	{	return this.getMin() <= f.getMin() && this.getMax() >= f.getMax();
	}

/**
 * ermittelt und returniert eine eventuelle gemeinsame Teilmenge
 * @param f
 * @return die eventuell vorhandene Teilmenge von this und f
  */
	public XGAddressField intersection(XGAddressField f)
	{	return new XGAddressField(Math.max(this.min, f.min), Math.min(this.max, f.max), this.mask);
	}

	@Override public boolean equals(Object o)
	{	if(!(o instanceof XGAddressField)) return false;
		XGAddressField a = (XGAddressField)o;
		return this.min == a.min && this.max == a.max && this.mask == a.mask;
	}

	@Override public String toString()
	{	if(this.isRange()) return this.getMin() + "-" + this.getMax();
		return "" + this.getMin();
	}

	@Override public int compareTo(XGAddressField o)
	{	int res = Integer.compare(this.min, o.min);
		if(res == 0) res = Integer.compare(this.max, o.max);
		if(res == 0) res = Integer.compare(this.mask, o.mask);
		return res;
	}

	@Override public Iterator<Integer> iterator()
	{	Set<Integer> set = new LinkedHashSet<>();
		for(int i = this.min; i <= this.max; i++) set.add(i);
		return set.iterator();
	}
}
