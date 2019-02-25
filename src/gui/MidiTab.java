package gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import parm.XGParameterConstants;

public class MidiTab extends JPanel
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	
	public MidiTab()
	{	add(new JButton("Test"));
		addAndRegister(new MySlider(XGParameterConstants.MP_ELRES));
		addAndRegister(new MyCombo(XGParameterConstants.MP_CH));
		
//		Tags tag, XGAdress adr, ByteType bType, AdressType aType, TranslationType tType, int min, int max, String lName, String sName
	}

	private <T extends JComponent & XGObjectSelectionListener> void addAndRegister(T c)
	{	super.add(c);
		MultiPartListView.instance.registerXGObjectChangeListener(c);
	}
}
