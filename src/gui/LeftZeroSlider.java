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
import msg.Bytes;
import obj.XGObject;
import parm.XGParameterConstants;
import value.XGValue;

public class LeftZeroSlider extends JComponent implements GuiConstants, KeyListener, MouseWheelListener, MouseMotionListener, MouseListener, XGObjectSelectionListener, XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*****************************************************************************************************************************/

	private final int offset;
	private XGValue value;

	public LeftZeroSlider(int offset)
	{	this.offset = offset;
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

		int w = Bytes.linearIO((int)this.value.getValue(), this.value.getParameter().getMinValue(), this.value.getParameter().getMaxValue(), 0, SL_W);

		g2.setColor(BACK);
		g2.fillRoundRect(0, 0 , SL_W, SL_H, SL_RADI, SL_RADI);

		g2.setColor(FORE);
		g2.drawRoundRect(0, 0 , SL_W - 1, SL_H - 1, SL_RADI, SL_RADI);
		g2.fillRoundRect(0, 0 , w - 1, SL_H - 1, SL_RADI, SL_RADI);

		g2.setColor(Color.BLACK);
		g2.drawString(this.value.getParameter().getShortName(), GAP, FONTMIDDLE);

		String t = this.value.getTranslatedValue();
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
	{	if(this.value.changeValue(Bytes.linearIO(e.getX(), 0, this.getWidth(), this.value.getParameter().getMinValue(), this.value.getParameter().getMaxValue())))repaint();
		e.consume();
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{	this.grabFocus();
		if(e.getButton() == MouseEvent.BUTTON1)
		{	if(Bytes.linearIO((int)this.value.getValue(), this.value.getParameter().getMinValue(), this.value.getParameter().getMaxValue(), 0, this.getWidth()) < e.getX())
			{	if(this.value.addValue(1)) repaint();
			}
			else
			{	if(this.value.addValue(-1)) repaint();
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

	public void xgObjectSelected(XGObject o)
	{	this.value = o.getXGValue(this.offset);
		try
		{	this.setToolTipText(this.value.getParameter().getLongName());
			this.setVisible(true);
		}
		catch(NullPointerException e)
		{	e.printStackTrace();
			setVisible(false);
		}
		this.repaint();
	}
}
