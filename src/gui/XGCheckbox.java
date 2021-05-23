package gui;

import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;import value.XGValue;
import value.XGValueChangeListener;
import javax.swing.*;import javax.swing.border.LineBorder;import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;import java.awt.event.ItemEvent;

public class XGCheckbox extends JCheckBox implements XGComponent, XGParameterChangeListener, XGValueChangeListener
{	private static final Dimension MIN_DIM = new Dimension(GRID * 3, GRID * 2);

/*******************************************************************************************/

	private final XGValue value;

	public XGCheckbox(XGValue val)
	{	this.setPreferredSize(MIN_DIM);
		this.setMinimumSize(MIN_DIM);
		this.value = val;
		if(this.value == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			return;
		}
		if(this.value.getOpcode().isMutable()) this.value.getParameterListeners().add(this);
		this.value.getValueListeners().add(this);
		this.addActionListener((ActionEvent e)->{this.value.toggleIndex();});
		this.parameterChanged(this.value.getParameter());
		this.contentChanged(this.value);
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.setName(p.getShortName());
		this.setText(p.getName());
		this.setToolTipText(p.getName());
		this.setVisible(p != XGParameterConstants.NO_PARAMETER);
		this.setEnabled(p.isValid());
		this.borderize();//TODO: finde heraus, warum das nicht funktioniert...
	}

	@Override public void contentChanged(XGValue v)
	{	this.setSelected(v.getParameter().getMaxIndex() == v.getIndex());
		this.repaint();
	}
}
