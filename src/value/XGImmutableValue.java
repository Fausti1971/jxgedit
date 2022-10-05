package value;

import bulk.XGBulk;import parm.XGParameter;

public class XGImmutableValue extends XGValue
{
	final int defaultValue;
	final XGParameter parameter;

	XGImmutableValue(int v)
	{	super(v);
		this.defaultValue = v;
		this.parameter = new XGParameter(NO_PARAMETERNAME, v);
	}

	XGImmutableValue(XGValueType vt, XGBulk bk)
	{	super(vt, bk);

		this.parameter = new XGParameter(type.getConfig());
		this.defaultValue = type.getConfig().getValueAttribute(ATTR_DEFAULT, 0);
	}

	public void initDepencies(){}

	public int getDefaultValue(){	return this.defaultValue;}

	public XGParameter getParameter(){	return this.parameter;}
}
