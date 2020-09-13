package gui;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.JXG;
import device.XGDevice;
import module.XGModule;
import module.XGModuleType;

public class XGMainTreeModel implements TreeModel
{

	private XGAddressableSet<XGAddressable> foldedModules(XGDevice dev)
	{	XGAddressableSet<XGAddressable> set = new XGAddressableSet<>();
		for(XGModuleType t : dev.getModuleTypes())
		{	XGAddressableSet<XGModule> temp = t.getModules();
			if(temp.size() == 0) continue;
			if(temp.size() == 1) set.add(temp.get(0));
			else set.add(t);
		}
		return set;
	}

	@Override public Object getRoot()
	{	return JXG.getApp();
	}

	@Override public Object getChild(Object parent, int index)
	{	if(parent == this.getRoot()) return XGDevice.getDevices().toArray()[index];
		if(parent instanceof XGDevice) return this.foldedModules(((XGDevice)parent)).get(index);
		if(parent instanceof XGModuleType) return ((XGModuleType)parent).getModules().get(index);
//		if(parent instanceof XGModule) return ((XGModule)parent).getInstances().toArray()[index];
		return parent;
	}

	@Override public int getChildCount(Object parent)
	{	if(parent == this.getRoot()) return XGDevice.getDevices().size();
		if(parent instanceof XGDevice) return this.foldedModules((XGDevice)parent).size();
		if(parent instanceof XGModuleType) return ((XGModuleType)parent).getModules().size();
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
