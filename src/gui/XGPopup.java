package gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class XGPopup extends JPopupMenu
{	/**
	 * 
	 */
	private static final long serialVersionUID=-8590959410134092274L;

	public XGPopup(JComponent inv, XGContext context, Point p)
	{
		JMenuItem i = new JMenuItem(context.toString());
		i.setEnabled(false);
		this.add(i);
		this.addSeparator();
		this.setInvoker(inv);
		this.setLocation(p);

		for(String s : context.getContexts())
		{	i = new JMenuItem(s);
			i.addActionListener((ActionEvent e) ->	{context.actionPerformed(new ActionEvent(context, 0, s));});
			this.add(i);
		}
		this.setVisible(true);
	}
}
