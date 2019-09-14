package gui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressField;
import adress.XGInstanceSelectionListener;
import obj.XGObjectConstants;
import parm.XGParameterConstants;

public class MultiPartVoiceTab extends JPanel implements XGObjectConstants, XGParameterConstants
{
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*******************************************************************************************************/

	public MultiPartVoiceTab()
	{	XGAdressField hi = new XGAdressField(MULTI);
		try
		{	addAndRegister(MP_ELRES, new LeftZeroSlider(new XGAdress(hi, null, new XGAdressField(MP_ELRES))));
			addAndRegister(MP_TUNE, new LeftZeroSlider(new XGAdress(hi, null, new XGAdressField(MP_TUNE))));
			addAndRegister(MP_ATACK, new LeftZeroSlider(new XGAdress(hi, null, new XGAdressField(MP_ATACK))));
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}

//		MultiPartTab.getTableView().registerObjectSelectionListener(this);
	}

	private <T extends JComponent & XGInstanceSelectionListener> void addAndRegister(int offs, T c)
	{	super.add(c);
		MultiPartTab.getTableView().addXGInstanceSelectionListener(c);
	}
}