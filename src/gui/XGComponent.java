package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.NoSuchElementException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;
import adress.InvalidXGAddressException;
import adress.XGAddressConstants;
import adress.XGMemberNotFoundException;
import application.Configurable;
import application.XGLoggable;
import application.XGStrings;
import module.XGModule;
import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;

public abstract class XGComponent extends JComponent implements XGAddressConstants, XGUI, Configurable, MouseListener, FocusListener, XGLoggable
{
	private static final long serialVersionUID = 1L;
	public static MouseEvent dragEvent = null;
	public static Cursor lastCursor = null;
	public static boolean mousePressed = false;

	static final XGValue DEF_VALUE = new XGFixedValue("n/a", 0);

	public static JComponent init(XGModule mod) throws NoSuchElementException
	{	XGTemplate t = mod.getType().getGuiTemplate();
		XMLNode xml = null;
		if(t != null) xml = t.getConfig();
		if(xml == null) throw new NoSuchElementException(mod.getType() + " has no GUI");
		return new XGFrame(xml, mod);
	}

	protected static JComponent newItem(XMLNode n, XGModule mod) throws XGMemberNotFoundException, InvalidXGAddressException
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

	protected final XMLNode config;

	public XGComponent(XMLNode n)
	{	this.config = n;
	}

	public XGComponent(String text)
	{	this.config = new XMLNode(text.replaceAll(XGStrings.REGEX_NON_ALNUM, XGStrings.TEXT_REPLACEMENT));
		this.setName(text);
	}

	public XGComponent(XMLNode n, XGModule mod)
	{	this.config = n;
		this.setBounds();
		this.setName(n.getStringAttributeOrDefault(ATTR_NAME, mod.toString()));
	}

	private void setBounds()
	{	Rectangle r = new XGGrid(this.config.getStringAttributeOrDefault(ATTR_BOUNDS, "0,0,0,0"));
		super.setBounds(GRID * r.x, GRID * r.y, GRID * r.width, GRID * r.height);
		Dimension dim = new Dimension(this.getBounds().getSize());
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
	}

	public void borderize()
	{	if(this.isEnabled())
		{	Color c = this.getBackground();
			if(c == null) c = COL_PANEL_BACK;
			c = c.darker();
			if(this.hasFocus()) c = c.darker();
			this.setBorder(new TitledBorder(BorderFactory.createLineBorder(c, 1, true), this.getName(), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, SMALL_FONT, c));
		}
		else this.setBorder(null);
	}

	public void deborderize()
	{	this.setBorder(null);
	}

	@Override public boolean isFocusable()
	{	return this.isEnabled();
	}

	@Override public boolean isManagingFocus()
	{	return this.isEnabled();
	}
	
	@Override public boolean isFocusTraversable()
	{	return this.isEnabled();
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public void mouseClicked(MouseEvent e)
	{	if(e.getClickCount() == 2)
		{
System.out.println("doubleclick detected");
		}
	}

	@Override public void mousePressed(MouseEvent e)
	{	XGComponent.mousePressed = true;
		XGComponent.dragEvent = e;
		e.consume();
	}

	@Override public void mouseReleased(MouseEvent e)
	{	XGComponent.mousePressed = false;
		XGComponent.dragEvent = e;
	}

	@Override public void mouseEntered(MouseEvent e)
	{	if(!XGComponent.mousePressed) this.requestFocusInWindow();
	}

	@Override public void mouseExited(MouseEvent e)
	{
	}

	@Override public void focusLost(FocusEvent e)
	{	this.borderize();
		this.repaint();
	}

	@Override public void focusGained(FocusEvent e)
	{	this.borderize();
		this.repaint();
	}

/************************************************************************************************************/

	private class XGGrid extends Rectangle
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
