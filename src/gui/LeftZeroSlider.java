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
import memory.Bytes;
import obj.XGObject;
import parm.XGParameter;
import parm.XGParameterConstants;

public class LeftZeroSlider extends JComponent implements GuiConstants, KeyListener, MouseWheelListener, MouseMotionListener, MouseListener, XGObjectSelectionListener, XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*****************************************************************************************************************************/

//	private XGParameter parm = null;
	private int offset;
	private XGObject obj;

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

	private XGParameter getParam()
	{	return this.obj.getParameter(this.offset);
	}
	@Override protected void paintComponent(Graphics g)
	{	Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = Bytes.linearIO(this.obj.getValue(this.offset), getParam().getMinValue(), getParam().getMaxValue(), 0, SL_W);

		g2.setColor(BACK);
		g2.fillRoundRect(0, 0 , SL_W, SL_H, SL_RADI, SL_RADI);

		g2.setColor(FORE);
		g2.drawRoundRect(0, 0 , SL_W - 1, SL_H - 1, SL_RADI, SL_RADI);
		g2.fillRoundRect(0, 0 , w - 1, SL_H - 1, SL_RADI, SL_RADI);

		g2.setColor(Color.BLACK);
		g2.drawString(getParam().getShortName(), GAP, SL_H - GAP);

		String t = getParam().getValueAsText(this.obj);
		g2.drawString(t, SL_W - GAP - g.getFontMetrics().stringWidth(t), SL_H - GAP);
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
	{	if(this.obj.addValue(this.getParam().getOpcode().getOffset(), e.getWheelRotation())) repaint();
		e.consume();
	}

	public void mouseDragged(MouseEvent e)
	{	if(this.obj.changeValue(this.getParam().getOpcode().getOffset(), Bytes.linearIO(e.getX(), 0, this.getWidth(), getParam().getMinValue(), getParam().getMaxValue())))repaint();
		e.consume();
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{	if(e.getButton() == MouseEvent.BUTTON1)
		{	if(Bytes.linearIO(this.obj.getValue(this.getParam().getOpcode().getOffset()), getParam().getMinValue(), getParam().getMaxValue(), 0, this.getWidth()) < e.getX())
			{	if(this.obj.addValue(this.getParam().getOpcode().getOffset(), 1)) repaint();
			}
			else
			{	if(this.obj.addValue(this.getParam().getOpcode().getOffset(), -1)) repaint();
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
	{	this.obj = o;
		if(this.getParam() != null)
		{	this.setToolTipText(this.getParam().getLongName());
			this.setVisible(true);
		}
		else setVisible(false);
		this.repaint();
	}
}
