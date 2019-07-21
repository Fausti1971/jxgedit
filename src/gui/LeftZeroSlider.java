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
import adress.InvalidXGAdressException;
import adress.XGAdress;
import application.Rest;
import obj.XGObjectType;
import parm.XGParameter;
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

	private final XGAdress adress;
	private final XGParameter parameter;
	private XGValue value;

	public LeftZeroSlider(XGAdress adr) throws InvalidXGAdressException
	{	this.adress = adr;
		this.parameter = XGObjectType.getObjectTypeOrNew(adr).getParameter(adr);
		setSize(SL_DIM);
		setMinimumSize(SL_DIM);
		setPreferredSize(SL_DIM);
		setMaximumSize(SL_DIM);
		setVisible(false);
		setFocusable(true);
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
	}

	@Override protected void paintComponent(Graphics g)
	{	Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = 0;
		try
		{	w = Rest.linearIO(this.value.getNumberValue(), this.parameter.getMinValue(), this.parameter.getMaxValue(), 0, SL_W);}
		catch(WrongXGValueTypeException e)
		{	e.printStackTrace();}

		g2.setColor(BACK);
		g2.fillRoundRect(0, 0 , SL_W, SL_H, SL_RADI, SL_RADI);

		g2.setColor(FORE);
		g2.drawRoundRect(0, 0 , SL_W - 1, SL_H - 1, SL_RADI, SL_RADI);
		g2.fillRoundRect(0, 0 , w - 1, SL_H - 1, SL_RADI, SL_RADI);

		g2.setColor(Color.BLACK);
		g2.drawString(this.parameter.getShortName(), GAP, FONTMIDDLE);

		String t;
		try
		{	t = this.value.getTextValue();
		}
		catch(WrongXGValueTypeException e)
		{	e.printStackTrace();
			t = "no value";
		}
		if(t != null) g2.drawString(t, SL_W - GAP - g2.getFontMetrics().stringWidth(t), FONTMIDDLE);
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public void keyPressed(KeyEvent e)
	{
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{	if(this.value.addValue(e.getWheelRotation())) repaint();
		e.consume();
	}

	public void mouseDragged(MouseEvent e)
	{	if(this.value.changeValue(Rest.linearIO(e.getX(), 0, this.getWidth(), this.value.getParameter().getMinValue(), this.value.getParameter().getMaxValue())))repaint();
		e.consume();
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{	this.grabFocus();
		if(e.getButton() == MouseEvent.BUTTON1)
		{	try
			{	if(Rest.linearIO(this.value.getNumberValue(), this.parameter.getMinValue(), this.parameter.getMaxValue(), 0, this.getWidth()) < e.getX())
				{	if(this.value.addValue(1)) repaint();
				}
				else
				{	if(this.value.addValue(-1)) repaint();
				}
			}
			catch(WrongXGValueTypeException e1)
			{	e1.printStackTrace();
			}
		}
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void valueChanged(XGValue v)
	{	this.value = v;
		this.repaint();
	}

	public XGAdress getAdress()
	{	return this.adress;}
}
