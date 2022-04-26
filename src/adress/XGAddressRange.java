package adress;

import application.XGLoggable;

public class XGAddressRange implements XGLoggable, XGAddressConstants
{
	final XGAddressField hi, mid, lo;

	protected XGAddressRange(int hi, int mid, int lo)
	{	this(new XGAddressField(hi), new XGAddressField(mid), new XGAddressField(lo));
	}

	public XGAddressRange(String hi, String mid, String lo)
	{	this.hi = new XGAddressField(hi, DEF_ADDRESSFILED);
		this.mid = new XGAddressField(mid, DEF_ADDRESSFILED);
		this.lo = new XGAddressField(lo, DEF_ADDRESSFILED);
	}

	public XGAddressRange(XGAddressField h, XGAddressField m, XGAddressField l)
	{	this.hi = h;
		this.mid = m;
		this.lo = l;
	}
/**
 * Erzeugt eine XGAddress aus den Slash-Tokens des übergebenen Strings mit der Option, die im String fehlenden Tokens durch die Felder der übergebenen XGAddress zu ersetzen.
 * @param adr	optionale XGAddresse aus der fehlende Attribute ersetzt werden 
 * @param s		Slash-getrennter Address-String
 */
	public XGAddressRange(String s, XGAddressRange adr)
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
			this.lo = new XGAddressField(s.substring(lastSlash + 1 ), l);
		}
		else
		{	this.hi = h;
			this.mid = m;
			this.lo = l;
		}
	}

	public XGAddressRange(String string){	this(string, null);}

/**
 * extrahiert und returniert das Hi-Field der XGAdress this
 * @return Wert des Fields als int
 */
	public XGAddressField getHi(){	return this.hi;}

	/**
	 * extrahiert und returniert das Mid-Field der XGAdress this
	 * @return Wert des Fields als int
	 */
	public XGAddressField getMid(){	return this.mid;}

	/**
	 * extrahiert und returniert das Lo-Field der XGAdress this
	 * @return Wert des Fields als int
	 */
	public XGAddressField getLo(){	return this.lo;}

/**
 * komplettiert this mittels adr, indem variable Fields durch diese aus adr möglichst konkretisiert werden 
 * @param adr Adresse mittels derer this konkretisiert werden soll
 * @return  die erfolgreich konktretisierte Adresse
 * @throws InvalidXGAddressException falls keine Schnittmenge ermittelt werden kann
 */
	public XGAddressRange complement(XGAddressRange adr) throws InvalidXGAddressException
	{	//		if(this.isFixed()) return this;
		//		if(adr.isFixed()) return adr;
		try{	return new XGAddressRange(this.hi.complement(adr.hi), this.mid.complement(adr.mid), this.lo.complement(adr.lo));}
		catch(InvalidXGAddressException e){	throw new InvalidXGAddressException(e.getMessage() + " within address: " + this + " and " + adr);}
	}

/**
 * testet, ob alle Felder von adr in allen Feldern von this enthalten sind
 * @param adr Adresse gegen deren Felder getestet wird
 * @return true, wenn alle Felder ineinander enthalten sind
 */
	public boolean contains(XGAddressRange adr){	return this.hi.contains(adr.hi) && this.mid.contains(adr.mid) && this.lo.contains(adr.lo);}

/**
 * testet alle Felder, inklusive ihrer Validität auf Gleichheit
 */
	@Override public boolean equals(Object obj)
	{	if(!(obj instanceof XGAddressRange)) return false;
		XGAddressRange adr = (XGAddressRange)obj;
		return this.hi.equals(adr.hi) && this.mid.equals(adr.mid) && this.lo.equals(adr.lo);
	}

/**
 * Stringrepresäntation einer Adresse
 */
	@Override public String toString(){	return "(" + this.hi + "/" + this.mid + "/" + this.lo + ")";}

	//@Override public Iterator<XGAddressRange> iterator()
	//{	Set<XGAddressRange> set = new LinkedHashSet<>();
	//	for(int hi : this.hi)
	//		for(int mid : this.mid)
	//			for(int lo : this.lo)
	//				set.add(new XGAddressRange(hi, mid, lo));
	//	return set.iterator();
	//}
}
