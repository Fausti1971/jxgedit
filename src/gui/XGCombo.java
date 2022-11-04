package gui;

import java.awt.*;
import javax.swing.*;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import table.XGTable;
import table.XGTableEntry;
import value.XGValue;

public class XGCombo extends XGFrame implements XGParameterChangeListener
{

/*****************************************************************************************************************/

	private final XGValue value;
	private final JComboBox<XGTableEntry> box = new JComboBox<>();

	public XGCombo(XGValue val)throws XGComponentException
	{	super("");
		this.value = val;
		if(this.value == null) throw new XGComponentException("value is null");

		XGParameter p = this.value.getParameter();
		if(p != null)
		{	XGTable t = p.getTranslationTable();
			for(int i = p.getMinIndex(); i <= p.getMaxIndex(); i++) this.box.addItem(t.getByIndex(i));
			this.box.setSelectedItem(t.getByIndex(this.value.getIndex()));//ruft angehängte ActionListener auf, deshalb vor addActionListener ausführen
			this.box.addActionListener((ActionEvent)->this.entrySelected());
		}
		else this.setEnabled(false);
		this.setAutoscrolls(true);

		this.add(this.box, "0,0,1,2");

		this.value.getValueListeners().add((XGValue v)->this.box.setSelectedItem(this.value.getEntry()));
		this.value.getParameterListeners().add(this);
		this.parameterChanged(this.value.getParameter());
	}

	private void entrySelected()
	{	Object o = this.box.getSelectedItem();
		if(o instanceof XGTableEntry) this.value.setEntry((XGTableEntry)o, false, true);
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.setName(p.getShortName());
		this.setToolTipText(p.getName());
		this.setEnabled(p.isValid());
		this.setVisible(p != XGParameter.NO_PARAMETER);
		this.repaint();
	}
}
