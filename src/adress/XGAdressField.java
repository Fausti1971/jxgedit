package adress;

public class XGAdressField implements XGAdressConstants
{	private static String EXCEPTIONTEXT = "access to invald XGAdressField: ";

/******************************************************************************************************************/

	private final int value, mask;
	private final boolean valid;
//	private AdressFieldType type;

	public XGAdressField()
	{	this.valid = false;
		this.mask = 0;
		this.value = 0;
	}

	public XGAdressField(String v, String m)
	{	int tempValue, tempMask;
		boolean tempValid = true;
		try
		{	tempValue = Integer.parseInt(v);
		}
		catch(NumberFormatException e)
		{	tempValid = false;
			tempValue = 0;
		}
		try
		{	tempMask = Integer.parseInt(m);}
		catch(NumberFormatException e1)
		{	tempMask = MIDIBYTEMASK;}
		this.valid = tempValid;
		this.value = tempValue;
		this.mask = tempMask;
	
	}
	public XGAdressField(int v)
	{	this.value = v;
		this.mask = MIDIBYTEMASK;
		this.valid = true;
	}

	public XGAdressField(int v, int m)
	{	this.value = v;
		this.mask = m;
		this.valid = true;
	}

	public boolean isValid()
	{	return this.valid;}

	public int getValue() throws InvalidXGAdressException
	{	if(this.valid) return this.value;
		else throw new InvalidXGAdressException(EXCEPTIONTEXT);
	}

	public int getMaskedValue() throws InvalidXGAdressException
	{	if(this.valid) return this.value & this.mask;
		else throw new InvalidXGAdressException(EXCEPTIONTEXT);
	}

	public XGAdressField complement(XGAdressField f) throws InvalidXGAdressException
	{	int status = 0;
		if(this.valid) status = 1;
		if(f.valid) status |= 2;
		switch(status)
		{	case 1:	return this;
			case 2:	return f;
			case 3:	if(this.value == f.value) return this;
			default:
			case 0:	throw new InvalidXGAdressException("can't complement adress-field " + this + " with " + f);
		}
	}

	public int compare(XGAdressField o)
	{	if(this.valid && o.valid) return Integer.compare(this.value, o.value);
		else return 0;
	}

	public boolean equalsMaskedValid(XGAdressField a)
	{	if(this.valid && a.valid) return (this.value & this.mask & a.mask) == (a.value & a.mask & this.mask);
		return true;
	}

	@Override public boolean equals(Object o)
	{	if(!(o instanceof XGAdressField)) return false;
		return this.valid == ((XGAdressField)o).valid && this.value == ((XGAdressField)o).value && this.mask == ((XGAdressField)o).mask;
	}

	@Override public String toString()
	{	if(this.valid) return "" + this.value;
		else return "-";
	}
}
