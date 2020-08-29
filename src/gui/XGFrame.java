package gui;

import adress.XGMemberNotFoundException;
import module.XGModule;
import xml.XMLNode;

public class XGFrame extends XGComponent
{	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

	public XGFrame(String text)
	{	super(text);
		this.borderize();
	}

	public XGFrame(XMLNode n, XGModule mod)
	{	super(n, mod);
		if(n.hasAttribute(ATTR_NAME)) this.borderize();
		for(XMLNode x : n.getChildNodes())
		{	try
			{	this.add(newItem(x, mod));
			}
			catch(XGMemberNotFoundException e)
			{	LOG.severe(e.getMessage());
			}
		}
//		this.logInitSuccess();
	}
}
