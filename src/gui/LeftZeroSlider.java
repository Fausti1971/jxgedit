package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import application.Rest;
import parm.XGFixedParameter;
import parm.XGParameterConstants;
import value.WrongXGValueTypeException;
import value.XGValue;
import value.XGValueChangeListener;

public class LeftZeroSlider extends JComponent implements GuiConstants, KeyListener, MouseWheelListener, MouseMotionListener, MouseListener, XGParameterConstants, XGValueChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*****************************************************************************************************************************/

	private final XGAddress adress;
	private XGValue value;

	public LeftZeroSlider(XGAddress adr) throws InvalidXGAddressException
	{	this.adress = adr;
		this.valueChanged(this.value);
		setSize(SL_DIM);
		setMinimumSize(SL_DIM);
		setPreferredSize(SL_DIM);
		setMaximumSize(SL_DIM);
		setFocusable(true);
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
	}

	@Override protected void paintComponent(Graphics g)
	{	Graphics2D g2 = (Graphics2D)g;
		XGFixedParameter p = this.value.getParameter();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = 0;
		w = Rest.linearIO((int)this.value.getContent(), p.getMinValue(), p.getMaxValue(), 0, SL_W);

		g2.setColor(BACK);
		g2.fillRoundRect(0, 0 , SL_W, SL_H, SL_RADI, SL_RADI);

		g2.setColor(FORE);
		g2.drawRoundRect(0, 0 , SL_W - 1, SL_H - 1, SL_RADI, SL_RADI);
		g2.fillRoundRect(0, 0 , w - 1, SL_H - 1, SL_RADI, SL_RADI);

		g2.setColor(Color.BLACK);
		g2.drawString(p.getShortName(), GAP, FONTMIDDLE);

		String t;
		t = this.value.toString();
		if(t != null) g2.drawString(t, SL_W - GAP - g2.getFontMetrics().stringWidth(t), FONTMIDDLE);
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

	@Override public void mouseWheelMoved(MouseWheelEvent e)
	{	try
		{	if(this.value.addAndTransmit(e.getWheelRotation())) repaint();
		}
		catch(WrongXGValueTypeException e1)
		{	e1.printStackTrace();
		}
		e.consume();
	}

	@Override public void mouseDragged(MouseEvent e)
	{	XGFixedParameter p = this.value.getParameter();
		try
		{	if(this.value.setContentAndTransmit(Rest.linearIO(e.getX(), 0, this.getWidth(), p.getMinValue(), p.getMaxValue())))repaint();
		}
		catch(WrongXGValueTypeException|InvalidXGAddressException e1)
		{	e1.printStackTrace();
		}
		e.consume();
	}

	@Override public void mouseMoved(MouseEvent e)
	{
	}

	@Override public void mouseClicked(MouseEvent e)
	{	this.grabFocus();
		XGFixedParameter p = this.value.getParameter();
		if(e.getButton() == MouseEvent.BUTTON1)
		{	if(Rest.linearIO((int)this.value.getContent(), p.getMinValue(), p.getMaxValue(), 0, this.getWidth()) < e.getX())
			{	try
				{	if(this.value.addAndTransmit(1)) repaint();}
				catch(WrongXGValueTypeException e1)
				{	e1.printStackTrace();}
			}
			else
			{	try
				{	if(this.value.addAndTransmit(-1)) repaint();}
				catch(WrongXGValueTypeException e1)
				{	e1.printStackTrace();}
			}
		}
	}

	@Override public void mousePressed(MouseEvent e)
	{
	}

	@Override public void mouseReleased(MouseEvent e)
	{
	}

	@Override public void mouseEntered(MouseEvent e)
	{
	}

	@Override public void mouseExited(MouseEvent e)
	{
	}

	@Override public boolean isVisible()
	{	return this.value != null;
	}

	@Override public void contentChanged(XGValue v)
	{	if(this.isVisible()) this.repaint();
	}

	@Override public void valueChanged(XGValue v)
	{	if(this.value != null) this.value.removeListener(this);
		this.value = v;
		if(v != null)
		{	this.value.addListener(this);
			this.setToolTipText(v.getParameter().getLongName());
		}
		this.setVisible(this.isVisible());
		this.repaint();
	}

	public XGAddress getAdress()
	{	return this.adress;
	}

	public String getInfo()
	{	return this.toString();
	}
}
