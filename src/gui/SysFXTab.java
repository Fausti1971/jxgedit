package gui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import obj.XGObjectConstants;
import parm.XGParameterConstants;

public class SysFXTab extends JComponent implements XGParameterConstants, XGObjectConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	JPanel revPanel = new JPanel(), choPanel = new JPanel(), varPanel = new JPanel(), panel;
	public SysFXTab()
	{	this.add(revPanel);
		this.add(choPanel);
		this.add(varPanel);
		
		revPanel.add(new LeftZeroSlider(FX1_REV_P01));
		
	}
}
