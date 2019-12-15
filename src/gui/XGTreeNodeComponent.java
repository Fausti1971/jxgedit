package gui;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class XGTreeNodeComponent extends JLabel implements XGComponent, XGColorable, XGFrameable
{	/**
	 * 
	 */
	private static final long serialVersionUID=-5545676580707956206L;

/*********************************************************************************************/

	private final XGTreeNode node;

	public XGTreeNodeComponent(XGTreeNode n)
	{	super(n.toString());
		this.node = n;
//		this.setOpaque(true);
//		this.colorize();
//		this.deframize();
	}

	public void setStatus()
	{	if(this.node.isSelected())
		{	this.setBackground(this.getForeground());
//			this.setForeground(COL_NODESELECTEDTEXT);
		}
		else
		{	this.setBackground(this.getBackground());
//			this.setForeground(COL_NODETEXT);
		}
		if(this.node.hasFocus())
		{	this.framize(null);
		}
		else
		{	this.deframize();
		}
		this.node.repaintNode();
	}

	public JComponent getGuiComponent()
	{	return this;
	}

	@Override public JComponent getParentComponent()
	{	return this.node.getTree();
	}
}
