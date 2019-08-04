package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGInstanceSelectionListener;
import parm.XGParameter;
import parm.XGParameterConstants;
import value.WrongXGValueTypeException;
import value.XGValue;
import value.XGValueChangeListener;

public class MyCombo extends JButton implements ActionListener, GuiConstants, XGParameterConstants, XGValueChangeListener, XGInstanceSelectionListener
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

	public void instanceSelected(XGAdress adr)
	{	try
		{	valueChanged(XGValue.getValue(this.adress.complement(adr)));}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
	}

	public void valueChanged(XGValue v)
	{	if(this.value != null) this.value.removeListener(this);
		if(v != null)
		{	this.value = v;
			this.value.addListener(this);
				this.setToolTipText(v.getParameter().getLongName());
				this.setText(this.value.toString());
				addActionListener(this);
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
			int v = (int)c.value.getContent();
				for(Entry<Integer, String> e : XGParameter.getParameter(c.getAdress()).getTranslationMap().entrySet())
				{	JCheckBoxMenuItem m = new JCheckBoxMenuItem(e.getValue());
					if(e.getKey() == v) m.setSelected(true);
					m.addActionListener(new ActionListener()
					{	public void actionPerformed(ActionEvent ae)
						{	try
							{	c.value.setContentAndTransmit(e.getKey());}
							catch(WrongXGValueTypeException|InvalidXGAdressException e)
							{	e.printStackTrace();}
							instance.setVisible(false);
							instance.setEnabled(false);
						}
					});
					this.add(m);
				}
			this.setEnabled(true);
			this.setVisible(true);
		}
	}
}
