package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import obj.XGObject;
import parm.XGParameterConstants;
import value.WrongXGValueTypeException;
import value.XGValue;

public class MyCombo extends JButton implements GuiConstants, XGObjectSelectionListener, XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	private XGValue value;
	private int offset;

	public MyCombo(int offs)
	{	this.offset = offs;
		MyCombo mc = this;
		setSize(SL_DIM);
		setVisible(false);
		addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	new MyPopup(mc).show(mc, 0, 0);}
		});
	}

	public void xgObjectSelected(XGObject o)
	{	this.value = o.getXGValue(this.offset);
		this.setToolTipText(this.value.getParameter().getLongName());
		this.setVisible(true);
		try
		{	this.setText(this.value.getTextValue());
		}
		catch(WrongXGValueTypeException e)
		{	e.printStackTrace();
		}
		this.repaint();
	}

	public void valueChanged(int v)
	{	this.value.changeValue(v);
		try
		{	this.setText(this.value.getTextValue());
		}
		catch(WrongXGValueTypeException e)
		{	e.printStackTrace();
		}
	}

	private class MyPopup extends JPopupMenu
	{	/**
		 * 
		 */
		private static final long serialVersionUID=1L;

		public MyPopup(MyCombo c)
		{	try
			{	int v = c.value.getNumberValue();
				for(Entry<Integer, String> e : c.value.getParameter().getTranslationMap().entrySet())
				{	JCheckBoxMenuItem m = new JCheckBoxMenuItem(e.getValue());
					if(e.getKey() == v) m.setSelected(true);
					m.addActionListener(new ActionListener()
					{	public void actionPerformed(ActionEvent ev)
						{	c.valueChanged(e.getKey());
						}
					});
					add(m);
				}
			}
			catch(WrongXGValueTypeException e)
			{	e.printStackTrace();}
		}
	}

}
