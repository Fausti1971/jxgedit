package gui;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPanel;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import obj.XGObject;
import obj.XGObjectConstants;
import parm.XGParameterConstants;

public class SysFXTab extends JComponent implements XGParameterConstants, XGObjectConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	JPanel revPanel = new JPanel(), choPanel = new JPanel(), varPanel = new JPanel(), panel;
	public SysFXTab()
	{	XGObjectSelectionListener c;
		XGObject o;
		try
		{	o = XGObject.getXGObjectInstance(new XGAdress(SYSFX, 1));
			c = new MyCombo(FX1_REV_TYPE);
			c.xgObjectSelected(o);
			panel = revPanel;
			panel.add((Component)c);
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
		}

		this.add(revPanel);
		this.add(choPanel);
		this.add(varPanel);
		
		revPanel.add(new LeftZeroSlider(FX1_REV_P01));
		
	}
}
