package value;

import adress.InvalidXGAddressException;
import adress.XGAddress;
import msg.XGMessenger;

public class XGIntegerValue extends XGValue
{	int content;

	public XGIntegerValue(XGMessenger src, XGAddress adr) throws InvalidXGAddressException
	{	super(src, adr);}

	@Override protected void validate(Object o) throws WrongXGValueTypeException
	{	if(o instanceof Integer) return;
		throw new WrongXGValueTypeException(o.getClass().getSimpleName() + " is not a integer!");
	}

	@Override public boolean setContent(Object o) throws WrongXGValueTypeException
	{	this.validate(o);
		int i = (int)this.limitize(o);
		boolean changed = (i == (int)o);
		this.content = i;
		this.notifyListeners();
		return changed;
	}

	@Override public Object getContent()
	{	return this.content;}

	@Override public boolean addContent(Object o) throws WrongXGValueTypeException
	{	this.validate(o);
		return setContent(this.content + (int)o);
	}

	@Override protected Object limitize(Object o)
	{	return Math.min(this.getParameter().getMaxValue(), Math.max(this.getParameter().getMinValue(), (int)o));
	}

	@Override public String toString()
	{	return this.getParameter().getValueTranslator().translate(this);
	}
}
