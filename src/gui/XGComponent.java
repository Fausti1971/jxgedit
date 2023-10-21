package gui;

import value.XGValue;import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;

public interface XGComponent extends XGUI, MouseListener
{
	enum XGOrientation{horizontal, vertical}

	default JComponent getJComponent(){	return (JComponent)this;}

	default XGWindow getWindow()
	{	Container c = this.getJComponent();
		while(c != null)
		{	if(c instanceof XGWindow) return (XGWindow)c;
			c = c.getParent();
		}
		return XGMainWindow.MAINWINDOW;
	}

	@Override default void mouseClicked(MouseEvent e)
	{	Object o = e.getSource();
		if(e.getButton() == MouseEvent.BUTTON3)
		{	while(o != null)
			{	if(o instanceof XGValueComponent)
					for(XGValue v : ((XGValueComponent)o).getValues())
						v.setValue(v.getDefaultValue(), false, true);
				if(o instanceof Component) o = ((Component)o).getParent();
				else o = null;
			}
		}
	}

	default Rectangle getDrawArea()
	{	Rectangle r = new Rectangle(this.getJComponent().getBounds());
		Insets ins = this.getJComponent().getInsets();
		r.x += ins.left;
		r.y += ins.top;
		r.width -= (ins.left + ins.right);
		r.height -= (ins.top + ins.bottom);
		return r;
	}

	@Override default void mousePressed(MouseEvent e)
	{	ENVIRONMENT.mousePressed = true;
		ENVIRONMENT.dragEvent = e;
		e.consume();
	}

	@Override default void mouseReleased(MouseEvent e)
	{	ENVIRONMENT.mousePressed = false;
		ENVIRONMENT.dragEvent = e;
		e.consume();
	}

	@Override  default void mouseEntered(MouseEvent e)
	{	if(!ENVIRONMENT.mousePressed) this.getJComponent().requestFocusInWindow();
	}

	@Override default void mouseExited(MouseEvent e)
	{
	}
}
