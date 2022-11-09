package value;

import bulk.XGBulk;
import parm.XGParameter;
import table.XGParameterTable;
import static table.XGParameterTable.PARAMETERTABLES;

public class XGMutableParametersValue extends XGValue implements XGMutableParameters
{
	XGValue parametersSelector = null;
	final XGParameterTable parametersTable;
	final int defaultValue;

	public XGMutableParametersValue(XGValueType type,XGBulk blk)
	{	super(type,blk);

		this.parametersTable = PARAMETERTABLES.get(type.parameterTableName);
		if(this.parametersTable == null) throw new RuntimeException("parameter-table \"" + type.parameterTableName + "\" for " + type.getTag() + " not found");

		this.defaultValue = type.getConfig().getValueAttribute(ATTR_DEFAULT, 0);
	}

	public XGValue getParametersSelector(){	return this.parametersSelector;}

	public void setParametersSelector(XGValue v){	this.parametersSelector = v;}

	public XGParameterTable getParametersTable(){	return this.parametersTable;}

	public XGValue getThis(){	return this;}

	@Override public XGParameter getParameter(){	return XGMutableParameters.super.getParameter();}

	@Override public void initDepencies(){	XGMutableParameters.super.initParameterDepencies();}

	@Override public int getDefaultValue(){	return this.defaultValue;}
}
