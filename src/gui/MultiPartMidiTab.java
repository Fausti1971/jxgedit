package gui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import parm.XGParameterConstants;

public class MultiPartMidiTab extends JPanel implements XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	
	public MultiPartMidiTab()
	{	addAndRegister(new MyCombo(MP_CH));
	}

	private <T extends JComponent & XGObjectSelectionListener> void addAndRegister(T c)
	{	super.add(c);
//		XGObjectTableView.getInstance().registerXGObjectSelectionListener(c);
		MultiPartListView.getInstance().registerXGObjectSelectionListener(c);
	}
}
