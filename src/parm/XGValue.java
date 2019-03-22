package parm;
import java.util.HashSet;
import java.util.Set;
import msg.XGMessageParameterChange;
import obj.XGAdress;
import obj.XGObject;

public class XGValue
{	private final XGAdress adress;
	private Object value;
	private Set<ValueChangeListener> listeners = new HashSet<>();
	
	public XGValue(XGAdress adr)
	{	this(adr, new Object());
	}

	public XGValue(XGAdress adr, Object val)
	{	this.adress = adr;
		this.value = val;
	}

	public Object getValue()
	{	return this.value;}

	public XGOpcode getOpcode()
	{	return XGObject.getOpcode(this.adress);}

	public XGAdress getAdress()
	{	return this.adress;}

	private XGParameter getParameter()
	{	return XGObject.getParameter(this.adress);}

	public boolean setValue(Object v)
	{	v = this.limitize(v);
		boolean changed = !this.value.equals(v);
		this.value = v;
		return changed;
	}

	public boolean changeValue(Object v)
	{	v = this.limitize(v);
		boolean changed = this.setValue(v);
		if(changed) new XGMessageParameterChange(this).transmit();
		return changed;
	}

	public boolean addValue(Object v)
	{	switch(this.getOpcode().getValueType())
		{	case Number:	return changeValue((int)this.value + (int)v);
			case Text:		return changeValue(((String)this.value) + v);
			case Bitmap:	return false;
			default:		return false;
		}
	}

	private	Object limitize(Object v)
	{	switch(this.getOpcode().getValueType())
		{	case Number:	return Math.min(getParameter().getMaxValue(), Math.max(getParameter().getMinValue(), (int)v));
			case Text:		return "";
			case Bitmap:	return null;
			default:		return null;
		}
	}
}
