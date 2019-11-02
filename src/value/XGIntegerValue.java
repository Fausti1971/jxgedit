package value;

import adress.InvalidXGAdressException;
import adress.XGAdress;
import device.XGDevice;
import opcode.NoSuchOpcodeException;

public class XGIntegerValue extends XGValue
{	int content;

	public XGIntegerValue(XGDevice dev, XGAdress adr) throws InvalidXGAdressException, NoSuchOpcodeException
	{	super(dev, adr);}

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

	public boolean addContent(Object o) throws WrongXGValueTypeException
	{	this.validate(o);
		return setContent(this.content + (int)o);
	}

	protected Object limitize(Object o)
	{	return Math.min(this.getParameter().getMaxValue(), Math.max(this.getParameter().getMinValue(), (int)o));}

	public String toString()
	{	return this.getParameter().getValueTranslator().translate(this);}
}
