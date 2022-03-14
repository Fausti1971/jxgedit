package gui;

import java.awt.*;
import javax.swing.JComboBox;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTable;
import parm.XGTableEntry;
import value.XGValue;

public class XGCombo extends JComboBox<XGTableEntry> implements XGParameterChangeListener, XGComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension MIN_DIM = new Dimension(XGUI.GRID * 6, XGUI.GRID * 2);

/*****************************************************************************************************************/

	private final XGValue value;

	public XGCombo(XGValue val)
	{	this.value = val;
		if(this.value == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			return;
		}
		this.setMinimumSize(MIN_DIM);
		this.setPreferredSize(MIN_DIM);
//		this.borderize();
		XGParameter p = this.value.getParameter();
		if(p != null)
		{	XGTable t = p.getTranslationTable();
			for(int i = p.getMinIndex(); i <= p.getMaxIndex(); i++)
			{	this.addItem(t.getByIndex(i));
			}
			this.setSelectedItem(t.getByIndex(this.value.getIndex()));//ruft angehängte ActionListener auf, deshalb vor addActionListener ausführen
			this.addActionListener((ActionEvent)->{this.entrySelected();});
		}
		else this.setEnabled(false);
		this.setAutoscrolls(true);
		this.setFont(MEDIUM_FONT);

//		this.addFocusListener(this);
		this.value.getValueListeners().add((XGValue v)->{this.setSelectedItem(this.value.getEntry());});
		this.value.getParameterListeners().add(this);
		this.parameterChanged(this.value.getParameter());
	}

	private void entrySelected()
	{	this.value.setEntry((XGTableEntry)this.getSelectedItem(), false, true);
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.setName(p.getShortName());
		this.setToolTipText(p.getName());
		this.setEnabled(p.isValid());
		this.setVisible(p != XGParameter.NO_PARAMETER);
		this.borderize();
		this.repaint();
	}
}
