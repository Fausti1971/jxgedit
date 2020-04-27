package gui;

import javax.swing.JComponent;
import adress.XGAddressableSet;
import value.XGValue;
import xml.XMLNode;

public class XGEnvelope extends JComponent implements XGComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XGEnvelope(XMLNode n, XGAddressableSet<XGValue> set)
	{
	}

	@Override public JComponent getJComponent()
	{	return this;
	}

	@Override public XMLNode getConfig()
	{	return new XMLNode("env", null);
	}

	@Override public XGValue getValue()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
