package gui;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import table.XGTable;
import table.XGTableEntry;

public class XGCategorizedTableTreeModel implements TreeModel
{
	/******************************************************************************************************/

	private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
	private final XGTable table;

	public XGCategorizedTableTreeModel(XGTable tab)
	{	this.table = tab;
	}

	@Override public Object getRoot(){	return this.root;}

	@Override public Object getChild(Object parent, int index)
	{	if(parent == this.root) return this.table.getCategories().toArray()[index];
		if(parent instanceof String) return this.table.categorize((String)parent).getByIndex(index);
		return parent;
	}

	@Override public int getChildCount(Object parent)
	{	if(parent == this.root) return this.table.getCategories().size();
		if(parent instanceof XGTableEntry) return 0;
		if(parent instanceof String) return this.table.categorize((String)parent).size();
		return 0;
	}

	@Override public boolean isLeaf(Object node){	return this.getChildCount(node) == 0;}

	@Override public void valueForPathChanged(TreePath path, Object newValue){}

	@Override public int getIndexOfChild(Object parent, Object child){	return -1;}

	@Override public void addTreeModelListener(TreeModelListener l){}

	@Override public void removeTreeModelListener(TreeModelListener l){}
}
