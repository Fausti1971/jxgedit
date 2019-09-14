package adress;

import java.util.logging.Logger;
import org.w3c.dom.Node;
import application.Rest;
public class XGAdress implements XGAdressConstants, Comparable<XGAdress>, XGAdressable
{	private static Logger log =Logger.getAnonymousLogger();

	private final XGAdressField hi, mid, lo;

	public XGAdress(String hi, String mid, String lo)
	{	this.hi = new XGAdressField(hi);
		this.mid = new XGAdressField(mid);
		this.lo = new XGAdressField(lo);
	}

	public XGAdress(int hi, int mid, int lo)
	{	this.hi = new XGAdressField(hi);
		this.mid = new XGAdressField(mid);
		this.lo = new XGAdressField(lo);
	}

	public XGAdress(XGAdressField h, XGAdressField m, XGAdressField l)
	{	if(h != null) this.hi = h;
		else this.hi = new XGAdressField();
		if(m != null) this.mid = m;
		else this.mid = new XGAdressField();
		if(l != null) this.lo = l;
		else this.lo = new XGAdressField();
	}

	public XGAdress(Node item)
	{	this.hi = new XGAdressField(Rest.getFirstNodeChildTextContentByTagAsString(item, TAG_HI));
		this.mid = new XGAdressField(Rest.getFirstNodeChildTextContentByTagAsString(item, TAG_MID));
		this.lo = new XGAdressField(Rest.getFirstNodeChildTextContentByTagAsString(item, TAG_LO));
	}
/**
 * extrahiert und returniert das Hi-Field der XGAdress this
 * @return Wert des Fields als int
 * @throws InvalidXGAdressException falls das Field invalid ist
 */
	public int getHi() throws InvalidXGAdressException
	{	return this.hi.getValue();}

	/**
	 * extrahiert und returniert das Mid-Field der XGAdress this
	 * @return Wert des Fields als int
	 * @throws InvalidXGAdressException falls das Field invalid ist
	 */
	public int getMid() throws InvalidXGAdressException
	{	return this.mid.getValue();}

	/**
	 * extrahiert und returniert das Lo-Field der XGAdress this
	 * @return Wert des Fields als int
	 * @throws InvalidXGAdressException falls das Field invalid ist
	 */
	public int getLo() throws InvalidXGAdressException
	{	return this.lo.getValue();}

/**
 * testet, ob die Adresse vollständig ist, und somit einen XGValue adressieren kann
 * @return true, wenn alle Fields valide sind
 */
	public boolean isValueAdress()
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
 */
	public XGAdress complement(XGAdress adr)
	{	if(this.isValueAdress()) return this;
		if(adr.isValueAdress()) return adr;
		try
		{	return new XGAdress(this.hi.complement(adr.hi), this.mid.complement(adr.mid), this.lo.complement(adr.lo));
		}
		catch(InvalidXGAdressException e)
		{	log.info("can't complement adress " + this + " with " + adr);
			return this;
		}
	}

/**
 * testet ausschließlich valide Fields von this mit validen Fields von adr
 * @param adr Adresse gegen deren valide Field getestet wird
 * @return true, wenn alle valieden Fields gleich sind
 */
	public boolean equalsValidFields(XGAdress adr)
	{	if(!(this.hi.equalsValid(adr.hi))) return false;
		if(!(this.mid.equalsValid(adr.mid))) return false;
		if(!(this.lo.equalsValid(adr.lo))) return false;
		return true;
	}

/**
 * testet alle Fields, inklusive ihrer Validität auf Gleichheit
 */
	@Override public boolean equals(Object obj)
	{	if(!(obj instanceof XGAdress)) return false;
		XGAdress adr = (XGAdress)obj;
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

	public int compareTo(XGAdress o)
	{	int temp = 0;
		if(this.isHiValid() && o.isHiValid()) temp = this.hi.compare(o.hi);
		if(temp == 0 && this.isMidValid() && o.isMidValid()) temp = this.mid.compare(o.mid);
		if(temp == 0 && this.isLoValid() && o.isLoValid()) temp = this.lo.compare(o.lo);
		return temp;
	}

	public XGAdress getAdress()
	{	return this;}

	public String getInfo()
	{	return this.getClass().getSimpleName() + " " + this.toString();}
}
