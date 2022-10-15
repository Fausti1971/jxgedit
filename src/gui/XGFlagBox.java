package gui;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.*;
import application.XGStrings;import module.XGModule;import parm.XGParameter;import value.XGValue;import xml.XMLNode;

public class XGFlagBox extends XGFrame
{
	public static XGFlagBox newFlagbox(XGModule mod, XMLNode node)throws XGComponentException
	{	Set<String> tags = XGStrings.splitCSV(node.getStringAttribute(ATTR_VALUE_TAG));
		int i = 0;
		XGValue[] vals = new XGValue[tags.size()];
		for(String tag : tags) vals[i++] = mod.getValues().get(tag);
		return new XGFlagBox(node.getStringAttribute(ATTR_LABEL), vals);
	}

/************************************************************************************************************************/

	private final Set<XGValue> values = new LinkedHashSet<>();

	public XGFlagBox(String name, XGValue... vals)throws XGComponentException
	{	super(name);
		if(vals == null ) throw new XGComponentException("value array is null");
		for(XGValue v : vals)
		{	if(v != null) this.values.add(v);
		}

		JButton button = new JButton("select");
		button.addActionListener((ActionEvent)->new XGCheckboxPopup(this, this.values));
		button.setAlignmentY(0.5f);
		button.setAlignmentX(0.5f);

		this.add(button, "0,0,1,2");
	}

		private class XGCheckboxPopup extends JPopupMenu
		{
		/********************************************************************************************************************/

		public XGCheckboxPopup(JComponent inv, Set<XGValue> values)
		{	this.setInvoker(inv);
			this.setLocation(inv.getLocationOnScreen());
		
			XGParameter p;
			JCheckBox c;
			for(XGValue v : values)
			{	p = v.getParameter();
				c = new JCheckBox(p.getName(), p.getMaxIndex() == v.getIndex());
				c.addActionListener((ActionEvent)->v.toggleIndex(true));
				this.add(c);
			}
			this.setVisible(true);
		}
	}
}