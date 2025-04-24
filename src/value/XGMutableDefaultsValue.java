package value;

import bulk.XGBulk;
import parm.XGParameter;
import table.XGDefaultsTable;

public class XGMutableDefaultsValue extends XGValue implements XGMutableDefaults
{
	XGValue defaultsSelector = null;
	final XGDefaultsTable defaultsTable;
	final XGParameter parameter;

	public XGMutableDefaultsValue(XGValueType type,XGBulk blk)
	{	super(type,blk);

		this.parameter = new XGParameter(type.getConfig());
		this.defaultsTable = XGDefaultsTable.DEFAULTSTABLES.get(type.defaultsTableName);
	}

	public XGValue getDefaultsSelector(){	return this.defaultsSelector;}

	public void setDefaultsSelector(XGValue dsv){	this.defaultsSelector = dsv;}

	public XGDefaultsTable getDefaultsTable(){	return this.defaultsTable;}

	public XGValue getThis(){	return this;}

	@Override public int getDefaultValue(){	return XGMutableDefaults.super.getDefaultValue();}

	@Override public void initDepencies(){	XGMutableDefaults.super.initDefaultsDepencies();}

	@Override public XGParameter getParameter(){	return this.parameter;}
}
