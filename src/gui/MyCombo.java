package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import obj.XGObjectType;
import parm.XGParameterConstants;
import value.WrongXGValueTypeException;
import value.XGValue;
import value.XGValueChangeListener;

public class MyCombo extends JButton implements ActionListener, GuiConstants, XGParameterConstants, XGValueChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	private XGValue value;
	private XGAdress adress;

	public MyCombo(XGAdress adr)
	{	this.adress = adr;
		setSize(SL_DIM);
		this.valueChanged(this.value);
	}

	public void valueChanged(XGValue v)
	{	if(this.value != null) this.value.removeListener(this);
		if(v != null)
		{	this.value = v;
			try
			{	this.value.addListener(this);
				this.setToolTipText(XGObjectType.getObjectTypeOrNew(this.adress).getParameter(this.adress).getLongName());
				this.setText(this.value.getTextValue());
				addActionListener(this);
			}
			catch(WrongXGValueTypeException | InvalidXGAdressException e1)
			{	e1.printStackTrace();
			}
		}
		this.repaint();
	}

	public void actionPerformed(ActionEvent e)
	{	new MyPopup(this);}

	public XGAdress getAdress()
	{	return this.adress;}

	private class MyPopup extends JPopupMenu
	{	/**
		 * 
		 */
		private static final long serialVersionUID=1L;

	/*******************************************************************************************************/

		private MyPopup(MyCombo c)
		{	MyPopup instance = this;
			this.setInvoker(c);
			this.setLocation(c.getLocation());
			try
			{	int v = c.value.getNumberValue();
				for(Entry<Integer, String> e : XGObjectType.getObjectTypeOrNew(c.getAdress()).getParameter(c.getAdress()).getTranslationMap().entrySet())
				{	JCheckBoxMenuItem m = new JCheckBoxMenuItem(e.getValue());
					if(e.getKey() == v) m.setSelected(true);
					m.addActionListener(new ActionListener()
					{	public void actionPerformed(ActionEvent ae)
						{	c.value.changeValue(e.getKey());
							instance.setVisible(false);
							instance.setEnabled(false);
						}
					});
					this.add(m);
				}
			}
			catch(WrongXGValueTypeException | InvalidXGAdressException e)
			{	e.printStackTrace();}
			this.setEnabled(true);
			this.setVisible(true);
		}
	}
}
