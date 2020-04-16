package parm;

import xml.XMLNode;

public class XGFXParameter extends XGParameter
{
	final XGFXOption reverbOption, chorusOption, variationOption, insertionOption;
	final int defaultValue;
	final boolean control;
	final int insertAddressType;

	public XGFXParameter(XMLNode n)
	{	super(n);
	}
	
}
