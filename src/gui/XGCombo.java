package gui;

import static application.XGLoggable.log;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComboBox;
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

public class XGCombo extends XGFrame implements XGValueChangeListener, XGParameterChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int PREF_W = 64, PREF_H = 64;

/*****************************************************************************************************************/

	private final JComboBox<XGTableEntry> combo;
	
	public XGCombo(XMLNode n, XGModule mod)
	{	super(n, mod);
		this.combo = new XGComboBox<>(this.value);
		this.addGB(this.combo, 0, 0, 0, 0, GridBagConstraints.HORIZONTAL);
		this.value.addValueListener(this);
		this.value.addParameterListener(this);
		this.setSizes(PREF_W, PREF_H);
		this.parameterChanged(this.value.getParameter());

		log.info("combo initialized: " + this.getName());
	}


	@Override public void parameterChanged(XGParameter p)
	{	if(p != null)
		{	this.setName(p.getShortName());
			this.setToolTipText(p.getLongName());
		}
		else
		{	this.setName("n/a");
			this.setToolTipText("no parameter");
		}
		this.borderize();
		this.repaint();
	}

	@Override public void contentChanged(XGValue v)
	{	this.combo.setSelectedItem(this.value.getParameter().getValueTranslator().getTable(this.value).get(v.getContent()));
	}

/*****************************************************************************************************/

	private class XGComboBox<T> extends JComboBox<XGTableEntry> implements ActionListener
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final XGValue value;

		public XGComboBox(XGValue v)
		{	this.value = v;
			XGTable t = this.value.getParameter().getValueTranslator().getTable(v);
			for(XGTableEntry e : t.values()) this.addItem(e);
//			this.setMaximumRowCount(Toolkit.getDefaultToolkit().getScreenSize().height/this.row, t.size());
			this.setSelectedItem(t.get(this.value.getContent()));//ruft angehängte ActionListener auf, deshalb vor addActionListener ausführen
			this.addActionListener(this);
			this.setAutoscrolls(true);
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
	}
}
