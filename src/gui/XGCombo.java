package gui;

import static application.XGLoggable.log;
import java.awt.event.ActionEvent;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import module.XGModule;
import msg.XGMessageParameterChange;
import parm.XGTable;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGCombo extends JComboBox<XGTableEntry> implements XGComponent, XGValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int PREF_W = 256, PREF_H = 64;

/*****************************************************************************************************************/
	private final XGValue value;
	private final XGAddress address;
	private final XMLNode config;
	private final XGModule module;
	
	public XGCombo(XMLNode n, XGModule mod)
	{	super();
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
		this.config = n;
		this.module = mod;
		XGValue v = mod.getDevice().getValues().getFirstIncluded(this.address);
		this.setEnabled(v != null);
		if(v == null) v = DEF_VALUE;
		this.value = v;
		if(this.isEnabled())
		{	this.setName(this.value.getParameter().getShortName());
			XGTable t = this.value.getParameter().getValueTranslator().getTable(v);
			for(XGTableEntry e : t.values()) this.addItem(e);
			this.setMaximumRowCount(t.size());
			this.setSelectedItem(t.get(this.value.getContent()));
			this.setToolTipText(this.value.getParameter().getLongName());
			this.setFocusable(true);
			this.value.addListener(this);
		}
		this.setAutoscrolls(true);
		this.setSizes(PREF_W, PREF_H);
		this.borderize();
		this.addActionListener(this);
		this.addMouseListener(this);
		this.addFocusListener(this);

		log.info("combo initialized: " + this.getName());
	}

	@Override public void actionPerformed(ActionEvent ae)
	{	boolean changed = this.value.setContent(((XGTableEntry)this.getSelectedItem()).getKey());//TODO: wird erst transmitted, nachdem die listener notified wurden (falsch für masterValues...)
		if(changed)
		{	try
			{	new XGMessageParameterChange(this.value.getSource(), this.value.getSource().getDevice().getMidi(), this.value).transmit();
			}
			catch(InvalidXGAddressException | InvalidMidiDataException e)
			{	e.printStackTrace();
			}
		}
	}

	@Override public boolean isEnabled()
	{	return super.isEnabled() && this.value != null && this.value.getParameter() != null;
	}

	@Override public void contentChanged(XGValue v)
	{	this.setSelectedItem(this.value.getParameter().getValueTranslator().getTable(this.value).get(v.getContent()));
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public JComponent getJComponent()
	{	return this;
	}

	@Override public XGValue getValue()
	{	return this.value;
	}
}
