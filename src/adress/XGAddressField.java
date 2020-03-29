package adress;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * eines der drei Felder einer XGAddress, kann fix, und variabel (Range) sein, was dem einstmaligen Status "invalid" entspricht
 * @author thomas
 *
 */
public class XGAddressField implements XGAddressConstants, Comparable<XGAddressField>, Iterable<Integer>
{
	private static String EXCEPTIONTEXT = "access to variable XGAdressField: ";

/******************************************************************************************************************/

	private final int min, max;

	public XGAddressField(int v)
	{	this(v, v);
	}

	public XGAddressField(int vMin, int vMax)
	{	this.min = vMin;
		this.max = vMax;
	}

	public XGAddressField(String v)
	{	if(v == null)
		{	this.min = DEF_ADDRESSFILED.min;
			this.max = DEF_ADDRESSFILED.max;
		}
		else
		{	String vMin, vMax;
			StringTokenizer t = new StringTokenizer(v, "-");
			vMin = vMax = t.nextToken();
			while(t.hasMoreTokens()) vMax = t.nextToken();
			this.min = Integer.parseInt(vMin);
			this.max = Integer.parseInt(vMax);
		}
	}

/**
 * 
 * @return true, wenn sowohl min und max definiert als auch gleich sind
 */
	public boolean isFix()
	{	return this.min == this.max ;
	}

/**
 * negiertes isFix(); nur aus Lesbarkeitsgründen
 */
	public boolean isRange()
	{	return this.min != this.max ;
	}

/**
 * 
 * @return einen eventuellen festen Wert
 * @throws InvalidXGAddressException wenn der Wert variabel (Range) ist
 */
	public int getValue() throws InvalidXGAddressException
	{	if(this.isFix()) return this.min;
		else throw new InvalidXGAddressException(EXCEPTIONTEXT + this);
	}

/**
 * versucht die Verschmelzung zweier Addressfelder zu einem möglichst konkreten Wert
 * zwei Ranges ergeben eine eventuelle gemeinsame Teilmenge, ansonsten eine Exception
 * zwei fixe, gleiche Werte ergeben eben diesen, ansonsten eine Exception
 * @param f	das Addressfeld, durch das this komplettiert werden soll
 * @return ein neues Addressfeld als Komplement
 * @throws InvalidXGAddressException falls die variablen Werte keine Schnittmenge haben, oder fixe Werte verschieden sind
 */
	public XGAddressField complement(XGAddressField f) throws InvalidXGAddressException
	{
		if(f.isFix())
		{	if(this.isFix())
			{	if(this.equals(f)) return f;
			}
			else
				if(this.contains(f)) return f;
		}
		else
		{	if(this.isFix())
			{	if(f.contains(this)) return this;
			}
			else
				if(this.contains(f)) return this.intersection(f);
		}
		throw new InvalidXGAddressException(EXCEPTIONTEXT + this + " and " + f);
	}

/**
 * versucht festzustellen, ob ein definiertes, festes f in einem definierten this enthalten ist
 * @param f	Addressfeld
 * @return	true, true, wenn eines oder beide Felder variabel sind und eine gemeinsame Teilmenge haben (bei fixen Werten, wenn gleich)
 */
	public boolean contains(XGAddressField f)
	{	return this.min <= f.max && this.max >= f.min;
	}

/**
 * ermittelt und returniert eine eventuelle gemeinsame Teilmenge
 * @param f
 * @return die eventuell vorhandene Teilmenge von this und f
 * @throws InvalidXGAddressException falls keine solche existiert
 */
	public XGAddressField intersection(XGAddressField f) throws InvalidXGAddressException
	{	if(this.contains(f) || f.contains(this)) return new XGAddressField(Math.max(this.min, f.min), Math.min(this.max, f.max));
		throw new InvalidXGAddressException("no intersection of " + this + " and " + f);
	}

	@Override public boolean equals(Object o)
	{	if(!(o instanceof XGAddressField)) return false;
		XGAddressField a = (XGAddressField)o;
		return this.min == a.min && this.max == a.max;
	}

	@Override public String toString()
	{	return this.min + "-" + this.max;
	}

	@Override public int compareTo(XGAddressField o)
	{	int res = Integer.compare(this.min, o.min);
		if(res == 0) Integer.compare(this.max, o.max);
		return res;
	}

	@Override public Iterator<Integer> iterator()
	{	Set<Integer> set = new LinkedHashSet<>();
		for(int i = this.min; i <= this.max; i++) set.add(i);
		return set.iterator();
	}
}
