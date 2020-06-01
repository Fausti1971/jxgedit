package gui;

import static application.XGLoggable.log;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class XGCombo extends XGComponent implements XGValueChangeListener, XGParameterChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************/

	private final JComboBox<XGTableEntry> combo;
	
	public XGCombo(XMLNode n, XGModule mod)
	{	super(n, mod);
		this.setLayout(new GridBagLayout());
		this.combo = new XGComboBox<>(this.value);
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 0, 0, 0.5, 0.5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0);
		this.add(this.combo, gbc);
		this.value.addValueListener(this);
		this.value.addParameterListener(this);
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
	{	this.combo.setSelectedItem(this.value.getEntry());
	}

/*****************************************************************************************************/

	private class XGComboBox<T> extends JComboBox<XGTableEntry> implements ActionListener
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final XGValue value;

		public XGComboBox(XGValue v)
		{	super();
			this.value = v;
			XGTable t = this.value.getParameter().getTranslationTable();
			for(XGTableEntry e : t) this.addItem(e);
//			this.setMaximumRowCount(Toolkit.getDefaultToolkit().getScreenSize().height/this.row, t.size());
			this.setSelectedItem(t.getByIndex(this.value.getContent()));//ruft angehängte ActionListener auf, deshalb vor addActionListener ausführen
			this.addActionListener(this);
			this.setAutoscrolls(true);
		}

		@Override public void actionPerformed(ActionEvent ae)
		{	boolean changed = this.value.setEntry((XGTableEntry)this.getSelectedItem());//TODO: wird erst transmitted, nachdem die listener notified wurden (falsch für masterValues...)
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
