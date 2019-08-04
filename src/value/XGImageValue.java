package value;

import java.awt.Image;
import adress.InvalidXGAdressException;
import adress.XGAdress;

public class XGImageValue extends XGValue
{	Image content;

	public XGImageValue(XGAdress adr) throws InvalidXGAdressException
	{	super(adr);}

	public boolean setContent(Object o) throws WrongXGValueTypeException
	{	this.validate(o);
		this.content = (Image)this.limitize(o);
		return false;
	}

	public Object getContent()
	{	return this.content;}

	public boolean addAndTransmit(Object o) throws WrongXGValueTypeException
	{	return setContent(o);}

	protected Object limitize(Object o)
	{	return o;}

	public String toString()
	{	return "imageplaceholder";}

	protected void validate(Object o) throws WrongXGValueTypeException
	{	if(o instanceof Image) return;
		throw new WrongXGValueTypeException(o.getClass().getSimpleName() + " is not a image!");
	}
}
