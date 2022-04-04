package gui;

import value.XGValue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class XGValueLabel extends JLabel implements MouseListener
{
/**
 * @param v***************************************************************************************************/

	private final XGValue value;

	public XGValueLabel(XGValue v)
	{	this.value = v;
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

	@Override public void mousePressed(MouseEvent e){}

	@Override public void mouseReleased(MouseEvent e){}

	@Override public void mouseEntered(MouseEvent e){}

	@Override public void mouseExited(MouseEvent e){}
}
