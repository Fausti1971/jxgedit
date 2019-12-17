package gui;

import java.util.Enumeration;
import java.util.Set;
import javax.swing.tree.TreeNode;
import value.XGValue;

public class XGFilterTreeNode implements XGTreeNode
{	private final XGTreeNode parent;
	private final Set<XGValue> data;
	private boolean selected = false;

	public XGFilterTreeNode(XGTreeNode par, Set<XGValue> set)
	{	this.parent = par;
		this.data = set;
	}

	public TreeNode getParent()
	{	return this.parent;
	}

	public boolean getAllowsChildren()
	{	return true;
	}

	public Enumeration<? extends TreeNode> children()
	{	return null;
	}

	public void setSelected(boolean s)
	{	this.selected = s;
	}

	public boolean isSelected()
	{	return this.selected;
	}

}
