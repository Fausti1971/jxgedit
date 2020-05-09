package gui;

import static application.XGLoggable.log;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import msg.XGMessageParameterChange;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGCombo1 extends JButton implements XGComponent, ActionListener, GuiConstants, XGValueChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	private static final int PREF_W = 128, PREF_H = 32;

/********************************************************************************************************/

	private final XGValue value;
	private final XMLNode config;
	private final XGAddress address;

	public XGCombo1(XMLNode n, XGAddressableSet<XGValue> set)
	{	super();
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), null);
		this.config = n;
		XGValue v = set.getFirstValid(this.address);
		this.setEnabled(v != null);
		if(v == null) v = DEF_VALUE;
		this.value = v;
		if(this.isEnabled())
		{	this.setName(this.value.getParameter().getShortName());
			this.setToolTipText(this.value.getParameter().getLongName());
			this.setFocusable(true);
		}
		this.setSizes(PREF_W, PREF_H);
		this.borderize();
		this.value.addListener(this);
		this.addActionListener(this);
		log.info("combo initialized: " + this.getName());
	}

	@Override public boolean isEnabled()
	{	return super.isEnabled() && this.value != null && this.value.getParameter() != null;
	}

	@Override public void contentChanged(XGValue v)
	{	this.setText(this.value.toString());
		if(this.isVisible()) this.repaint();
	}

	@Override public void actionPerformed(ActionEvent e)
	{	new MyPopup(this);
	}

	public XGAddress getAdress()
	{	return this.address;
	}

	public String getInfo()
	{	return this.toString();
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public JComponent getJComponent()
	{	return this;
	}

/*******************************************************************************************************/

	private class MyPopup extends JPopupMenu
	{	/**
		 * 
		 */
		private static final long serialVersionUID=1L;

	/***************************************************************/

		private MyPopup(XGCombo1 c)
		{	MyPopup instance = this;
			this.setInvoker(c);
			this.setLocation(c.getLocationOnScreen());
			int v = c.value.getContent();
			for(Entry<Integer, XGTableEntry> e : c.value.getParameter().getTranslationTable().entrySet())
			{	JCheckBoxMenuItem m = new JCheckBoxMenuItem(e.getValue().getName());
				if(e.getKey() == v) m.setSelected(true);
				m.addActionListener(new ActionListener()
				{	@Override public void actionPerformed(ActionEvent ae)
					{	boolean changed = c.value.setContent(e.getKey());
						if(changed)
						{	try
							{	new XGMessageParameterChange(c.value.getSource(), c.value.getSource().getDevice().getMidi(), c.value).transmit();
								setEnabled(false);
								setVisible(false);
							}
							catch(InvalidXGAddressException | InvalidMidiDataException e)
							{	e.printStackTrace();
								instance.setVisible(false);
								instance.setEnabled(false);
							}
						}
					}
				});
				this.add(m);
			}
			this.setEnabled(true);
			this.setVisible(true);
		}
	}
}
