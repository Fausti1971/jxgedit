package adress;

import java.util.logging.Logger;
import xml.XMLNode;
public class XGAddress implements XGAddressConstants, Comparable<XGAddress>, XGAddressable
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
	{	if(h != null) this.hi = h;
		else this.hi = new XGAddressField();
		if(m != null) this.mid = m;
		else this.mid = new XGAddressField();
		if(l != null) this.lo = l;
		else this.lo = new XGAddressField();
	}

	public XGAddress(XMLNode item)
	{	this.hi = new XGAddressField(item.getIntegerAttribute(ATTR_HI));
		this.mid = new XGAddressField(item.getIntegerAttribute(ATTR_MID));
		this.lo = new XGAddressField(item.getIntegerAttribute(ATTR_LO));
	}
/**
 * extrahiert und returniert das Hi-Field der XGAdress this
 * @return Wert des Fields als int
 * @throws InvalidXGAddressException falls das Field invalid ist
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
	{	return this.mid.getValue();}

	/**
	 * extrahiert und returniert das Lo-Field der XGAdress this
	 * @return Wert des Fields als int
	 * @throws InvalidXGAddressException falls das Field invalid ist
	 */
	public int getLo() throws InvalidXGAddressException
	{	return this.lo.getValue();}

/**
 * testet, ob die Adresse vollständig ist, und somit einen XGValue adressieren kann
 * @return true, wenn alle Fields valide sind
 */
	public boolean isValidAdress()
	{	return this.isHiValid() && this.isMidValid() && this.isLoValid();}

/**
 * testet das Hi-Field auf Validität
 * @return true, wenn Field valide
 */
	public boolean isHiValid()
	{	return this.hi.isValid();}

	/**
	 * testet das Mid-Field auf Validität
	 * @return true, wenn Field valide
	 */
	public boolean isMidValid()
	{	return this.mid.isValid();}

	/**
	 * testet das Lo-Field auf Validität
	 * @return true, wenn Field valide
	 */
	public boolean isLoValid()
	{	return this.lo.isValid();}

/**
 * komplettiert this mittels adr, indem invalide Fields durch diese aus adr ersetzt werden 
 * @param adr Adresse mittels this ergänzt werden soll
 * @return  die erfolgreich komplettierte Adresse
 * @throws InvalidXGAddressException 
 */
	public XGAddress complement(XGAddress adr) throws InvalidXGAddressException
	{	if(this.isValidAdress()) return this;
		if(adr.isValidAdress()) return adr;
		return new XGAddress(this.hi.complement(adr.hi), this.mid.complement(adr.mid), this.lo.complement(adr.lo));
	}

/**
 * testet ausschließlich valide Felder von this mit validen Feldern von adr
 * @param adr Adresse gegen deren valide Felder getestet wird
 * @return true, wenn alle validen Felder gleich sind
 */
	public boolean equalsValidFields(XGAddress adr)
	{	if(!(this.hi.equalsValid(adr.hi))) return false;
		if(!(this.mid.equalsValid(adr.mid))) return false;
		if(!(this.lo.equalsValid(adr.lo))) return false;
		return true;
	}

/**
 * testet alle Felder, inklusive ihrer Validität auf Gleichheit
 */
	@Override public boolean equals(Object obj)
	{	if(!(obj instanceof XGAddress)) return false;
		XGAddress adr = (XGAddress)obj;
		try
		{	return this.hi.equals(adr.hi) && this.mid.equals(adr.mid) && this.lo.equals(adr.lo);
		}
		catch(NullPointerException e)
		{	e.printStackTrace();
			return false;
		}
	}

/**
 * Stringrepresäntation einer Adresse
 */
	@Override public String toString()
	{	return "(" + this.hi + "/" + this.mid + "/" + this.lo + ")";}

	@Override public int compareTo(XGAddress o)
	{	int temp = 0;
		if(this.isHiValid() && o.isHiValid()) temp = this.hi.compare(o.hi);
		if(temp == 0 && this.isMidValid() && o.isMidValid()) temp = this.mid.compare(o.mid);
		if(temp == 0 && this.isLoValid() && o.isLoValid()) temp = this.lo.compare(o.lo);
		return temp;
	}

	@Override public XGAddress getAddress()
	{	return this;}
}
