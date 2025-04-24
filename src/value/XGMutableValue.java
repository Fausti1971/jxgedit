package value;

import bulk.XGBulk;
import parm.XGParameter;
import table.XGDefaultsTable;
import table.XGParameterTable;

public class XGMutableValue extends XGValue implements XGMutableDefaults, XGMutableParameters
{
	XGValue parametersSelector = null, defaultsSelector = null;
	final XGParameterTable parametersTable;
	final XGDefaultsTable defaultsTable;

	XGMutableValue(XGValueType vt, XGBulk bk)
	{	super(vt, bk);

		this.parametersTable = XGParameterTable.PARAMETERTABLES.get(type.parameterTableName);
		if(this.parametersTable == null) throw new RuntimeException("parameter-table \"" + type.parameterTableName + "\" for " + type.getTag() + " not found");

		this.defaultsTable = XGDefaultsTable.DEFAULTSTABLES.get(type.defaultsTableName);
	}

	public XGValue getThis(){	return this;}

	@Override public void initDepencies()
	{
		XGMutableParameters.super.initParameterDepencies();
		XGMutableDefaults.super.initDefaultsDepencies();
	}

	@Override public int getDefaultValue(){	return XGMutableDefaults.super.getDefaultValue();}

	@Override public XGParameter getParameter(){	return XGMutableParameters.super.getParameter();}

	public XGValue getDefaultsSelector(){	return this.defaultsSelector;}

	public void setDefaultsSelector(XGValue dsv){	this.defaultsSelector = dsv;}

	public XGDefaultsTable getDefaultsTable(){	return this.defaultsTable;}

	public XGValue getParametersSelector(){	return this.parametersSelector;}

	public void setParametersSelector(XGValue v){	this.parametersSelector = v;}

	public XGParameterTable getParametersTable(){	return this.parametersTable;}
}
