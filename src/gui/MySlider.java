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

public class MySlider extends JComponent implements GuiConstants, KeyListener, MouseWheelListener, MouseMotionListener, MouseListener, XGObjectChangeListener, XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*****************************************************************************************************************************/

	XGParameter parm = null;
	final Tags tag;

	public MySlider(Tags tag)
	{	this.tag = tag;
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

//		getInsets().set(0, 5, 1, 5);
//		setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(3), new Insets(0))));

	}

	@Override protected void paintComponent(Graphics g)
	{	Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = Bytes.linearIO(parm.getValue(), parm.min, parm.max, 0, SL_W);

		g2.setColor(BACK);
		g2.fillRoundRect(0, 0 , SL_W, SL_H, SL_RADI, SL_RADI);

		g2.setColor(FORE);
		g2.drawRoundRect(0, 0 , SL_W - 1, SL_H - 1, SL_RADI, SL_RADI);
		g2.fillRoundRect(0, 0 , w - 1, SL_H - 1, SL_RADI, SL_RADI);

		g2.setColor(Color.BLACK);
		g2.drawString(parm.shortName, GAP, SL_H - GAP);

		String t = parm.getValueAsText();
		g2.drawString(t, SL_W - GAP - g.getFontMetrics().stringWidth(t), SL_H - GAP);
	}

	public void bind(XGParameter p)
	{	if(p != null)
		{	this.parm = p;
			setToolTipText(p.longName);
			setVisible(true);
		}
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
	{	if(parm.addValue(e.getWheelRotation())) repaint();
		e.consume();
	}

	public void mouseDragged(MouseEvent e)
	{	if(parm.setValue(Bytes.linearIO(e.getX(), 0, this.getWidth(), parm.min, parm.max)))repaint();
		e.consume();
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{	if(e.getButton() == MouseEvent.BUTTON1)
		{	if(Bytes.linearIO(parm.getValue(), parm.min, parm.max, 0, this.getWidth()) < e.getX())
			{	if(parm.addValue(1)) repaint();
			}
			else
			{	if(parm.addValue(-1)) repaint();
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

	public void objectChanged(XGObject o)
	{	bind(o.parameters.get(this.tag));
		repaint();
	}

	public byte[] getByteArray()
	{	return parm.obj.getByteArray();
	}
}
