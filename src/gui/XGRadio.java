package gui;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import module.XGModule;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTable;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;
import static value.XGValueStore.STORE;import xml.XMLNode;import static xml.XMLNodeConstants.ATTR_ADDRESS;import static xml.XMLNodeConstants.ATTR_ORIENTATION;

public class XGRadio extends JPanel implements XGValueChangeListener, XGParameterChangeListener
{	
	private static final long serialVersionUID = 1L;
	private static Map<String, Integer> ORIENTATION = new HashMap<>();
	static
	{	ORIENTATION.put("horizontal", BoxLayout.X_AXIS);
		ORIENTATION.put("vertical", BoxLayout.Y_AXIS);
	};

/*********************************************************************************************************/

	private final XGValue value;
	private final XGAddress address;
	private final int orientation;

	public XGRadio(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
		this.orientation = ORIENTATION.getOrDefault(n.getStringAttribute(ATTR_ORIENTATION), BoxLayout.X_AXIS);
		this.value = STORE.getFirstIncluded(this.address);
		this.value.addValueListener(this);
		this.value.addParameterListener(this);
		this.parameterChanged(this.value.getParameter());
	}

	@Override public void parameterChanged(XGParameter p)
	{	if(p != null)
		{	this.setLayout(new BoxLayout(this, this.orientation));
			this.setEnabled(true);
			this.setName(p.getShortName());
			this.setToolTipText(p.getName());
			XGTable t = this.value.getParameter().getTranslationTable();
			for(XGTableEntry e : t) this.add(new XGRadioButton(this.value, e));
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

	private class XGRadioButton extends JRadioButton
	{
		private static final long serialVersionUID = 1L;

/******************************************************************/

		private final XGTableEntry entry;
		private final XGValue value;

		XGRadioButton(XGValue v, XGTableEntry e)
		{	super(e.getName());
			this.setRolloverEnabled(true);
			this.setToolTipText(e.toString());
			this.entry = e;
			this.value = v;
			this.addActionListener((ActionEvent)->{this.value.editIndex(entry.getValue());});
		}

		@Override public boolean isSelected()
		{	return this.entry.getValue() == this.value.getIndex();
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
