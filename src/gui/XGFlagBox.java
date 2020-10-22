package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JButton;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import module.XGModule;
import value.XGValue;
import xml.XMLNode;

public class XGFlagBox extends XGComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************/

	private final JButton button;
	private final Set<XGValue> values = new LinkedHashSet<>();

	public XGFlagBox(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	super(n, mod);
		this.setLayout(new GridBagLayout());
		XGAddress adr;
		XGValue val;
		for(XMLNode f : n.getChildNodes(TAG_FLAG))
		{	adr = new XGAddress(f.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
			val = mod.getType().getDevice().getValues().get(adr);
			if(val == null) throw new XGMemberNotFoundException(mod + " has no value for address " + adr);
			this.values.add(val);
		}
		this.button = new JButton(n.getStringAttribute(ATTR_NAME));
		this.button.addActionListener((ActionEvent)->{new XGPopup(this.button, this.values);});
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 0, 0, 0.5, 0.5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0);
		this.add(this.button, gbc);
		this.addFocusListener(this);
	}
}