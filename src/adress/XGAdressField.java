package adress;

public class XGAdressField implements XGAdressConstants
{	private static String EXCEPTIONTEXT = "access to invald XGAdressField: ";

/******************************************************************************************************************/

	private final int value;
	private final boolean valid;

	public XGAdressField()
	{	this.valid = false;
		this.value = 0;
	}

	public XGAdressField(String v)
	{	int tempValue;
		boolean tempValid = true;
		try
		{	tempValue = Integer.parseInt(v);
		}
		catch(NumberFormatException e)
		{	tempValid = false;
			tempValue = 0;
		}
		this.valid = tempValid;
		this.value = tempValue;
	
	}
	public XGAdressField(int v)
	{	this.value = v;
		if(v == INVALIDFIELD) this.valid = false;
		else this.valid = true;
	}

	public boolean isValid()
	{	return this.valid;}

	public int getValue() throws InvalidXGAdressException
	{	if(this.valid) return this.value;
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

	public boolean equalsValid(XGAdressField a)
	{	if(this.valid && a.valid) return this.value == a.value;
		return true;
	}

	@Override public boolean equals(Object o)
	{	if(!(o instanceof XGAdressField)) return false;
		XGAdressField a = (XGAdressField)o;
		return this.valid == a.valid && this.value == a.value;
	}

	@Override public String toString()
	{	if(this.valid) return "" + this.value;
		else return "-";
	}
}
