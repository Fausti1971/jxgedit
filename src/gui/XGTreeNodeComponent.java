package gui;

import java.awt.Component;
import javax.swing.JLabel;

public class XGTreeNodeComponent extends JLabel implements XGComponent
{	/**
	 * 
	 */
	private static final long serialVersionUID=-5545676580707956206L;

	public static enum Status {unselected, selected, active};

/*********************************************************************************************/

	private Status status = Status.unselected;

	public XGTreeNodeComponent(String text)
	{	super(text);
//		this.setText(text);
//		this.setSize(this.getPreferredSize());

		System.out.println("Node: " + text + this.getSize());
		this.setStatus(Status.unselected);
		this.setOpaque(true);
		this.addMouseListener(this);	//funktioniert nicht, da CellRenderer nur zeichnet; keine Objekte einfügt;

		this.colorize();
		this.framize(text);
	}

	public void setStatus(Status s)
	{	this.status = s;
		switch(s)
		{	default:
			case unselected:
				this.setOpaque(false);
				this.setForeground(COL_NODETEXT);
				break;
			case selected:
				this.setOpaque(true);
				this.setBackground(COL_NODESELECTEDBACK);
				this.setForeground(COL_NODESELECTEDTEXT);
				break;
			case active:
				this.setOpaque(true);
				this.setBackground(COL_NODEFOCUSEDBACK);
				this.setForeground(COL_NODESELECTEDTEXT);
				break;
		}
	}

	public Status getStatus()
	{	return this.status;
	}

	public Component getGuiComponent()
	{	return this;
	}
}
