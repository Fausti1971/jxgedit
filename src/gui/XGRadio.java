package gui;

import static application.XGLoggable.log;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JCheckBox;
import adress.InvalidXGAddressException;
import module.XGModule;
import msg.XGMessageParameterChange;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTable;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGRadio extends XGFrame implements XGValueChangeListener, XGParameterChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int PREF_W = 128, PREF_H = 64;

	public XGRadio(XMLNode n, XGModule mod)
	{	super(n, mod);
		this.value.addValueListener(this);
		this.value.addParameterListener(this);
		this.setSizes(PREF_W, PREF_H);
		this.parameterChanged(this.value.getParameter());
		this.borderize();
		log.info("radio initialized: " + this.getName());
	}

	@Override public void parameterChanged(XGParameter p)
	{	if(p != null)
		{	this.setEnabled(true);
			this.setName(p.getShortName());
			this.setToolTipText(p.getLongName());
			XGTable t = this.value.getParameter().getValueTranslator().getTable(this.value);
			int i = 0;
			for(XGTableEntry e : t.values())
			{	this.addGB(new XGRadioButton(this.value, e), 0, i++);
			}
		}
		else
		{	this.setEnabled(false);
			this.setName("n/a");
		}
	}

	@Override public void contentChanged(XGValue v)
	{	this.repaint();
	}

	@Override protected void paintComponent(Graphics g)
	{	if(this.isEnabled()) super.paintComponent(g);
	}

/****************************************************************************************************/

	private class XGRadioButton extends JCheckBox implements ActionListener
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

/******************************************************************/

		private final XGTableEntry entry;
		private final XGValue value;

		XGRadioButton(XGValue v, XGTableEntry e)
		{	super(e.getName());
			this.setRolloverEnabled(true);
			this.entry = e;
			this.value = v;
			this.addActionListener(this);
		}

		@Override public boolean isSelected()
		{	return this.entry.getKey() == this.value.getContent();
		}

		@Override public void actionPerformed(ActionEvent e)
		{	boolean changed = this.value.setContent(entry.getKey());
			if(changed)
			{	try
				{	new XGMessageParameterChange(this.value.getSource(), this.value.getSource().getDevice().getMidi(), this.value).transmit();
				}
				catch(InvalidXGAddressException | InvalidMidiDataException ex)
				{	ex.printStackTrace();
				}
			}
		}

		@Override public void paint(Graphics g)
		{	this.setSelected(this.isSelected());
			super.paint(g);
		}

		@Override public String toString()
		{	return this.entry.toString();
		}
	}
}
