package parm;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import application.InvalidXGAdressException;
import msg.XGMessageParameterChange;
import obj.XGAdress;
import obj.XGObject;
import parm.XGParameterConstants.ValueType;

public class XGValue
{	private final XGAdress adress;
	private Object value;
	private final XGParameter param;
	private Set<ValueChangeListener> listeners = new HashSet<>();
	
	public XGValue(XGAdress adr)
	{	this(adr, new Object());}

	public XGValue(XGAdress adr, Object val)
	{	this.adress = adr;
		this.value = val;
		this.param = XGObject.getParameter(adr);
	}

	public Object getValue()
	{	return this.value;}

	public int getOffset()
	{	try
		{	return this.adress.getLo();}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return this.param.getOffset();
		}
	}

	public msg.Bytes.ByteType getByteType()
	{	return this.param.getByteType();}

	public int getByteCount()
	{	return this.param.getByteCount();}

	public ValueType getValueType()
	{	return this.param.getValueType();}

	public int getMaxValue()
	{	return this.param.getMaxValue();}

	public int getMinValue()
	{	return this.param.getMinValue();}

	public String getLongName()
	{	return this.param.getLongName();}

	public String getShortName()
	{	return this.param.getShortName();}

	public Map<Integer, String> getTranslationMap()
	{	return this.param.getTranslationMap();}

	public XGAdress getAdress()
	{	return this.adress;}

	public String getTranslatedValue()
	{	return this.param.translate(this);}

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
			for(ValueChangeListener l : listeners) l.valueChanged(this);
		}
		return changed;
	}

	public boolean addValue(Object v)
	{	try
		{	switch(this.getValueType())
			{	case NUMBER:	return changeValue((int)this.value + (int)v);
				case TEXT:		return changeValue(((String)this.value) + v);
				case BITMAP:	return false;
				default:		return false;
			}
		}
		catch(NullPointerException e)
		{	e.printStackTrace();
			return false;
		}
	}

	private	Object limitize(Object v)
	{	try
		{	switch(this.getValueType())
			{	case NUMBER:	return Math.min(getMaxValue(), Math.max(getMinValue(), (int)v));
				case TEXT:		return "";
				case BITMAP:	return null;
				default:		return null;
			}
		}
		catch(NullPointerException e)
		{	e.printStackTrace();
			return null;
		}
	}
}
