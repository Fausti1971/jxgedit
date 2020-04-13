package adress;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import xml.XMLNode;

public class XGAddress implements XGAddressConstants, Comparable<XGAddress>, XGAddressable, Iterable<XGAddress>
{	private static Logger log =Logger.getAnonymousLogger();

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
 * Erzeugt eine XGAddress aus den hi-, mid- und lo-Attributen der übergebenen XMLNode mit der Option, die in der XMLNode fehlenden Attribute durch die Felder der übergebenen XGAddress zu ersetzen.
 * @param adr	optionale XGAddresse aus der fehlende Attribute ersetzt werden 
 * @param n		XMLNode, aus deren Attributen hi, mid und lo eine neue XGAddress erzeugt wird
 */
	public XGAddress(XGAddress adr, XMLNode n)
	{	XGAddressField h = DEF_ADDRESSFILED, m = DEF_ADDRESSFILED, l = DEF_ADDRESSFILED;
		if(adr != null)
		{	h = adr.getHi();
			m = adr.getMid();
			l = adr.getLo();
		}
		this.hi = new XGAddressField(n.getStringAttribute(ATTR_HI), h);
		this.mid = new XGAddressField(n.getStringAttribute(ATTR_MID), m);
		this.lo = new XGAddressField(n.getStringAttribute(ATTR_LO), l);
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
	 * @throws InvalidXGAddressException falls das Field invalid ist
	 */
	public XGAddressField getMid()
	{	return this.mid;
	}

	/**
	 * extrahiert und returniert das Lo-Field der XGAdress this
	 * @return Wert des Fields als int
	 * @throws InvalidXGAddressException falls das Field invalid ist
	 */
	public XGAddressField getLo()
	{	return this.lo;
	}

/**
 * testet, ob die Adresse vollständig ist, und somit einen XGValue adressieren kann
 * @return true, wenn alle Fields valide sind
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
	public XGAddress complement(XGAddress adr)
	{	if(this.isFixed()) return this;
		if(adr.isFixed()) return adr;
		return new XGAddress(this.hi.complement(adr.hi), this.mid.complement(adr.mid), this.lo.complement(adr.lo));
	}

/**
 * testet, ob fixe Felder gleich sind bzw. variable Felder eine gemeinsame Teilmenge haben
 * @param adr Adresse gegen deren Felder getestet wird
 * @return true, wenn alle Felder ineinander enthalten sind
 */
	public boolean contains(XGAddress adr)
	{	if(!(this.hi.contains(adr.hi))) return false;
		if(!(this.mid.contains(adr.mid))) return false;
		if(!(this.lo.contains(adr.lo))) return false;
		return true;
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
