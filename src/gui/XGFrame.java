package gui;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class XGFrame extends JPanel implements XGBorderable
{	/**
	 * 
	 */
	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

	public XGFrame(String text)
	{	super();
		this.setName(text);
//		this.colorize();
		this.borderize();
	}
	@Override public JComponent getJComponent()
	{	return this;
	}
}
