package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import parm.XGParameter;

public class MyPopup extends JPopupMenu
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public MyPopup(MyCombo c)
	{	XGParameter p = c.getParm();
		int v = p.getValue();
		for(Entry<Integer, String> e : p.getTranslationMap().entrySet())
		{	JCheckBoxMenuItem m = new JCheckBoxMenuItem(e.getValue());
			m.addActionListener(new ActionListener()
			{	public void actionPerformed(ActionEvent ev)
				{	c.valueChanged(e.getKey());
				}
			});
			if(e.getKey() == v) m.setSelected(true);
			add(m);
		}
	}
}
