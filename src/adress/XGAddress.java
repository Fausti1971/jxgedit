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
	{	this.hi = new XGAddressField(hi);
		this.mid = new XGAddressField(mid);
		this.lo = new XGAddressField(lo);
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

	public XGAddress(XMLNode item)
	{	this.hi = new XGAddressField(item.getStringAttribute(ATTR_HI));
		this.mid = new XGAddressField(item.getStringAttribute(ATTR_MID));
		this.lo = new XGAddressField(item.getStringAttribute(ATTR_LO));
	}
/**
 * extrahiert und returniert das Hi-Field der XGAdress this
 * @return Wert des Fields als int
 * @throws InvalidXGAddressException falls das Field variabel ist
 */
	public int getHi() throws InvalidXGAddressException
	{	return this.hi.getValue();
	}

	/**
	 * extrahiert und returniert das Mid-Field der XGAdress this
	 * @return Wert des Fields als int
	 * @throws InvalidXGAddressException falls das Field invalid ist
	 */
	public int getMid() throws InvalidXGAddressException
	{	return this.mid.getValue();
	}

	/**
	 * extrahiert und returniert das Lo-Field der XGAdress this
	 * @return Wert des Fields als int
	 * @throws InvalidXGAddressException falls das Field invalid ist
	 */
	public int getLo() throws InvalidXGAddressException
	{	return this.lo.getValue();
	}

/**
 * testet, ob die Adresse vollständig ist, und somit einen XGValue adressieren kann
 * @return true, wenn alle Fields valide sind
 */
	public boolean isFixedAddress()
	{	return this.hi.isFix() && this.mid.isFix() && this.lo.isFix();
	}

/**
 * komplettiert this mittels adr, indem invalide Fields durch diese aus adr ersetzt werden 
 * @param adr Adresse mittels this ergänzt werden soll
 * @return  die erfolgreich komplettierte Adresse
 * @throws InvalidXGAddressException 
 */
	public XGAddress complement(XGAddress adr) throws InvalidXGAddressException
	{	if(this.isFixedAddress()) return this;
		if(adr.isFixedAddress()) return adr;
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
