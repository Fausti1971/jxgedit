package value;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import msg.XGMessageParameterChange;
import obj.XGObject;
import parm.XGParameter;

public class XGValue
{	private static Logger log = Logger.getAnonymousLogger();

	private final XGAdress adress;
	private final XGParameter parameter;
	private Object value;
	private Set<XGValueChangeListener> listeners = new HashSet<>();
	
	public XGValue(XGAdress adr)
	{	this(adr, new Object());}

	public XGValue(XGAdress adr, Object val)
	{	this.adress = adr;
		this.value = val;
		this.parameter = XGObject.getParameter(adr);
	}

	public Object getValue()
	{	return this.value;}

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

	public String getTranslatedValue()
	{	return this.parameter.getValueTranslator().translate(this);}

	public boolean setValue(Object v)
	{	v = this.limitize(v);
		boolean changed = !this.value.equals(v);
		this.value = v;
		return changed;
	}

	public boolean changeValue(Object v)
	{	v = this.limitize(v);
		boolean changed = this.setValue(v);
		if(changed)
		{	try
			{	new XGMessageParameterChange(this).transmit();}
			catch(InvalidXGAdressException e)
			{	e.printStackTrace();}
			for(XGValueChangeListener l : listeners) l.valueChanged(this);
		}
		return changed;
	}

	public boolean addValue(Object v)
	{	switch(this.parameter.getValueType())
		{	case NUMBER:	return changeValue((int)this.value + (int)v);
			case TEXT:		return changeValue(((String)this.value) + v);
			case BITMAP:	return false;
			default:		return false;
		}
	}

	private	Object limitize(Object v)
	{	if(!this.parameter.isLimitizable()) return v;
		switch(this.parameter.getValueType())
		{	case NUMBER:	return Math.min(this.parameter.getMaxValue(), Math.max(this.parameter.getMinValue(), (int)v));
			case TEXT:		return "";
			case BITMAP:	return null;
			default:		return null;
		}
	}

	@Override public String toString()
	{	return this.getTranslatedValue();}
}
