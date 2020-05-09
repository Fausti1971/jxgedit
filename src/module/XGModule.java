package module;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddressable;
import device.TimeoutException;
import gui.XGTemplate;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindowSource;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public interface XGModule extends XGAddressable, XGModuleConstants, XGMessenger, XGTreeNode, XGWindowSource
{

/********************************************************************************************************************/

	String getName();
	Set<XGModule> getChildModules();
	XGModule getParentModule();
	XGTemplate getGuiTemplate();

	@Override public default void submit(XGResponse msg) throws InvalidXGAddressException
	{
	}

	@Override public default XGResponse request(XGRequest req) throws InvalidXGAddressException, TimeoutException
	{	return null;
	}

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