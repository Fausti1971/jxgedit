package gui;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import adress.XGAddressableSet;
import value.XGValue;

public class XGValueTreeModel implements TreeModel
{
	private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
//	private final XGAddressableSet<XGValue> all;
	private final Map<String, Set<XGValue>> categories;

	public XGValueTreeModel(XGAddressableSet<XGValue> tab)
	{	this.categories  = new LinkedHashMap<>();
		for(XGValue v : tab)
		{	if(this.categories.containsKey(v.getCategory()))
			{	this.categories.get(v.getCategory()).add(v);
			}
			else
			{	Set<XGValue> set = new LinkedHashSet<>();
				set.add(v);
				this.categories.put(v.getCategory(), set);
			}
		}
		this.categories.put("All", tab);
	}

	@Override public Object getRoot()
	{	return this.root;
	}

	@Override public Object getChild(Object parent, int index)
	{	if(parent == this.root) return this.categories.keySet().toArray()[index];
		if(parent instanceof String) return this.categories.get((String)parent).toArray()[index];
		return parent;
	}

	@Override public int getChildCount(Object parent)
	{	if(parent == this.root) return this.categories.size();
		if(parent instanceof XGValue) return 0;
		if(parent instanceof String) return this.categories.get((String)parent).size();
		return 0;
	}

	@Override public boolean isLeaf(Object node)
	{	return this.getChildCount(node) == 0;
	}

	@Override public void valueForPathChanged(TreePath path, Object newValue)
	{
	}

	@Override public int getIndexOfChild(Object parent, Object child)
	{	return -1;
	}

	@Override public void addTreeModelListener(TreeModelListener l)
	{
	}

	@Override public void removeTreeModelListener(TreeModelListener l)
	{
	}
}
