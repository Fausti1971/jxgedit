package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class XGTooltip extends JFrame implements XGUI
{
	private static final long serialVersionUID = 1L;

/**************************************************************************************************************/

	private final JLabel label = new JLabel();

	public XGTooltip()
	{	this.add(this.label);
		this.label.setFont(SMALL_FONT);
		this.setUndecorated(true);
		this.setFocusable(false);
		this.setAutoRequestFocus(false);
//		this.setFocusableWindowState(false);
	}

	@Override public void setName(String name)
	{	this.label.setText(name);
		this.pack();
	}
}
