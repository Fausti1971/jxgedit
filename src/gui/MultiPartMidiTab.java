package gui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import adress.XGAdress;
import adress.XGAdressField;
import obj.XGObjectConstants;
import parm.XGParameterConstants;
import value.XGValueChangeListener;

public class MultiPartMidiTab extends JPanel implements XGParameterConstants, XGObjectConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	
	public MultiPartMidiTab()
	{	addAndRegister(new MyCombo(new XGAdress(new XGAdressField(MULTI), new XGAdressField(), new XGAdressField(MP_CH))));
	}

	private <T extends JComponent & XGValueChangeListener> void addAndRegister(T c)
	{	super.add(c);
		MultiPartTab.getTableView().addXGValueChangeListener(c);
	}
}
