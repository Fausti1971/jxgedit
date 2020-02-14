package gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class XGPopup extends JPopupMenu
{	/**
	 * 
	 */
	private static final long serialVersionUID=-8590959410134092274L;

	public XGPopup(JComponent inv, XGAction action, Point p)
	{	JMenuItem i = new JMenuItem(action.toString());
		i.setEnabled(false);
		this.add(i);
		this.addSeparator();
		this.setInvoker(inv);
		for(String s : action.getActions())
		{	i = new JMenuItem(s);
			i.addActionListener(new ActionListener()
			{	@Override public void actionPerformed(ActionEvent e)
				{	action.actionPerformed(new ActionEvent(action, 0, s));
				}
			});
			this.add(i);
		}
		this.setLocation(p);
		this.setVisible(true);
	}
}
