package gui;

import value.XGValue;import value.XGValueChangeListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class XGValueLabel extends JLabel implements XGComponent, XGValueChangeListener
{
/**
 * @param v***************************************************************************************************/

	protected final XGValue value;

	public XGValueLabel(XGValue v)throws XGComponentException
	{	this.value = v;
		if(this.value == null) throw new XGComponentException("value is null");
		this.setText(v.toString());
		this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.CENTER);
		this.setOpaque(true);
		this.addMouseListener(this);
		this.value.getValueListeners().add(this);
	}

	@Override public void mouseClicked(MouseEvent e)
	{	String s = JOptionPane.showInputDialog(e.getComponent(), this.value.getParameter(), this.value);
		try
		{	this.value.setValue(s, true);
		}
		catch(NumberFormatException ignored)
		{	LOG.warning(s);
		}
	}

	@Override public void contentChanged(XGValue v){	this.setText(v.toString());}
}
