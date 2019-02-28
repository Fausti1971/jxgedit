package gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import parm.XGParameterConstants;

public class MultiPartMidiTab extends JPanel implements XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	
	public MultiPartMidiTab()
	{	add(new JButton("Test"));
		addAndRegister(new LeftZeroSlider(MP_ELRES));
		addAndRegister(new MyCombo(MP_CH));
		addAndRegister(new LeftZeroSlider(MP_TUNE));
	}

	private <T extends JComponent & XGObjectSelectionListener> void addAndRegister(T c)
	{	super.add(c);
		MultiPartListView.getInstance().registerXGObjectSelectionListener(c);
	}
}
