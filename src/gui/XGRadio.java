package gui;

import java.awt.Graphics;
import javax.swing.*;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTable;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;

public class XGRadio extends JPanel implements XGValueChangeListener, XGParameterChangeListener, XGComponent
{
	private static final long serialVersionUID = 1L;
	private static final java.awt.Dimension MINDIM = new java.awt.Dimension(132, 88);

/*********************************************************************************************************/

	private final XGValue value;
	private final int orientation;

	public XGRadio(XGValue val)
	{	this(val, BoxLayout.Y_AXIS);
	}

	public XGRadio(XGValue val, int orientation)
	{	this.orientation = orientation;
		this.value = val;
		if(val == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			return;
		}
		this.setMinimumSize(MINDIM);
		this.setPreferredSize(MINDIM);
//		this.value.getValueListeners().add(this);
		this.value.getValueListeners().add(this);
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
		this.borderize();
	}

	@Override public void contentChanged(XGValue v)
	{	this.repaint();
	}

	//@Override protected void paintComponent(Graphics g)
	//{	if(this.isEnabled()) super.paintComponent(g);
	//}

/****************************************************************************************************/

	private class XGRadioButton extends JRadioButton
	{
		private static final long serialVersionUID = 1L;

/******************************************************************/

		private final XGTableEntry entry;
		private final XGValue value;

		XGRadioButton(XGValue v, XGTableEntry e)
		{	super(e.getName());
			this.setFont(MEDIUM_FONT);
			this.setRolloverEnabled(true);
			this.setToolTipText(e.toString());
			this.entry = e;
			this.value = v;
			this.addActionListener((ActionEvent)->{this.value.setEntry(entry, false, true);});
		}

		@Override public boolean isSelected()
		{	return this.entry.getValue() == this.value.getValue();
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
