package gui;

import value.XGValue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class XGValueLabel extends XGLabel implements MouseListener
{
/**
 * @param v***************************************************************************************************/

	private final XGValue value;

	public XGValueLabel(XGValue v)
	{	super("");
		this.value = v;
		this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		this.addMouseListener(this);
	}

	@Override public void mouseClicked(MouseEvent e)
	{	int v = this.value.getValue();
		String s = JOptionPane.showInputDialog(e.getComponent(), "Value:", this.getText());
		try
		{	this.value.setValue(s, true);
		}
		catch(NumberFormatException ex)
		{	this.value.setValue(v, false, false);
		}
	}

	@Override public void mousePressed(MouseEvent e){}

	@Override public void mouseReleased(MouseEvent e){}

	@Override public void mouseEntered(MouseEvent e){}

	@Override public void mouseExited(MouseEvent e){}
}
