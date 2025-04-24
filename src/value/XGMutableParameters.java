package value;

import parm.XGParameter;
import parm.XGParameterConstants;import table.XGParameterTable;import xml.XMLNodeConstants;

public interface XGMutableParameters
{
	XGValue getParametersSelector();

	void setParametersSelector(XGValue v);

	XGParameterTable getParametersTable();

	XGValue getThis();

	default XGParameter getParameter()
	{	return this.getParametersTable().getOrDefault(this.getParametersSelector().getValue(), XGParameterConstants.NO_PARAMETER);
	}

	default void initParameterDepencies()
	{	XGValue v = this.getThis();
		assert v.bulk != null;
		assert v.type != null;
		XGValue psv = v.bulk.getModule().getValues().get(v.type.parameterSelectorTag);
		if(psv == null) throw new RuntimeException(XMLNodeConstants.ATTR_PARAMETERSELECTOR + " " + v.type.parameterSelectorTag + " not found for value " + v.getTag());
		this.setParametersSelector(psv);
		this.getParametersSelector().valueListeners.add((XGValue val)->v.notifyParameterListeners());
	}
}
