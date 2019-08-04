package gui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressField;
import obj.XGObject;
import obj.XGObjectConstants;
import parm.XGParameterConstants;
import value.XGValueChangeListener;

public class MultiPartVoiceTab extends JPanel implements XGObjectConstants, XGParameterConstants
{
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*******************************************************************************************************/

	private Map<Integer, XGValueChangeListener> components = new HashMap<>();

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

	private <T extends JComponent & XGValueChangeListener> void addAndRegister(int offs, T c)
	{	super.add(c);
		components.put(offs, c);
	}

	public void xgObjectSelected(XGObject o)
	{	
	}
}

//TODO um die statischen Components/Parameter lauschen zu lassen ist ein "ReceivingListener" erforderlich
//und zur Registrierung an diesem die Initialisierung der Parameter/Components mittels neuer XGAdressen (hi/-/lo);
//hi- und mid-adressen können aus dem XGObjectType bezogen werden zu dem eine Beziehung über parameterMapName besteht, sodass evtl. keine Umstellung der XGParameterMaps.XML erforderlich ist