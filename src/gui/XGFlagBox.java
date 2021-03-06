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
import static value.XGValueStore.STORE;import xml.XMLNode;

public class XGFlagBox extends XGFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************/

	private final JButton button = new JButton();
	private final Set<XGValue> values = new LinkedHashSet<>();

	public XGFlagBox(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	super(n);
		this.setLayout(new GridBagLayout());

		XGAddress adr;
		XGValue val;
		for(XMLNode f : n.getChildNodes(TAG_FLAG))
		{	adr = new XGAddress(f.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
			val = STORE.get(adr);
			if(val == null) throw new XGMemberNotFoundException(mod + " has no value for address " + adr);
			this.values.add(val);
		}
		this.button.addActionListener((ActionEvent)->{new XGPopup(this, this.values);});
		this.button.setText("select...");
		this.addFocusListener(this);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 0, 0, 0.5, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);
		this.add(this.button, gbc);
	}
}