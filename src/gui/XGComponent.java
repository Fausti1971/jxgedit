package gui;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.border.Border;

public interface XGComponent extends XGUI, MouseListener, FocusListener, Border
{
	int BORDERLINE_THICKNESS = 2; 
	int BORDERLINE_ROUNDNESS = BORDERLINE_THICKNESS * 4;
	Insets BORDER_INSETS = new Insets(BORDERLINE_THICKNESS, BORDERLINE_THICKNESS, BORDERLINE_THICKNESS, BORDERLINE_THICKNESS);

/********************************************************************************************/

	default JComponent getJComponent(){	return (JComponent)this;}

	@Override default void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{	String txt = c.getName();
		if(this.getJComponent() == null) return;
		Graphics2D g2 = (Graphics2D)g.create();
		g2.addRenderingHints(AALIAS);
		Insets ins = this.getBorderInsets(c);
//outer border
		GradientPaint gp = new GradientPaint(0, c.getHeight() / 2, c.getBackground().brighter(), c.getWidth() / 2, c.getHeight(), c.getBackground().darker(),false);
		g2.setPaint(gp);
		g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), BORDERLINE_ROUNDNESS, BORDERLINE_ROUNDNESS);
//inner border
		g2.setColor(c.getBackground());
		g2.fillRoundRect(ins.left, ins.top, c.getWidth() - (ins.left + ins.right), c.getHeight() - (ins.top + ins.bottom), BORDERLINE_ROUNDNESS, BORDERLINE_ROUNDNESS);
//text
		if(txt != null && !txt.isEmpty())
		{	Rectangle2D strBnd = g2.getFontMetrics().getStringBounds(txt, g2);
			g2.setColor(COL_BORDER_TEXT);
			float sx = (float)(c.getWidth() / 2 - strBnd.getWidth() / 2);
			g2.drawString(txt, sx, (float)strBnd.getHeight() - 2);
		}
		g2.dispose();
	}

	@Override default Insets getBorderInsets(Component component){	return BORDER_INSETS;}

	@Override default boolean isBorderOpaque(){	return true;}

	@Override  default void mouseClicked(MouseEvent e)
	{	if(e.getClickCount() == 2)
		{
System.out.println("doubleclick detected");
		}
	}

	@Override  default void mousePressed(MouseEvent e)
	{	ENVIRONMENT.mousePressed = true;
		ENVIRONMENT.dragEvent = e;
		e.consume();
	}

	@Override  default void mouseReleased(MouseEvent e)
	{	ENVIRONMENT.mousePressed = false;
		ENVIRONMENT.dragEvent = e;
	}

	@Override  default void mouseEntered(MouseEvent e)
	{	if(!ENVIRONMENT.mousePressed) this.getJComponent().requestFocusInWindow();
	}

	@Override  default void mouseExited(MouseEvent e)
	{
	}

	@Override  default void focusLost(FocusEvent e)
	{
	}

	@Override  default void focusGained(FocusEvent e)
	{
	}
}
