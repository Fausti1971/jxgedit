package gui;

import module.XGModule;
import xml.XMLNode;

public class XGEnvelope extends XGFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XGEnvelope(XMLNode n, XGModule mod)
	{	super(n, mod);
	}

	@Override public XMLNode getConfig()
	{	return new XMLNode("env");
	}
}
