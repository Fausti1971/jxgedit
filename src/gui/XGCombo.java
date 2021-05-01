package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import module.XGModule;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTable;
import parm.XGTableEntry;
import value.XGValue;
import static value.XGValueStore.STORE;import xml.XMLNode;

public class XGCombo extends JComboBox<XGTableEntry> implements XGParameterChangeListener, XGComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************/

//	private final JComboBox<XGTableEntry> combo = new JComboBox<>();
	private final XGValue value;

	public XGCombo(XGValue val)
	{	//this.setLayout(new GridBagLayout());
		this.value = val;
		if(this.value == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			return;
		}
		this.borderize();
		XGParameter p = this.value.getParameter();
		if(p != null)
		{	XGTable t = p.getTranslationTable();
			for(XGTableEntry e : t) this.addItem(e);
			this.setSelectedItem(t.getByIndex(this.value.getIndex()));//ruft angehängte ActionListener auf, deshalb vor addActionListener ausführen
			this.addActionListener((ActionEvent)->{this.entrySelected();});
		}
		else this.setEnabled(false);
		this.setAutoscrolls(true);

		this.addFocusListener(this);
		this.value.addValueListener((XGValue v)->{this.setSelectedItem(this.value.getEntry());});
		this.value.addParameterListener(this);
		this.parameterChanged(this.value.getParameter());
	}

	private void entrySelected()
	{	this.value.editEntry((XGTableEntry)this.getSelectedItem());
	}

	@Override public void parameterChanged(XGParameter p)
	{	if(p != null)
		{	this.setName(p.getShortName());
			this.setToolTipText(p.getName());
		}
		else
		{	this.setName("n/a");
			this.setToolTipText("no parameter");
		}
		this.setEnabled(p != null);
		this.borderize();
		this.repaint();
	}
}
