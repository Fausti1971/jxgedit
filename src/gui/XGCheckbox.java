package gui;

import parm.XGParameter;
import parm.XGParameterChangeListener;
import value.XGValue;
import value.XGValueChangeListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class XGCheckbox extends XGFrame implements XGParameterChangeListener, XGValueChangeListener
{	private static final Dimension MIN_DIM = new Dimension(3, 2);

/*******************************************************************************************/

	private final XGValue value;
	private final JCheckBox checkbox = new JCheckBox();

	public XGCheckbox(XGValue val)
	{	super (true);
		this.value = val;
		if(this.value == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			return;
		}
		if(this.value.getType().hasMutableParameters()) this.value.getParameterListeners().add(this);
		this.value.getValueListeners().add(this);
		this.add(this.checkbox, "0,0,1,1");
		this.checkbox.addActionListener((ActionEvent e)->this.value.toggleIndex(true));
		this.parameterChanged(this.value.getParameter());
		this.contentChanged(this.value);
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.checkbox.setText(p.getShortName());
		this.checkbox.setToolTipText(p.getName());
		this.setToolTipText(p.getName());
		this.setVisible(p.isValid());
		this.setEnabled(p.isValid());
	}

	@Override public void contentChanged(XGValue v)
	{	this.checkbox.setSelected(v.getParameter().getMaxIndex() == v.getIndex());
		this.repaint();
	}
}
