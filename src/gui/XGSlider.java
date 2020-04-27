package gui;

import static application.XGLoggable.log;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
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
	private final Rectangle area = new Rectangle(), bar = new Rectangle();

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
	{	//super.paintComponent(g);
		if(!(g instanceof Graphics2D) || !this.isEnabled()) return;
		Graphics2D g2 = (Graphics2D)g.create();
		XGParameter p = this.value.getParameter();
		g2.addRenderingHints(AALIAS);
		Insets ins = this.getInsets();
		int w_gap = ins.left + ins.right;
		int h_gap = ins.top + ins.bottom;
		area.x = ins.left;
		area.y = ins.top;
		area.width = this.getWidth() - w_gap;
		area.height = this.getHeight() - h_gap;

// draw background
		g2.setColor(Color.white);
		g2.fillRoundRect(area.x, area.y , area.width, area.height, ROUND_RADIUS, ROUND_RADIUS);
// draw foreground
		bar.x = area.x;
		bar.y = area.y;
		bar.width = Rest.linearIO(this.value.getContent(), p.getMinValue(), p.getMaxValue(), area.x, area.width);
		bar.height = area.height;
		g2.setColor(COL_NODEFOCUS);
		g2.fillRoundRect(bar.x, bar.y, bar.width, bar.height, ROUND_RADIUS, ROUND_RADIUS);
//		g2.fillRect(area.x, area.y, w, area.height);

//		int fontMiddle = (int)(area.height / 2 + FONTSIZE / 2);
//		g2.setColor(Color.BLACK);
//		g2.drawString(p.getShortName(), GAP, fontMiddle);
//draw value
		String t;
		g2.setXORMode(Color.white);
//		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.XOR) );
		t = this.value.toString();
//		g2.setColor(Color.black);
		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D tr = fm.getStringBounds(t, g2);
		if(t != null) g2.drawString(t, (int)(area.width / 2 - tr.getWidth() / 2), (int)(area.y + area.height / 2 + tr.getHeight() / 2));
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
		{	if(bar.x + bar.width < e.getX()) changed = this.value.setContent(this.value.getContent() + 1);
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
