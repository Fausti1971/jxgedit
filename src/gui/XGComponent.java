package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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

public interface XGComponent extends XGAddressConstants, XGUI, Configurable, MouseListener, FocusListener, XGLoggable
{
	static class Globals
	{	public MouseEvent dragEvent = null;
		public Cursor lastCursor = null;
		public boolean mousePressed = false;
	}

	Globals GLOBALS = new Globals();
//	static final XGValue DEF_VALUE = new XGFixedValue("n/a", 0);

	public static JComponent init(XGModule mod)
	{	XGTemplate t = mod.getType().getGuiTemplate();
		XMLNode xml = null;
		if(t != null) xml = t.getConfig();
		if(xml == null) return new JLabel(mod.getType() + " has no GUI");
		return new XGFrame(xml, mod);
	}

	static JComponent newItem(XMLNode n, XGModule mod) throws XGMemberNotFoundException, InvalidXGAddressException
	{	String s = n.getTag();
		JComponent c = null;
		switch(s)
		{	case TAG_VEG:		c = new XGVEG(n, mod); break;
			case TAG_AEG:		c = new XGAEG(n, mod); break;
			case TAG_PEG:		c = new XGPEG(n, mod); break;
			case TAG_MEQ:		c = new XGMEQ(n, mod); break;
			case TAG_FRAME:		c = new XGFrame(n, mod); break;
			case TAG_TABS:		c = new XGTabFrame(n, mod); break;
			case TAG_KNOB:		c = new XGKnob(n, mod); break;
			case TAG_SLIDER:	c = new XGSlider(n, mod); break;
			case TAG_RANGE:		c = new XGRange(n, mod); break;
			case TAG_COMBO:		c = new XGCombo(n, mod); break;
			case TAG_RADIO:		c = new XGRadio(n, mod); break;
			case TAG_BUTTON:	c = new XGButton(n, mod); break;
			case TAG_SELECTOR:	c = new XGProgramSelector(n, mod); break;
			case TAG_FLAGBOX:	c = new XGFlagBox(n, mod); break;
			case TAG_SCALE:		c = new XGScale(n, mod); break;
			default:			c = new XGFrame("unknown_" + s); break;
		}
		return c;
	}

/********************************************************************************************/

	default JComponent getJComponent()
	{	return (JComponent)this;
	}

	default void setBounds()
	{	JComponent j = this.getJComponent();
		j.setLayout(null);
		Rectangle r = new XGGrid(this.getConfig().getStringAttributeOrDefault(ATTR_BOUNDS, "0,0,0,0"));
		j.setBounds(GRID * r.x, GRID * r.y, GRID * r.width, GRID * r.height);
		Dimension dim = new Dimension(j.getBounds().getSize());
		j.setMinimumSize(dim);
		j.setPreferredSize(dim);
	}

	public default void borderize()
	{	JComponent j = this.getJComponent();
		if(j.isEnabled())
		{	Color c = j.getBackground().darker();
			if(j.hasFocus()) c = c.darker();
			j.setBorder(new TitledBorder(BorderFactory.createLineBorder(c, 1, true), j.getName(), TitledBorder.CENTER, TitledBorder.CENTER, SMALL_FONT, c));
		}
		else j.setBorder(null);
	}

	public default void deborderize()
	{	this.getJComponent().setBorder(null);
	}

	@Override public default void mouseClicked(MouseEvent e)
	{	if(e.getClickCount() == 2)
		{
System.out.println("doubleclick detected");
		}
	}

	@Override public default void mousePressed(MouseEvent e)
	{	XGComponent.GLOBALS.mousePressed = true;
		XGComponent.GLOBALS.dragEvent = e;
		e.consume();
	}

	@Override public default void mouseReleased(MouseEvent e)
	{	XGComponent.GLOBALS.mousePressed = false;
		XGComponent.GLOBALS.dragEvent = e;
	}

	@Override public default void mouseEntered(MouseEvent e)
	{	if(!XGComponent.GLOBALS.mousePressed) this.getJComponent().requestFocusInWindow();
	}

	@Override public default void mouseExited(MouseEvent e)
	{
	}

	@Override public default void focusLost(FocusEvent e)
	{	this.borderize();
		this.getJComponent().repaint();
	}

	@Override public default void focusGained(FocusEvent e)
	{	this.borderize();
		this.getJComponent().repaint();
	}

/************************************************************************************************************/

	class XGGrid extends Rectangle
	{
		private static final long serialVersionUID = 1L;

		public XGGrid(String s)
		{	super(0,0,0,0);
			if(s == null)
			{	LOG.warning("no grid found");
				return;
			}
			String[] str = s.split(",");
			if(str.length < 4)
			{	LOG.warning("grid incomplete");
				return;
			}
			this.x = Integer.parseInt(str[0]);
			this.y = Integer.parseInt(str[1]);
			this.width = Integer.parseInt(str[2]);
			this.height = Integer.parseInt(str[3]);
		}
	}
}
