package gui;

import static application.XGLoggable.log;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComponent;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import application.JXG;
import application.Rest;
import device.XGDevice;
import msg.XGMessageParameterChange;
import parm.XGParameter;
import parm.XGParameterConstants;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGSlider extends JComponent implements XGComponent, KeyListener, XGParameterConstants, XGValueChangeListener, MouseMotionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	private final static int PREF_W = 4, PREF_H = 1;

/*****************************************************************************************************************************/

	private final XGAddress address;
	private final XGValue value;
	private final XMLNode config;
	private final Rectangle barArea = new Rectangle(), valueArea = new Rectangle();
	private XGParameter parameter;
	private String valueString;

	public XGSlider(XMLNode n, XGAddressableSet<XGValue> set)
	{	this.config = n;
		this.address = new XGAddress(n.getStringAttribute(ATTR_VALUE), null);
		XGValue v = set.getFirstValid(this.address);
		this.setEnabled(true);
		if(v == null)
		{	v = DEF_VALUE;
			this.setEnabled(false);
		}
		this.value = v;
		this.value.addListener(this);
		this.setName(this.value.getParameter().getShortName());
		this.setToolTipText(this.value.getParameter().getLongName());
		this.setSizes(PREF_W,  PREF_H);
		this.setFocusable(true);
		this.borderize();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addFocusListener(this);
		log.info("slider initialized: " + this.getName());
		}

	@Override protected void paintComponent(Graphics g)
	{	if(!(g instanceof Graphics2D) || !this.isEnabled()) return;
		Graphics2D g2 = (Graphics2D)g.create();
		this.parameter = this.value.getParameter();
		g2.addRenderingHints(AALIAS);
		g2.setFont(FONT);
		Insets ins = this.getInsets();
		this.valueString = this.value.toString();
		int w_gap = ins.left + ins.right;
		int h_gap = ins.top + ins.bottom;
		this.valueArea.setBounds(g2.getFontMetrics().getStringBounds(this.valueString, g2).getBounds());
		this.valueArea.x = this.getWidth() / 2 - this.valueArea.width / 2;
		this.valueArea.y = this.getHeight() - ins.bottom - this.valueArea.height;

		this.barArea.x = ins.left;
		this.barArea.y = ins.top;
		this.barArea.width = this.getWidth() - w_gap;
		this.barArea.height = this.getHeight() - h_gap - this.valueArea.height;

// draw background
		g2.setColor(COL_BAR_BACK);
		g2.fillRoundRect(barArea.x, barArea.y, barArea.width, barArea.height, ROUND_RADIUS, ROUND_RADIUS);
// draw foreground
		int o = Rest.linearIO(this.parameter.getOrigin(), this.parameter.getMinValue(), this.parameter.getMaxValue(), 0, this.barArea.width);
		int w = Rest.linearIO(this.value.getContent(), this.parameter.getMinValue(), this.parameter.getMaxValue(), 0, this.barArea.width) - o;
		g2.setColor(COL_BAR_FORE);
		g2.fillRoundRect(this.barArea.x + Math.min(o, o + w), this.barArea.y, Math.abs(w), this.barArea.height, ROUND_RADIUS, ROUND_RADIUS);
// draw marker
//		g2.fillRoundRect(w + DEF_STROKEWIDTH / 2, barArea.y, DEF_STROKEWIDTH, barArea.height, ROUND_RADIUS, ROUND_RADIUS);
//draw value
		g2.setColor(COL_NODE_TEXT);
		if(this.valueString != null) g2.drawString(this.valueString, this.valueArea.x, this.valueArea.y + this.valueArea.height);
		g2.dispose();
	}

	@Override public void keyTyped(KeyEvent e)
	{
	}

	@Override public void keyPressed(KeyEvent e)
	{
	}

	@Override public void keyReleased(KeyEvent e)
	{
	}

	@Override public void mouseDragged(MouseEvent e)
	{	int distance = e.getX() - JXG.dragEvent.getX();
//		XGParameter p = this.value.getParameter();
//		int range = p.getMaxValue() - p.getMinValue();
		boolean changed = this.getValue().setContent(this.getValue().getContent() + distance);
		if(changed)
		{	XGDevice dev = this.getValue().getSource().getDevice();
			try
			{	new XGMessageParameterChange(dev, dev.getMidi(), this.getValue()).transmit();
			}
			catch(InvalidXGAddressException | InvalidMidiDataException e1)
			{	e1.printStackTrace();
			}
		}
		JXG.dragEvent = e;
		e.consume();
	}

	@Override public void mouseMoved(MouseEvent e)
	{
	}

	@Override public void mouseClicked(MouseEvent e)
	{	XGDevice dev = this.value.getSource().getDevice();
		boolean changed = false;
		if(e.getButton() == MouseEvent.BUTTON1)
		{	if(barArea.x + barArea.width < e.getX()) changed = this.value.setContent(this.value.getContent() + 1);
			else changed = this.value.setContent( this.value.getContent() - 1);
			if(changed)
			{	try
				{	new XGMessageParameterChange(dev, dev.getMidi(), this.value).transmit();
				}
				catch(InvalidXGAddressException | InvalidMidiDataException e1)
				{	e1.printStackTrace();
				}
			}
		}
		e.consume();
	}

	@Override public void contentChanged(XGValue v)
	{	this.repaint();
	}

	@Override public JComponent getJComponent()
	{	return this;
	}

	@Override public XGValue getValue()
	{	return this.value;
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}


	@Override public boolean isManagingFocus()
	{	return true;
	}
	
	@Override public boolean isFocusTraversable()
	{	return true;
	}

}
