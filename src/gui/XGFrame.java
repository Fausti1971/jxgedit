package gui;

import java.awt.Component;
import javax.swing.JPanel;

public class XGFrame extends JPanel implements XGComponent
{	/**
	 * 
	 */
	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

	private String text;

	public XGFrame(String txt)
	{	this.text = txt;
		this.colorize();
		this.framize(txt);
	}

	public Component getGuiComponent()
	{	return this;
	}
}
