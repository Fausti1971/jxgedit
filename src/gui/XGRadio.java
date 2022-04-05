package gui;

import java.awt.*;
import javax.swing.*;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTable;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;

public class XGRadio extends XGFrame implements XGValueChangeListener, XGParameterChangeListener
{
	private static final long serialVersionUID = 1L;
	private static final java.awt.Dimension MINDIM = new Dimension(132, 44);

/*********************************************************************************************************/

	private final XGValue value;
	private final int orientation;

	public XGRadio(XGValue val)
	{	this(val, BoxLayout.Y_AXIS);//TODO: bringe dem XGFrame noch Orientierung bei...
	}

	public XGRadio(XGValue val, int orientation)
	{	super(true);
		this.orientation = orientation;
		this.value = val;
		if(val == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			return;
		}
		this.setAlignmentY(XGFrame.TOP_ALIGNMENT);
		this.setAlignmentX(XGFrame.LEFT_ALIGNMENT);
		this.value.getValueListeners().add(this);
		this.parameterChanged(this.value.getParameter());
	}

	@Override public void parameterChanged(XGParameter p)
	{	if(p != null)
		{	this.setEnabled(true);
//			this.setName(p.getShortName());
			this.setToolTipText(p.getName());
			XGTable t = this.value.getParameter().getTranslationTable();
			int x = 0, y = 0;
			if(this.orientation == BoxLayout.X_AXIS) for(XGTableEntry e : t) this.add(new XGRadioButton(this.value, e), new int[]{x++,0,1,1});
			if(this.orientation == BoxLayout.Y_AXIS) for(XGTableEntry e : t) this.add(new XGRadioButton(this.value, e), new int[]{0,y++,1,1});
		}
		else
		{	this.setEnabled(false);
			this.setName("n/a");
		}
	}

	@Override public void contentChanged(XGValue v){	this.repaint();}

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
			this.setOpaque(false);
			this.setToolTipText(e.toString());
			this.entry = e;
			this.value = v;
			this.addActionListener((ActionEvent)->{this.value.setEntry(entry, false, true);});
		}

		@Override public boolean isSelected(){	return this.entry.getValue() == this.value.getValue();}

		@Override public void paint(Graphics g)
		{	this.setSelected(this.isSelected());
			super.paint(g);
		}

		@Override public String toString(){	return this.entry.toString();}
	}
}
