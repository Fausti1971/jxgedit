package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import obj.XGObject;
import parm.XGParameter;
import parm.XGParameterConstants;

public class MyCombo extends JButton implements GuiConstants, XGObjectSelectionListener, XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

//	private XGParameter parm;
	private XGObject obj;
	private int offset;

	public MyCombo(int offs)
	{	this.offset = offs;
		MyCombo mc = this;
		setSize(SL_DIM);
		setVisible(false);
		addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	new MyPopup(mc).show(mc, 0, 0);
			}
		});
	}

	private XGParameter getParam()
	{	return this.obj.getParameter(this.offset);}

	public void xgObjectSelected(XGObject o)
	{	this.obj = o;
		if(this.getParam() != null)
		{	this.setToolTipText(this.getParam().getLongName());
			this.setVisible(true);
			this.setText(this.getParam().getValueAsText(this.obj));
		}
		else this.setVisible(false);
		this.repaint();
	}

	public void valueChanged(int v)
	{	this.obj.changeValue(this.getParam().getOpcode().getOffset(), v);
		this.setText(this.getParam().getValueAsText(this.obj));
	}

	private class MyPopup extends JPopupMenu
	{	/**
		 * 
		 */
		private static final long serialVersionUID=1L;

		public MyPopup(MyCombo c)
		{	XGParameter p = c.getParam();
			int v = c.obj.getValue(c.offset);
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

}
