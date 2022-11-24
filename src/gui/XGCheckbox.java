package gui;

import parm.XGParameter;
import parm.XGParameterChangeListener;
import value.XGValue;
import value.XGValueChangeListener;import xml.XMLNode;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class XGCheckbox extends XGFrame implements XGParameterChangeListener, XGValueChangeListener, XGValueComponent
{

/*******************************************************************************************/

	private final XGValue value;
	private final JCheckBox checkbox = new JCheckBox();

	public XGCheckbox(XGValue val, XMLNode node)throws XGComponentException
	{	super (node.getStringAttributeOrDefault(ATTR_LABEL, ""));
		this.value = val;
		if(this.value == null) throw new XGComponentException("value is null");
		if(this.value.getType().hasMutableParameters()) this.value.getParameterListeners().add(this);
		this.value.getValueListeners().add(this);
		this.add(this.checkbox, "0,0,1,1");
		this.checkbox.addActionListener((ActionEvent e)->this.value.toggleIndex(true));
		this.checkbox.addMouseListener(this);
		this.parameterChanged(this.value.getParameter());
		this.contentChanged(this.value);
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.checkbox.setText(p.getTranslationTable().getByIndex(p.getMaxIndex()).getName());
		this.checkbox.setToolTipText(p.getName());
		this.setToolTipText(p.getName());
		this.setVisible(p.isValid());
		this.setEnabled(p.isValid());
	}

	@Override public void contentChanged(XGValue v)
	{	this.checkbox.setSelected(v.getParameter().getMaxIndex() == v.getIndex());
		this.repaint();
	}

	public XGValue[] getValues()
	{	return new XGValue[]{this.value};
	}
}
