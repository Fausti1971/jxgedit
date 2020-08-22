package gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import adress.XGAddress;
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

	public XGButton(XMLNode n, XGModule mod)
	{
		super(n, mod);
		this.address = new XGAddress(n.getStringAttribute(ATTR_VALUE), mod.getAddress());
		this.value = mod.getDevice().getValues().getFirstIncluded(this.address);
		this.button.setText(this.value.getParameter().getLongName());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 0, 0, 0.5, 0.5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0);
		this.add(this.button, gbc);
	}
}
