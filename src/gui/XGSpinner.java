package gui;

import javax.swing.JSpinner;
import parm.XGParameter;

public class XGSpinner extends JSpinner
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**************************************************************************************************/

	private final XGParameter parameter;

	public XGSpinner()
	{	this.parameter = null;
	
		this.setAlignmentX(0.5f);
		this.setAlignmentY(0.5f);

	}
}
