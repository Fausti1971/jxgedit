package value;

import java.awt.Image;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import device.XGDevice;
import opcode.NoSuchOpcodeException;

public class XGImageValue extends XGValue
{	Image content;

	public XGImageValue(XGDevice dev, XGAdress adr) throws InvalidXGAdressException, NoSuchOpcodeException
	{	super(dev, adr);}

	public boolean setContent(Object o) throws WrongXGValueTypeException
	{	this.validate(o);
		this.content = (Image)this.limitize(o);
		this.notifyListeners();
		return false;
	}

	public Object getContent()
	{	return this.content;}

	public boolean addContent(Object o) throws WrongXGValueTypeException
	{	return this.setContent(o);}

	protected Object limitize(Object o)
	{	return o;}

	public String toString()
	{	return "imageplaceholder";}

	protected void validate(Object o) throws WrongXGValueTypeException
	{	if(o instanceof Image) return;
		throw new WrongXGValueTypeException(o.getClass().getSimpleName() + " is not a image!");
	}
}
