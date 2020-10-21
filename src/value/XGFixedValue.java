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

	public XGFixedValue(String name, int v)
	{	super(name, v);
		this.parameter = new XGParameter(name, v);
	}

	@Override public XGParameter getParameter()
	{	return this.parameter;
	}

	@Override public boolean editIndex(int i)
	{	return false;
	}

}
