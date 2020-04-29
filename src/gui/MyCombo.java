package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import msg.XGMessageParameterChange;
import parm.XGParameterConstants;
import value.XGValue;
import value.XGValueChangeListener;

public class MyCombo extends JButton implements ActionListener, GuiConstants, XGParameterConstants, XGValueChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	private XGValue value;
	private final XGAddress adress;

	public MyCombo(XGAddress adr)
	{	this.adress = adr;
		setSize(SL_DIM);
	}

	@Override public void contentChanged(XGValue v)
	{	this.setText(this.value.toString());
		if(this.isVisible()) this.repaint();
	}

	@Override public void actionPerformed(ActionEvent e)
	{	new MyPopup(this);
	}

	public XGAddress getAdress()
	{	return this.adress;
	}

	@Override public boolean isVisible()
	{	return this.value != null;
	}

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
			int v = c.value.getContent();
			for(Entry<Integer, String> e : c.value.getParameter().getTranslationMap().entrySet())
			{	JCheckBoxMenuItem m = new JCheckBoxMenuItem(e.getValue());
				if(e.getKey() == v) m.setSelected(true);
				m.addActionListener(new ActionListener()
				{	@Override public void actionPerformed(ActionEvent ae)
					{	try
						{	c.value.setContent(e.getKey());
							c.value.transmit(c.value.getSource().getDevice().getMidi());
							setEnabled(false);
							setVisible(false);
						}
						catch(InvalidXGAddressException | MidiUnavailableException e)
						{	e.printStackTrace();
							instance.setVisible(false);
							instance.setEnabled(false);
						}
					}
				});
				this.add(m);
			}
			this.setEnabled(true);
			this.setVisible(true);
		}
	}

	public String getInfo()
	{	return this.toString();
	}
}
