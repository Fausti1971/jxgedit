package adress;

import application.Rest;
import xml.XMLNode;

public class XGAddressField implements XGAddressConstants
{	private static String EXCEPTIONTEXT = "access to invald XGAdressField: ";

/******************************************************************************************************************/

	private final int value;

	public XGAddressField()
	{	this.value = INVALIDFIELD;
	}

	public XGAddressField(XMLNode n)
	{	if(n == null) this.value = INVALIDFIELD;
		else this.value = Rest.parseIntOrDefault(n.getTextContent(), INVALIDFIELD);
	}

	public XGAddressField(String v)
	{	this.value = Rest.parseIntOrDefault(v, INVALIDFIELD);
	}

	public XGAddressField(int v)
	{	this.value = v;
	}

	public boolean isValid()
	{	return (this.value & ~MIDIBYTEMASK) == 0;
	}

	public int getValue() throws InvalidXGAddressException
	{	if(this.isValid()) return this.value;
		else throw new InvalidXGAddressException(EXCEPTIONTEXT);
	}

	public XGAddressField complement(XGAddressField f) throws InvalidXGAddressException
	{	int status = 0;
		if(this.isValid()) status = 1;
		if(f.isValid()) status |= 2;
		switch(status)
		{	case 1:	return this;
			case 2:	return f;
			case 3:	if(this.value == f.value) return this;
			default:
			case 0:	throw new InvalidXGAddressException("can't complement adress-field " + this + " with " + f);
		}
	}

	public int compare(XGAddressField o)
	{	if(this.isValid() && o.isValid()) return Integer.compare(this.value, o.value);
		else return 0;
	}

	public boolean equalsValid(XGAddressField a)
	{	if(this.isValid() && a.isValid()) return this.value == a.value;
		return true;
	}

	@Override public boolean equals(Object o)
	{	if(!(o instanceof XGAddressField)) return false;
		XGAddressField a = (XGAddressField)o;
		return this.isValid() == a.isValid() && this.value == a.value;
	}

	@Override public String toString()
	{	if(this.isValid()) return "" + this.value;
		else return "-";
	}
}
