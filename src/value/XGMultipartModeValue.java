package value;

import bulk.XGBulk;

public class XGMultipartModeValue extends XGMutableDefaultsValue
{
	public XGMultipartModeValue(XGValueType type,XGBulk blk)
	{	super(type,blk);
		this.valueListeners.add(XGProgramBuffer::restoreProgram);
	}
}
