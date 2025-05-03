package value;

import bulk.XGBulk;
import msg.XGMessageBulkDump;
import msg.XGMessengerException;import msg.XGRequest;import msg.XGResponse;

public class XGMultipartProgramValue extends XGMutableValue
{
	XGMultipartProgramValue(XGValueType vt,XGBulk bk)
	{	super(vt,bk);
		this.valueListeners.add(XGProgramBuffer::bufferProgram);
	}

	@Override public void submit(XGResponse res)throws XGMessengerException
	{	if(res instanceof XGMessageBulkDump)
		{	this.getModule().getValues().get(XGValueType.MP_PM_VALUE_TAG).submit(res);//setze zuerst Partmode
			super.submit(res);
		}
	}
}
