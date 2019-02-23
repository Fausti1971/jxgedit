package gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import application.MU80;
import obj.XGObjectMultiPart;
import parm.XGParameterConstants.Tags;

public class MidiTab extends JPanel
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	
	public MidiTab()
	{	add(new JButton("Test"));
		addAndRegister(new MySlider(XGObjectMultiPart.parameters.get(Tags.mp_elRes)));
		addAndRegister(new MyCombo(XGObjectMultiPart.parameters.get(Tags.mp_ch)));
		
//		Tags tag, XGAdress adr, ByteType bType, AdressType aType, TranslationType tType, int min, int max, String lName, String sName
	}

	private <T extends JComponent & XGObjectChangeListener> void addAndRegister(T c)
	{	super.add(c);
		MU80.multiPartListView.registerXGObjectChangeListener(c);
	}
}
