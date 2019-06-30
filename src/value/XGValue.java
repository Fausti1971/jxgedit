package value;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import msg.XGMessageParameterChange;
import obj.XGObjectType;
import parm.XGParameter;
import parm.XGParameterConstants;

public class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAdressable
{	private static Logger log = Logger.getAnonymousLogger();

	private final XGAdress adress;
	private final XGParameter parameter;
	private String textValue;
	private int numberValue;
	private Image imageValue;
	private Set<XGValueChangeListener> listeners = new HashSet<>();
	
	public XGValue(XGAdress adr) throws InvalidXGAdressException
	{	this(adr, new Object());}

	public XGValue(XGAdress adr, Object val) throws InvalidXGAdressException
	{	this.adress = adr;
		this.parameter = XGObjectType.getObjectType(adr).getParameter(adr);
		ValueType vt = this.parameter.getValueType();
		if(val instanceof Image && vt.equals(ValueType.BITMAP)) this.imageValue = (Image)val;
		if(val instanceof Integer && vt.equals(ValueType.NUMBER)) this.numberValue = (int)val;
		if(val instanceof String && vt.equals(ValueType.TEXT)) this.textValue = (String)val;
	}

	public void addListener(XGValueChangeListener l)
	{	this.listeners.add(l);}

	public void removeListener(XGValueChangeListener l)
	{	this.listeners.remove(l);}

	public String getTextValue() throws WrongXGValueTypeException
	{	switch(this.parameter.getValueType())
		{	case TEXT:		return this.textValue;
			case BITMAP:	throw new WrongXGValueTypeException("can't convert BITMAP to TEXT");
			case NUMBER:	return this.parameter.getValueTranslator().translate(this);
			default:		return "";
		}
	}

	public int getNumberValue() throws WrongXGValueTypeException
	{	switch(this.parameter.getValueType())
		{	case BITMAP:	throw new WrongXGValueTypeException("can't convert BITMAP to NUMBER");
			case NUMBER:	return this.numberValue;
			case TEXT:		try
							{	return Integer.parseInt(this.textValue);
							}
							catch(NumberFormatException e)
							{	throw new WrongXGValueTypeException("can't convert " + this.textValue + " to NUMBER");
							}
			default:		throw new WrongXGValueTypeException("can't convert " + this.parameter.getValueType().name() + " to NUMBER");
		}
	}

	public Image getImageValue() throws WrongXGValueTypeException
	{	switch(this.parameter.getValueType())
		{	case BITMAP:	return this.imageValue;
			case NUMBER:
			case TEXT:
			default:		throw new WrongXGValueTypeException("can't convert " + this.parameter.getValueType().name() + " to BITMAP");
		}
	}

	public int getOffset()
	{	try
		{	return this.adress.getLo();}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return 0;
		}
	}

	public XGParameter getParameter()
	{	return this.parameter;}

	public XGAdress getAdress()
	{	return this.adress;}

	public boolean setValue(Object v) throws WrongXGValueTypeException
	{	v = this.limitize(v);
		ValueType vt = this.parameter.getValueType();
		boolean changed;

		if(v instanceof Integer && vt.equals(ValueType.NUMBER))
		{	changed = this.numberValue != (int)v;
			this.numberValue = (int)v;
			return changed;
		}
		if(v instanceof String && vt.equals(ValueType.TEXT))
		{	changed = !this.textValue.equals((String)v);
			this.textValue = (String)v;
			return changed;
		}
		if(v instanceof Image && vt.equals(ValueType.BITMAP))
		{	changed = !this.imageValue.equals((Image)v);
			this.imageValue = (Image)v;
			return changed;
		}
		throw new WrongXGValueTypeException("can't convert " + v.getClass().getSimpleName() + " to " + vt.name());
	}

	public boolean changeValue(Object v)
	{	v = this.limitize(v);
		boolean changed = false;
		try
		{	changed = this.setValue(v);
			if(changed)
			{	new XGMessageParameterChange(this).transmit();
				notifyListeners();
			}
		}
		catch(WrongXGValueTypeException | InvalidXGAdressException e)
		{	return false;}
		return changed;
	}

	public boolean addValue(Object v)
	{	switch(this.parameter.getValueType())
		{	case NUMBER:	return changeValue(this.numberValue + (int)v);
			case TEXT:		return changeValue(this.textValue + v);
			case BITMAP:	return false;
			default:		return false;
		}
	}

	private	Object limitize(Object v)
	{	if(!this.parameter.isLimitizable()) return v;
		switch(this.parameter.getValueType())
		{	case NUMBER:	return Math.min(this.parameter.getMaxValue(), Math.max(this.parameter.getMinValue(), (int)v));
			case TEXT:		return "";	//TODO Stringlänge auf maxValue reduzieren;
			case BITMAP:	return null;
			default:		return null;
		}
	}

	private void notifyListeners()
	{	for(XGValueChangeListener l : listeners) l.valueChanged(this);}

	@Override public String toString()
	{	try
		{	return this.getTextValue();
		}
		catch(WrongXGValueTypeException e)
		{	e.printStackTrace();
			return "";
		}
	}

	public int compareTo(XGValue o)
	{	return this.adress.compareTo(o.adress);}
}
