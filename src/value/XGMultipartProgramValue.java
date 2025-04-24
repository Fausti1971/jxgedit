package value;

import bulk.XGBulk;
import msg.XGMessageBulkDump;
import msg.XGMessengerException;

public class XGMultipartProgramValue extends XGMutableValue
{
	XGMultipartProgramValue(XGValueType vt,XGBulk bk)
	{	super(vt,bk);
		this.valueListeners.add(XGProgramBuffer::bufferProgram);
	}

	@Override public void submit(XGMessageBulkDump msg)throws XGMessengerException
	{	this.getModule().getValues().get(XGValueType.MP_PM_VALUE_TAG).submit(msg);//setze zuerst Partmode
		super.submit(msg);
	}
}
