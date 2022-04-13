package gui;

import parm.XGParameter;import value.XGValue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class XGValueLabel extends JLabel implements XGComponent
{
/**
 * @param v***************************************************************************************************/

	protected final XGValue value;

	public XGValueLabel(XGValue v)
	{	this.value = v;
		this.setText(v.toString());
		this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.CENTER);
		this.addMouseListener(this);
	}

	@Override public void mouseClicked(MouseEvent e)
	{	String s = JOptionPane.showInputDialog(e.getComponent(), this.value.getParameter(), this.getText());
		try
		{	this.value.setValue(s, true);
		}
		catch(NumberFormatException ignored)
		{
		}
	}
}
