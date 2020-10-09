package gui;

import adress.XGMemberNotFoundException;
import module.XGModule;
import xml.XMLNode;

public class XGEnvelope extends XGComponent
{
	private static final long serialVersionUID = 1L;

/**************************************************************************************/

	private final XGEQCurve panel;

	public XGEnvelope(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	super(n, mod);
		this.borderize();
		this.setLayout(null);
		this.panel = new XGEQCurve(this, mod);
		this.add(this.panel);
	}
}
