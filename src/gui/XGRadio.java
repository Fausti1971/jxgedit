package gui;

import static application.XGLoggable.LOG;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import adress.XGAddress;
import module.XGModule;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTable;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGRadio extends XGFrame implements XGValueChangeListener, XGParameterChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*********************************************************************************************************/

	private final XGValue value;
	private final XGAddress address;

	public XGRadio(XMLNode n, XGModule mod)
	{	super(n, mod);
		this.address = new XGAddress(n.getStringAttribute(ATTR_VALUE), mod.getAddress());
		this.value = mod.getDevice().getValues().getFirstIncluded(this.address);
		this.value.addValueListener(this);
		this.value.addParameterListener(this);
		this.parameterChanged(this.value.getParameter());
		this.borderize();
		LOG.info("radio initialized: " + this.getName());
	}

	@Override public void parameterChanged(XGParameter p)
	{	if(p != null)
		{	this.setLayout(new GridBagLayout());
			this.setEnabled(true);
			this.setName(p.getShortName());
			this.setToolTipText(p.getLongName());
			XGTable t = this.value.getParameter().getTranslationTable();
			int i = 0;
			GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0);
			for(XGTableEntry e : t)
			{	gbc.gridy = i++;
				this.add(new XGRadioButton(this.value, e), gbc);
			}
		}
		else
		{	this.setEnabled(false);
			this.setName("n/a");
		}
	}

	@Override public void contentChanged(XGValue v)
	{	this.repaint();
	}

	@Override protected void paintComponent(Graphics g)
	{	if(this.isEnabled()) super.paintComponent(g);
	}

/****************************************************************************************************/

	private class XGRadioButton extends JCheckBox implements ActionListener
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

/******************************************************************/

		private final XGTableEntry entry;
		private final XGValue value;

		XGRadioButton(XGValue v, XGTableEntry e)
		{	super(e.getName());
			this.setRolloverEnabled(true);
			this.entry = e;
			this.value = v;
			this.addActionListener(this);
		}

		@Override public boolean isSelected()
		{	return this.entry.getValue() == this.value.getContent();
		}

		@Override public void actionPerformed(ActionEvent e)
		{	boolean changed = this.value.setContent(entry.getValue());
			if(changed) this.value.transmit();
		}

		@Override public void paint(Graphics g)
		{	this.setSelected(this.isSelected());
			super.paint(g);
		}

		@Override public String toString()
		{	return this.entry.toString();
		}
	}
}
