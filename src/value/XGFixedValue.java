package value;

import parm.XGParameter;

public class XGFixedValue extends XGValue
{
	public static final XGFixedValue
		VALUE_0 = new XGFixedValue("fix", 0),
		VALUE_64 = new XGFixedValue("fix", 64),
		VALUE_127 = new XGFixedValue("fix", 127);

/*************************************************************************************************/

	private XGParameter parameter;
	private final int value;
	private final String name;

	public XGFixedValue(String name, int v)
	{	super(v);
		this.name = name;
		this.value = v;
		this.parameter = new XGParameter(name, v);
	}

	public XGFixedValue(XGParameter parm, int v)
	{	this(parm.getName(), v);
		this.parameter = parm;
	}

	@Override public Integer getValue()	{	return this.value;}

	@Override public XGParameter getParameter(){	return this.parameter;}

	public String getTag(){	return this.name;}
}
