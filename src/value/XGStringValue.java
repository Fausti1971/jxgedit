package value;

import adress.InvalidXGAdressException;
import adress.XGAdress;

public class XGStringValue extends XGValue
{	String content;

	public XGStringValue(XGAdress adr) throws InvalidXGAdressException
	{	super(adr);
	}

	public boolean setContent(Object o) throws WrongXGValueTypeException
	{	this.validate(o);
		String s = (String)this.limitize(o);
		boolean changed = !this.content.equals(o);
		this.content = s;
		this.notifyListeners();
		return changed;
	}

	public Object getContent()
	{	return this.getContent();}

	public boolean addAndTransmit(Object o) throws WrongXGValueTypeException
	{	this.validate(o);
		try
		{	return this.setContentAndTransmit(this.limitize(this.content + (String)o));
		}
		catch(InvalidXGAdressException | WrongXGValueTypeException e)
		{	e.printStackTrace();
			return false;
		}
	}

	protected Object limitize(Object o)
	{	return ((String)o).substring(0, this.getParameter().getByteCount());}

	public String toString()
	{	return this.content;}

	protected void validate(Object o) throws WrongXGValueTypeException
	{	if(o instanceof String) return;
		throw new WrongXGValueTypeException(o.getClass().getSimpleName() + " is not a string");
	}
}
