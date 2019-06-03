package gui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressField;
import obj.XGObjectConstants;
import parm.XGParameterConstants;

public class SysFXTab extends JComponent implements XGParameterConstants, XGObjectConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	JPanel revPanel = new JPanel(), choPanel = new JPanel(), varPanel = new JPanel(), panel;
	public SysFXTab()
	{	this.add(revPanel);
		this.add(choPanel);
		this.add(varPanel);
		XGAdressField hi = new XGAdressField(SYSFX);
		XGAdressField mid = new XGAdressField(1);
		
		try
		{	revPanel.add(new LeftZeroSlider(new XGAdress(hi,mid,new XGAdressField(FX1_REV_TYPE))));
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
	}
}
