package adress;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import application.XGLoggable;

public class XGAddress implements XGLoggable, XGAddressConstants, Comparable<XGAddress>, XGAddressable, Iterable<XGAddress>
{
	private final XGAddressField hi, mid, lo;

	public XGAddress(String hi, String mid, String lo)
	{	this.hi = new XGAddressField(hi, DEF_ADDRESSFILED);
		this.mid = new XGAddressField(mid, DEF_ADDRESSFILED);
		this.lo = new XGAddressField(lo, DEF_ADDRESSFILED);
	}

	public XGAddress(int hi, int mid, int lo)
	{	this.hi = new XGAddressField(hi);
		this.mid = new XGAddressField(mid);
		this.lo = new XGAddressField(lo);
	}

	public XGAddress(XGAddressField h, XGAddressField m, XGAddressField l)
	{	this.hi = h;
		this.mid = m;
		this.lo = l;
	}
/**
 * Erzeugt eine XGAddress aus den Slash-Tokens des übergebenen Strings mit der Option, die im String fehlenden Tokens durch die Felder der übergebenen XGAddress zu ersetzen.
 * @param adr	optionale XGAddresse aus der fehlende Attribute ersetzt werden 
 * @param n		XMLNode, aus deren Attributen hi, mid und lo eine neue XGAddress erzeugt wird
 */
	public XGAddress(String s, XGAddress adr)
	{	XGAddressField h = DEF_ADDRESSFILED, m = DEF_ADDRESSFILED, l = DEF_ADDRESSFILED;
		if(adr != null)
		{	try
			{	h = h.complement(adr.getHi());
				m = m.complement(adr.getMid());
				l = l.complement(adr.getLo());
			}
			catch(InvalidXGAddressException e)
			{	LOG.severe(e.getMessage());
			}
		}
//		StringTokenizer t = new StringTokenizer(s, "/");//Beachte: leere Tokens sind keine Tokens!
		if(s != null && s.matches(REGEX_ADDRESS))
		{	int firstSlash = s.indexOf("/");
			int lastSlash = s.lastIndexOf("/");
			this.hi = new XGAddressField(s.substring(0, firstSlash), h);
			this.mid = new XGAddressField(s.substring(firstSlash + 1, lastSlash), m);
			this.lo = new XGAddressField(s.substring(lastSlash + 1, s.length()), l);
		}
		else
		{	this.hi = h;
			this.mid = m;
			this.lo = l;
		}
	}

/**
 * extrahiert und returniert das Hi-Field der XGAdress this
 * @return Wert des Fields als int
 * @throws InvalidXGAddressException falls das Field variabel ist
 */
	public XGAddressField getHi()
	{	return this.hi;
	}

	/**
	 * extrahiert und returniert das Mid-Field der XGAdress this
	 * @return Wert des Fields als int
	 * @throws InvalidXGAddressException falls das Field eine Range ist
	 */
	public XGAddressField getMid()
	{	return this.mid;
	}

	/**
	 * extrahiert und returniert das Lo-Field der XGAdress this
	 * @return Wert des Fields als int
	 * @throws InvalidXGAddressException falls das Field eine Range ist
	 */
	public XGAddressField getLo()
	{	return this.lo;
	}

/**
 * testet, ob die Adresse vollständig ist, und somit einen XGValue adressieren kann
 * @return true, wenn alle Fields fix sind
 */
	public boolean isFixed()
	{	return this.hi.isFix() && this.mid.isFix() && this.lo.isFix();
	}

/**
 * komplettiert this mittels adr, indem variable Fields durch diese aus adr möglichst konkretisiert werden 
 * @param adr Adresse mittels derer this konkretisiert werden soll
 * @return  die erfolgreich komplettierte Adresse
 * @throws InvalidXGAddressException 
 */
	public XGAddress complement(XGAddress adr) throws InvalidXGAddressException
	{	//		if(this.isFixed()) return this;
		//		if(adr.isFixed()) return adr;
		try
		{	return new XGAddress(this.hi.complement(adr.hi), this.mid.complement(adr.mid), this.lo.complement(adr.lo));
		}
		catch(InvalidXGAddressException e)
		{	throw new InvalidXGAddressException(e.getMessage() + " within address: " + this + " and " + adr);
		}
	}

/**
 * testet, ob alle Felder von adr in allen Feldern von this enthalten sind
 * @param adr Adresse gegen deren Felder getestet wird
 * @return true, wenn alle Felder ineinander enthalten sind
 */
	public boolean contains(XGAddress adr)
	{	if(this.hi.contains(adr.hi) && this.mid.contains(adr.mid) && this.lo.contains(adr.lo)) return true;
		return false;
	}

/**
 * testet alle Felder, inklusive ihrer Validität auf Gleichheit
 */
	@Override public boolean equals(Object obj)
	{	if(!(obj instanceof XGAddress)) return false;
		XGAddress adr = (XGAddress)obj;
		return this.hi.equals(adr.hi) && this.mid.equals(adr.mid) && this.lo.equals(adr.lo);
	}

/**
 * Stringrepresäntation einer Adresse
 */
	@Override public String toString()
	{	return "(" + this.hi + "/" + this.mid + "/" + this.lo + ")";
	}

	@Override public int compareTo(XGAddress o)
	{	int temp = this.hi.compareTo(o.hi);
		if(temp == 0) temp = this.mid.compareTo(o.mid);
		if(temp == 0) temp = this.lo.compareTo(o.lo);
		return temp;
	}

	@Override public XGAddress getAddress()
	{	return this;
	}

	@Override public Iterator<XGAddress> iterator()
	{	Set<XGAddress> set = new LinkedHashSet<>();
		for(int hi : this.hi)
			for(int mid : this.mid)
				for(int lo : this.lo)
					set.add(new XGAddress(hi, mid, lo));
		return set.iterator();
	}
}
