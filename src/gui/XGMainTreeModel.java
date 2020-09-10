package gui;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import application.JXG;
import device.XGDevice;
import module.XGModule;
import module.XGModuleFolder;

public class XGMainTreeModel implements TreeModel
{
	@Override public Object getRoot()
	{	return JXG.getApp();
	}

	@Override public Object getChild(Object parent, int index)
	{	if(parent == this.getRoot()) return XGDevice.getDevices().toArray()[index];
		if(parent instanceof XGDevice) return ((XGDevice)parent).getModules().toArray()[index];
		if(parent instanceof XGModuleFolder) ((XGModuleFolder)parent).getChildAt(index);
		if(parent instanceof XGModule) return ((XGModule)parent).getInstances().toArray()[index];
		return parent;
	}

	@Override public int getChildCount(Object parent)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override public boolean isLeaf(Object node)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override public void valueForPathChanged(TreePath path, Object newValue)
	{
		// TODO Auto-generated method stub
	}

	@Override public int getIndexOfChild(Object parent, Object child)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override public void addTreeModelListener(TreeModelListener l)
	{
		// TODO Auto-generated method stub
	}

	@Override public void removeTreeModelListener(TreeModelListener l)
	{
		// TODO Auto-generated method stub
	}
}
