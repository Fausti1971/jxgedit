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
		addAndRegister(new LeftZeroSlider(XGParameterConstants.MP_ELRES));
		addAndRegister(new MyCombo(XGParameterConstants.MP_CH));
		addAndRegister(new LeftZeroSlider(XGParameterConstants.MP_TUNE));
	}

	private <T extends JComponent & XGObjectSelectionListener> void addAndRegister(T c)
	{	super.add(c);
		MultiPartListView.getInstance().registerXGObjectChangeListener(c);
	}
}
