package gui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import parm.XGParameterConstants;

public class MultiPartVoiceTab extends JPanel implements XGParameterConstants
{
	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public MultiPartVoiceTab()
	{	addAndRegister(new LeftZeroSlider(MP_ELRES));
		addAndRegister(new LeftZeroSlider(MP_TUNE));
		addAndRegister(new LeftZeroSlider(MP_ATACK));
	}

	private <T extends JComponent & XGObjectSelectionListener> void addAndRegister(T c)
	{	super.add(c);
//		XGObjectTableView.getInstance().registerXGObjectSelectionListener(c);
		MultiPartListView.getInstance().registerXGObjectSelectionListener(c);
	}
}

