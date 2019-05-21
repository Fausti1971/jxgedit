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

//TODO um die statischen Components und Parameter lauschen zu lassen ist ein "ReceivingListener" und zur Registrierung der Parameter die Initialisierung mittels XGAdressen (hi/-/lo) erforderlich