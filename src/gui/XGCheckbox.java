package gui;

import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;import value.XGValue;
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
	{	super();
		//this.setPreferredSize(MIN_DIM);
		//this.setMinimumSize(MIN_DIM);
		this.value = val;
		if(this.value == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			return;
		}
		if(this.value.getType().hasMutableParameters()) this.value.getParameterListeners().add(this);
		this.value.getValueListeners().add(this);
//		this.setFont(MEDIUM_FONT);
		this.add(this.checkbox);
		this.checkbox.addActionListener((ActionEvent e)->{this.value.toggleIndex(true);});
		this.parameterChanged(this.value.getParameter());
		this.contentChanged(this.value);
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.setName(p.getShortName());
		this.checkbox.setText(p.getName());
		this.setToolTipText(p.getName());
		this.setVisible(p.isValid());
		this.setEnabled(p.isValid());
	}

	@Override public void contentChanged(XGValue v)
	{	this.checkbox.setSelected(v.getParameter().getMaxIndex() == v.getIndex());
		this.repaint();
	}
}
