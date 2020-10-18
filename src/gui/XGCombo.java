package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import module.XGModule;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTable;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGCombo extends XGComponent implements XGValueChangeListener, XGParameterChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************/

	private final JComboBox<XGTableEntry> combo;
	private final XGValue value;
	private final XGAddress address;

	public XGCombo(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	super(n, mod);
		this.setLayout(new GridBagLayout());
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
		this.value = mod.getType().getDevice().getValues().get(this.address);
		this.combo = new XGComboBox<>(this.value);
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 0, 0, 0.5, 0.5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0);
		this.add(this.combo, gbc);
		this.value.addValueListener(this);
		this.value.addParameterListener(this);
		this.parameterChanged(this.value.getParameter());
	}

	@Override public void parameterChanged(XGParameter p)
	{	if(p != null)
		{	this.setName(p.getShortName());
			this.setToolTipText(p.getName());
		}
		else
		{	this.setName("n/a");
			this.setToolTipText("no parameter");
		}
		this.borderize();
		this.repaint();
	}

	@Override public void contentChanged(XGValue v)
	{	this.combo.setSelectedItem(this.value.getEntry());
	}

/*****************************************************************************************************/

	private class XGComboBox<T> extends JComboBox<XGTableEntry>
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final XGValue value;

		public XGComboBox(XGValue v)
		{	super();
			this.value = v;
			XGParameter p = this.value.getParameter();
			if(p != null)
			{	XGTable t = p.getTranslationTable();
				for(XGTableEntry e : t) this.addItem(e);
				this.setSelectedItem(t.getByIndex(this.value.getIndex()));//ruft angehängte ActionListener auf, deshalb vor addActionListener ausführen
				this.addActionListener((ActionEvent)->{this.entrySelected();});
			}
			else this.setEnabled(false);
			this.setAutoscrolls(true);
		}

		private void entrySelected()
		{	this.value.editEntry((XGTableEntry)this.getSelectedItem());
		}
	}
}
