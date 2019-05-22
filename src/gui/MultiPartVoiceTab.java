package gui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import obj.XGObject;
import parm.XGParameterConstants;
import value.XGValueChangeListener;

public class MultiPartVoiceTab extends JPanel implements XGParameterConstants, XGObjectSelectionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/*******************************************************************************************************/

	private Map<Integer, XGValueChangeListener> components = new HashMap<>();

	public MultiPartVoiceTab()
	{	addAndRegister(MP_ELRES, new LeftZeroSlider(MP_ELRES));
		addAndRegister(MP_TUNE, new LeftZeroSlider(MP_TUNE));
		addAndRegister(MP_ATACK, new LeftZeroSlider(MP_ATACK));

		MultiPartTab.getTableView().registerObjectSelectionListener(this);
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