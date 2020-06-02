package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;
import adress.XGAddress;
import adress.XGAddressConstants;
import application.Configurable;
import application.JXG;
import module.XGModule;
import value.XGValue;
import xml.XMLNode;

public abstract class XGComponent extends JComponent implements XGAddressConstants, GuiConstants, Configurable, MouseListener, FocusListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final XGValue DEF_VALUE = new XGValue("n/a", 0);
	private static final int AXIS_X = 1, AXIS_Y = 2, AXIS_XY = 3, AXIS_RAD = 4, DEF_AXIS = 1;

	public static XGComponent init(XGModule mod)
	{	XGTemplate t = mod.getGuiTemplate();
		XMLNode xml = null;
		if(t != null) xml = t.getXMLNode();
		if(xml == null) return new XGFrame("no template");
		return newItem(xml, mod);
	}

	private static XGComponent newItem(XMLNode n, XGModule mod)
	{	String s = n.getTag();
		XGComponent c = null;
		switch(s)
		{	case TAG_AUTO:		break;
			case TAG_ENVELOPE:	c = new XGEnvelope(n, mod); break;
			case TAG_ENVPOINT:	break;
			case TAG_FRAME:		c = new XGFrame(n, mod); break;
			case TAG_KNOB:		c = new XGKnob(n, mod); break;
			case TAG_SLIDER:	c = new XGSlider(n, mod); break;
			case TAG_COMBO:		c = new XGCombo(n, mod); break;
			case TAG_RADIO:		c = new XGRadio(n, mod); break;
			default:			c = new XGFrame("unknown " + s); break;
		}
		if(c != null) for(XMLNode x : n.getChildNodes()) c.add(newItem(x, mod));
		return c;
	}

	static int getAxis(String s)
	{	if(s == null) return DEF_AXIS;
		if(s.equals("x")) return AXIS_X;
		if(s.equals("y")) return AXIS_Y;
		if(s.equals("xy")) return AXIS_XY;
		if(s.equals("rad")) return AXIS_RAD;
		return 0;
	}

/********************************************************************************************/

	protected final XMLNode config;
	protected final XGValue value;
	protected final XGAddress address;

	public XGComponent(String text)
	{	this.config = new XMLNode(text, null);
		this.value = null;
		this.address = XGALLADDRESS;
		this.setName(text);
	}

	public XGComponent(XMLNode n, XGModule mod)
	{	this.config = n;
		this.setBounds();
		this.address = new XGAddress(n.getStringAttribute(ATTR_VALUE), mod.getAddress());
		XGValue v = mod.getDevice().getValues().getFirstIncluded(this.address);
		if(v == null) v = DEF_VALUE;
		this.value = v;
		this.setName(n.getStringAttribute(ATTR_NAME, mod.getName()));
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
	{	String s = this.config.getStringAttribute(ATTR_GB_GRID);
		String[] str = s.split(",");
		super.setBounds(GRID * Integer.parseInt(str[0]), GRID * Integer.parseInt(str[1]), GRID * Integer.parseInt(str[2]), GRID * Integer.parseInt(str[3]));
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

	@Override public boolean isFocusable()
	{	return this.isEnabled();
	}

	public  void deborderize()
	{	this.setBorder(null);
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
		{	System.out.println("doubleclick detected");
		}
	}

	@Override public void mousePressed(MouseEvent e)
	{	JXG.dragEvent = e;
		e.consume();
	}

	@Override public void mouseReleased(MouseEvent e)
	{	JXG.dragEvent = e;
	}

	@Override public void mouseEntered(MouseEvent e)
	{	if(JXG.dragEvent == null || JXG.dragEvent.getID() == MouseEvent.MOUSE_RELEASED) this.requestFocusInWindow();
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
}
