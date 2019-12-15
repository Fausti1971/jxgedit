package gui;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class XGFrame extends JPanel implements XGComponent, XGColorable, XGFrameable
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

	public JComponent getGuiComponent()
	{	return this;
	}

	public Component getParentComponent()
	{	return this.getGuiComponent().getParent();
	}

	public void leftClicked()
	{	
	}

	public void rightClicked()
	{	
	}
}
