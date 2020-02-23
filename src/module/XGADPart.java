package module;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Set;
import javax.swing.tree.TreeNode;
import adress.XGAddress;
import device.XGDevice;
import gui.XGTree;

public class XGADPart extends XGSuperModule
{
	private static final XGModuleTag TAG = XGModuleTag.adpart;

/********************************************************************************************************/

	protected XGADPart(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, adr);
	}

	@Override public String getTag()
	{	return TAG.name();
	}

	@Override public XGTree getTreeComponent()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override public void setTreeComponent(XGTree t)
	{
		// TODO Auto-generated method stub
		
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

	@Override public boolean getAllowsChildren()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override public Enumeration<? extends TreeNode> children()
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
}
