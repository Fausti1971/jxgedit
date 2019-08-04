package gui;
	
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import adress.XGAdressableSet;
import adress.XGAdressableSetListener;
import value.XGValue;
	
public class XGTreeModel implements TreeModel, XGAdressableSetListener
{	private XGAdressableSet<? extends XGAdressable> root;
	private Set<TreeModelListener> tml = new HashSet<>();

	public XGTreeModel(XGAdressableSet<? extends XGAdressable> root)
	{	this.root = root;
		root.addListener(this);
	}

	public Object getRoot()
	{	return root;}
	
	public Object getChild(Object parent,int index)
	{	if(parent instanceof XGAdressableSet<?>) return ((XGAdressableSet<?>)parent).get(index);
		return null;
	}
	
	public int getChildCount(Object parent)
	{	if(parent instanceof XGAdressableSet<?>) return ((XGAdressableSet<?>)parent).size();
		return 0;
	}
	
	public boolean isLeaf(Object node)
	{	return getChildCount(node) == 0;}
	
	public void valueForPathChanged(TreePath path,Object newValue)
	{	
	}
	
	public int getIndexOfChild(Object parent,Object child)
	{	return 0;
	}
	
	public void addTreeModelListener(TreeModelListener l)
	{
	}
	
	public void removeTreeModelListener(TreeModelListener l)
	{
	}

	public void setChanged(XGAdress adr)
	{	for(TreeModelListener l : this.tml)
		{	try
			{	l.treeStructureChanged(new TreeModelEvent(this, new TreePath(XGValue.getValue(adr))));}
			catch(InvalidXGAdressException e)
			{	e.printStackTrace();}
		}
	}
}
