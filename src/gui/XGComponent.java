package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.NoSuchElementException;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;
import adress.XGAddressConstants;
import adress.XGMemberNotFoundException;
import application.Configurable;
import application.XGLoggable;
import application.XGStrings;
import module.XGModule;
import value.XGValue;
import xml.XMLNode;

public abstract class XGComponent extends JComponent implements XGAddressConstants, GuiConstants, Configurable, MouseListener, FocusListener, XGLoggable
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static MouseEvent dragEvent = null;

	static final XGValue DEF_VALUE = new XGValue("n/a", 0);

	public static XGComponent init(XGModule mod) throws NoSuchElementException
	{	XGTemplate t = mod.getType().getGuiTemplate();
		XMLNode xml = null;
		if(t != null) xml = t.getXMLNode();
		if(xml == null) throw new NoSuchElementException();
		return new XGFrame(xml, mod);
	}

	protected static XGComponent newItem(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	String s = n.getTag();
		XGComponent c = null;
		switch(s)
		{	case TAG_ENVELOPE:	c = new XGEnvelope(n, mod); break;
			case TAG_FRAME:		c = new XGFrame(n, mod); break;
			case TAG_KNOB:		c = new XGKnob(n, mod); break;
			case TAG_SLIDER:	c = new XGSlider(n, mod); break;
			case TAG_RANGE:		c = new XGRange(n, mod); break;
			case TAG_COMBO:		c = new XGCombo(n, mod); break;
			case TAG_RADIO:		c = new XGRadio(n, mod); break;
			case TAG_BUTTON:	c = new XGButton(n, mod); break;
			case TAG_SELECTOR:	c = new XGProgramSelector(n, mod); break;
			default:			c = new XGFrame("unknown_" + s); break;
		}
		return c;
	}

/********************************************************************************************/

	protected final XMLNode config;

	public XGComponent(String text)
	{	this.config = new XMLNode(text.replaceAll(XGStrings.REGEX_NON_ALNUM, XGStrings.TEXT_REPLACEMENT));
		this.setName(text);
	}

	public XGComponent(XMLNode n, XGModule mod)
	{	this.config = n;
		this.setBounds();
		this.setName(n.getStringAttributeOrDefault(ATTR_NAME, mod.toString()));
	}

	@Override public Component add(Component comp)
	{	Dimension dim = this.getSize();
		Insets ins = this.getInsets();
		comp.setLocation(comp.getX() + ins.left, comp.getY() + ins.top);
		super.add(comp);
		dim.width = Math.max(dim.width, comp.getX() + comp.getWidth() + ins.right);
		dim.height = Math.max(dim.height, comp.getY() + comp.getHeight() + ins.bottom);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setSize(dim);
		return comp;
	}

	public void setBounds()
	{	Rectangle r = new XGGrid(this.config.getStringAttribute(ATTR_GRID));
		super.setBounds(GRID * r.x, GRID * r.y, GRID * r.width, GRID * r.height);
		Dimension dim = new Dimension(this.getBounds().getSize());
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
	}

	public void borderize()
	{	if(this.isEnabled())
		{	if(this.hasFocus())
				this.setBorder(new TitledBorder(focusLineBorder, this.getName(), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, FONT, COL_NODE_FOCUS));
			else
				this.setBorder(new TitledBorder(defaultLineBorder, this.getName(), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, FONT, COL_BORDER));
		}
		else this.setBorder(new TitledBorder(defaultLineBorder, "n/a", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, FONT, COL_BORDER));
	}

	public  void deborderize()
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
	{	XGComponent.dragEvent = e;
		e.consume();
	}

	@Override public void mouseReleased(MouseEvent e)
	{	XGComponent.dragEvent = e;
	}

	@Override public void mouseEntered(MouseEvent e)
	{	if(XGComponent.dragEvent == null || XGComponent.dragEvent.getID() == MouseEvent.MOUSE_RELEASED) this.requestFocusInWindow();
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
			{	LOG.warning("grid incomplete");;
				return;
			}
			this.x = Integer.parseInt(str[0]);
			this.y = Integer.parseInt(str[1]);
			this.width = Integer.parseInt(str[2]);
			this.height = Integer.parseInt(str[3]);
		}
	}
}
