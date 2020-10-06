package value;

import parm.XGParameter;

public class XGFixedValue extends XGValue
{
	private final XGParameter parameter;

	public XGFixedValue(String name, int v)
	{	super(name, v);
		this.parameter = new XGParameter(name, v);
	}

	@Override public XGParameter getParameter()
	{	return this.parameter;
	}
}
