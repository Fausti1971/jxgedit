package gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import module.XGModule;
import value.XGValue;
import xml.XMLNode;

public class XGButton extends XGComponent
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*******************************************************************************/

	private JButton button = new JButton();
	private final XGValue value;
	private final XGAddress address;

	public XGButton(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{
		super(n, mod);
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
		this.value = mod.getType().getDevice().getValues().getFirstIncluded(this.address);
		this.button.setText(this.value.getParameter().getName());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 0, 0, 0.5, 0.5, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0);
		this.add(this.button, gbc);

//		this.logInitSuccess();
	}
}
