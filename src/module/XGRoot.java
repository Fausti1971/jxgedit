package module;

import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.tree.TreeNode;
import device.XGDevice;
import gui.XGTree;

public class XGRoot extends XGSuperModule
{
	protected XGRoot(XGDevice dev)
	{	super(dev, null, XGModuleTag.unknown, INVALIDADRESS);
	}

	@Override public void setTreeComponent(XGTree t)
	{
	}

	@Override public void setSelected(boolean s)
	{
		// TODO Auto-generated method stub
		
	}

	@Override public boolean isSelected()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override public void nodeFocussed(boolean b)
	{
		// TODO Auto-generated method stub
		
	}

	@Override public String getNodeText()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override public Set<String> getContexts()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override public TreeNode getParent()
	{	return this.getDevice();
	}

}
