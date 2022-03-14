package value;

import parm.XGParameter;

public class XGFixedValue extends XGValue
{
	public static final XGFixedValue
		VALUE_0 = new XGFixedValue("fix", 0),
		VALUE_64 = new XGFixedValue("fix", 64),
		VALUE_127 = new XGFixedValue("fix", 127);

/*************************************************************************************************/

	private final XGParameter parameter;
	private final int value;

	public XGFixedValue(String name, int v)
	{	super(name, v);
		this.value = v;
		this.parameter = new XGParameter(name, v);
	}

	@Override public Integer getValue()
	{	return this.value;
	}

	@Override public XGParameter getParameter()
	{	return this.parameter;
	}

	public String getTag()
	{	return "fix";
	}
}
