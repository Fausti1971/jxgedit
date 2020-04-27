package gui;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import value.ChangeableContent;
import value.XGValue;
import xml.XMLNode;

public class XGSpinner extends JSpinner implements ChangeListener, XGComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**************************************************************************************************/

	private ChangeableContent<Integer> value;
	private final XMLNode config = new XMLNode("spinner", null);

	public XGSpinner(String name, ChangeableContent<Integer> v, int min, int max, int step)
	{	super(new SpinnerNumberModel((int)v.getContent(), min, max, step));
		this.value = v;
		this.addChangeListener(this);
		this.setName(name);
		this.setSizes(4, 1);
		this.borderize();
//		this.setAlignmentX(0.5f);
//		this.setAlignmentY(0.5f);
	}

	@Override public void stateChanged(ChangeEvent e)
	{	this.value.setContent((Integer)this.getModel().getValue());
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public JComponent getJComponent()
	{	return this;
	}

	@Override public XGValue getValue()
	{	return null;
	}
}
