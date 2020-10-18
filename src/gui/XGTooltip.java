package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class XGTooltip extends JFrame implements GuiConstants	//TODO: Ausführung als JFrame überdenken wegen Focuswechsel; vielleicht als Popup...
{
	private static final long serialVersionUID = 1L;

/**************************************************************************************************************/

	private final JLabel label = new JLabel();

	public XGTooltip()
	{	this.add(this.label);
		this.label.setFont(FONT);
		this.setUndecorated(true);
	}

	@Override public void setName(String name)
	{	this.label.setText(name);
		this.pack();
	}
}
