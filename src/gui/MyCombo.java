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
	private final XGAdress adress;

	public MyCombo(XGAdress adr)
	{	this.adress = adr;
		setSize(SL_DIM);
		this.valueChanged(this.value);
	}

	public void contentChanged(XGValue v)
	{	this.setText(this.value.toString());
		if(this.isVisible()) this.repaint();
	}

	public void valueChanged(XGValue v)
	{	if(this.value != null)
		{	this.value.removeListener(this);
			this.removeActionListener(this);
		}
		if(v != null)
		{	this.value = v;
			this.value.addListener(this);
			this.setToolTipText(v.getParameter().getLongName());
			this.setText(this.value.toString());
			this.addActionListener(this);
		}
		this.setVisible(this.isVisible());
		this.repaint();
	}

	public void actionPerformed(ActionEvent e)
	{	new MyPopup(this);}

	public XGAdress getAdress()
	{	return this.adress;}

	@Override public boolean isVisible()
	{	return this.value != null;}

/*******************************************************************************************************/

	private class MyPopup extends JPopupMenu
	{	/**
		 * 
		 */
		private static final long serialVersionUID=1L;

	/***************************************************************/

		private MyPopup(MyCombo c)
		{	MyPopup instance = this;
			this.setInvoker(c);
			this.setLocation(c.getLocationOnScreen());
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

	public String getInfo()
	{	return this.toString();}
}
