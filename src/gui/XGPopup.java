package gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import parm.XGParameter;
import value.XGValue;

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
			i.addActionListener((ActionEvent e) -> {context.actionPerformed(new ActionEvent(context, 0, s));});
			this.add(i);
		}
		this.setVisible(true);
	}

	public XGPopup(JComponent inv, Set<XGValue> values)
	{	this.setInvoker(inv);
		this.setLocation(inv.getLocationOnScreen());
	
		XGParameter p;
		JCheckBox c;
		for(XGValue v : values)
		{	p = v.getParameter();
			c = new JCheckBox(p.getName(), p.getMaxIndex() == v.getIndex());
			c.addActionListener((ActionEvent)->{v.toggleIndex();});
			this.add(c);
		}
		this.setVisible(true);
	}
}
