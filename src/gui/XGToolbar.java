package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class XGToolbar extends JToolBar implements XGUI
{

/************************************************************************************************************************/

	XGToolbar()
	{	this.setFloatable(false);
		this.setRollover(true);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	public void addAction(String key, String name, String desc, ActionListener al)
	{	JButton b = new JButton();
//		b.setName(name);
		b.setToolTipText(desc);
		b.setIcon(XGUI.loadImage(ICON_KEYS_40.get(key)));
		b.addActionListener(al);
		b.registerKeyboardAction(al, KEYSTROKES.get(key), WHEN_IN_FOCUSED_WINDOW);
		this.add(b);
	}
}
