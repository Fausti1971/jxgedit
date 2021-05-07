package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import adress.InvalidXGAddressException;
import adress.XGAddressConstants;
import adress.XGMemberNotFoundException;
import application.Configurable;
import application.XGLoggable;
import module.XGModule;
import xml.XMLNode;

public interface XGComponent extends XGAddressConstants, XGUI, MouseListener, FocusListener, XGLoggable
{
	//public static JComponent init(XGModule mod)
	//{	XGTemplate t = mod.getType().getGuiTemplate();
	//	XMLNode xml = null;
	//	if(t != null) xml = t.getConfig();
	//	if(xml == null) return new JLabel(mod.getType() + " has no GUI");
	//	return new XGFrame(xml, mod);
	//}

/********************************************************************************************/

	default JComponent getJComponent()
	{	return (JComponent)this;
	}

//	default void setComponentEnabled(boolean b)
//	{	this.getJComponent().setEnabled(b);
//		for(Component c : this.getJComponent().getComponents()) c.setVisible(b);
//	}

	default void borderize()
	{	JComponent j = this.getJComponent();
		if(j.isEnabled())
		{	Color c = j.getBackground().darker();
			if(j.hasFocus()) c = c.darker();
			j.setBorder(new TitledBorder(BorderFactory.createLineBorder(c, 1, true), j.getName(), TitledBorder.CENTER, TitledBorder.TOP, SMALL_FONT, c));
		}
		else j.setBorder(null);
	}

	default void deborderize()
	{	this.getJComponent().setBorder(null);
	}

	default Rectangle getContentArea()
	{	Rectangle r = new Rectangle(this.getJComponent().getBounds());
		Insets ins = this.getJComponent().getInsets();
		r.x = ins.left;
		r.y = ins.top;
		r.width -= (ins.right + ins.left);
		r.height -= (ins.top + ins.bottom);
		return r;
	}

	@Override  default void mouseClicked(MouseEvent e)
	{	if(e.getClickCount() == 2)
		{
System.out.println("doubleclick detected");
		}
	}

	@Override  default void mousePressed(MouseEvent e)
	{	VARIABLES.mousePressed = true;
		VARIABLES.dragEvent = e;
		e.consume();
	}

	@Override  default void mouseReleased(MouseEvent e)
	{	VARIABLES.mousePressed = false;
		VARIABLES.dragEvent = e;
	}

	@Override  default void mouseEntered(MouseEvent e)
	{	if(!VARIABLES.mousePressed) this.getJComponent().requestFocusInWindow();
	}

	@Override  default void mouseExited(MouseEvent e)
	{
	}

	@Override  default void focusLost(FocusEvent e)
	{	this.borderize();
		this.getJComponent().repaint();
	}

	@Override  default void focusGained(FocusEvent e)
	{	this.borderize();
		this.getJComponent().repaint();
	}
}
