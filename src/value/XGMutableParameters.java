package value;

import parm.XGParameter;
import static parm.XGParameterConstants.NO_PARAMETER;
import table.XGParameterTable;
import static xml.XMLNodeConstants.ATTR_PARAMETERSELECTOR;

public interface XGMutableParameters
{
	XGValue getParametersSelector();

	void setParametersSelector(XGValue v);

	XGParameterTable getParametersTable();

	XGValue getThis();

	default XGParameter getParameter()
	{	return this.getParametersTable().getOrDefault(this.getParametersSelector().getValue(), NO_PARAMETER);
	}

	default void initParameterDepencies()
	{	XGValue v = this.getThis();
		assert v.bulk != null;
		assert v.type != null;
		XGValue psv = v.bulk.getModule().getValues().get(v.type.parameterSelectorTag);
		if(psv == null) throw new RuntimeException(ATTR_PARAMETERSELECTOR + " " + v.type.parameterSelectorTag + " not found for value " + v.getTag());
		this.setParametersSelector(psv);
		this.getParametersSelector().valueListeners.add((XGValue val)->v.notifyParameterListeners());
	}
}
