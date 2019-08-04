package value;

import adress.InvalidXGAdressException;
import adress.XGAdress;

public class XGIntegerValue extends XGValue
{	int content;

	public XGIntegerValue(XGAdress adr) throws InvalidXGAdressException
	{	super(adr);}

	protected void validate(Object o) throws WrongXGValueTypeException
	{	if(o instanceof Integer) return;
		throw new WrongXGValueTypeException(o.getClass().getSimpleName() + " is not a integer!");
	}

	public boolean setContent(Object o) throws WrongXGValueTypeException
	{	this.validate(o);
		int i = (int)this.limitize(o);
		boolean changed = (i == (int)o);
		this.content = i;
		this.notifyListeners();
		return changed;
	}

	public Object getContent()
	{	return this.content;}

	public boolean addAndTransmit(Object o) throws WrongXGValueTypeException
	{	this.validate(o);
		try
		{	return setContentAndTransmit(this.content + (int)o);
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return false;
		}
	}

	protected Object limitize(Object o)
	{	return Math.min(this.getParameter().getMaxValue(), Math.max(this.getParameter().getMinValue(), (int)o));}

	public String toString()
	{	return this.getParameter().getValueTranslator().translate(this);}
}
