package module;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.Set;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import device.TimeoutException;
import device.XGDevice;
import gui.XGTree;
import msg.XGRequest;
import msg.XGResponse;
import obj.XGBulkDumpSequence;
import xml.XMLNode;
import xml.XMLNodeConstants;

public abstract class XGSuperModule implements XGModule, XMLNodeConstants
{
	private final XGModuleTag tag;
	private final Set<XGBulkDumpSequence> bulks;
	private final XGDevice device;
	private final XGModule parentModule;
//	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<XGValue>();
	private final XMLNode guiTemplate;

	protected XGSuperModule(XGDevice dev, Set<XGBulkDumpSequence> bulks, XGModuleTag tag)
	{	this.tag = tag;
		this.bulks = bulks;
		this.device = dev;
		this.parentModule = null;
		this.guiTemplate = 
	}

	protected XGSuperModule(XGModule par)
	{	if(par == null) return;
		this.parentModule = par;

		this.device = par.getDevice();
		this.tag = (XGModuleTag)par.getTag();
		this.guiTemplate = par.getGuiTemplate();
	}

	@Override public XGModuleTag getTag()
	{	return this.tag;
	}

	@Override public XGModule getParentModule()
	{	return this.parentModule;
	}

	@Override public Set<XGModule> getChildModules()
	{	return null; //TODO
	}

	@Override public XGDevice getDevice()
	{	return this.device;
	}

	public XMLNode getGuiTemplate()
	{	return guiTemplate;
	}
/*
	@Override public XGTagableAddressableSet<XGValue> getValues()
	{	return this.values;
	}

	@Override public XGTagableAddressableSet<XGOpcode> getOpcodes()
	{	return this.device.getOpcodes(this.tag);
	}
*/
	@Override public void setTreeComponent(XGTree t)
	{
	}

	@Override public TreeNode getParent()
	{	return this.parentModule;
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

	@Override public String getMessengerName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override public void submit(XGResponse msg) throws InvalidXGAddressException
	{
	}

	@Override public XGResponse request(XGRequest req) throws InvalidXGAddressException, TimeoutException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
