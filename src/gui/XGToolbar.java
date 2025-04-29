package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class XGToolbar extends JToolBar
{

/************************************************************************************************************************/

	XGToolbar()
	{	this.setFloatable(false);
		this.setRollover(true);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	public void addAction(String key, String name, String desc, ActionListener al)
	{	this.addAction(key, name, desc, al, null);
	}

	public void addAction(String key, String name, String desc, ActionListener al, JPopupMenu pop)
	{	JButton b = new JButton();
//		b.setName(name);
		if(pop != null) b.setComponentPopupMenu(pop);
		b.setToolTipText(desc);
		b.setIcon(XGUI.loadImage(XGUI.getIconPath(key)));
		b.addActionListener(al);
		b.registerKeyboardAction(al, XGUI.KEYSTROKES.get(key), WHEN_IN_FOCUSED_WINDOW);
		this.add(b);
	}
}
