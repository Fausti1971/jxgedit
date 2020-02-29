package module;

import java.awt.event.ActionEvent;
import java.util.Set;
import adress.XGAddress;
import device.XGDevice;
import gui.XGTree;

public class XGMultiEQ extends XGSuperModule
{	private static final XGModuleTag TAG =XGModuleTag.syseq;

	protected XGMultiEQ(XGDevice dev, XGModule par, XGAddress adr)
	{
		super(dev, par, adr);
		// TODO Auto-generated constructor stub
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
