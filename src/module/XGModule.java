package module;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import javax.swing.tree.TreeNode;
import adress.XGAddressable;
import adress.XGAddressableSet;
import adress.XGBulkDump;
import gui.XGTemplate;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindowSource;
import msg.XGMessenger;

public interface XGModule extends XGAddressable, XGModuleConstants, XGMessenger, XGTreeNode, XGWindowSource
{

/********************************************************************************************************************/

	void request();
	String getName();
	Set<XGModule> getChildModules();
	XGAddressableSet<XGBulkDump> getBulks();
	XGModule getParentModule();
	XGTemplate getGuiTemplate();

	@Override default boolean isLeaf()
	{	return this.getChildCount() < 2;
	}

	@Override public default XGTree getTreeComponent()
	{	return this.getDevice().getTreeComponent();
	}

	@Override public default TreeNode getParent()
	{	if(this.getParentModule() == null) return this.getDevice();
		else return this.getParentModule();
	}

	@Override public default boolean getAllowsChildren()
	{	return true;
	}

	@Override public default Enumeration<? extends TreeNode> children()
	{	if(this.getChildModules() == null) return Collections.emptyEnumeration();
		else return Collections.enumeration(this.getChildModules());
	}
}