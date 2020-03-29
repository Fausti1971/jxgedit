package module;

import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import adress.XGBulkDump;
import device.TimeoutException;
import device.XGDevice;
import gui.XGTree;
import msg.XGRequest;
import msg.XGResponse;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGSuperModule implements XGModule, XMLNodeConstants
{
	private final XGModuleTag tag;
	private final XGAddress address;
	private final XGAddressableSet<XGBulkDump> bulks;
	private final XGDevice device;
	private final XGModule parentModule;
//	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<XGValue>();
	private final XMLNode guiTemplate;

	public XGSuperModule(XGDevice dev, XMLNode n)
	{	this.parentModule = null;
		this.device = dev;
		this.tag = XGModuleTag.valueOf(n.getStringAttribute(ATTR_ID));
		this.address = new XGAddress(n);
		this.bulks = XGBulkDump.init(n);
		this.guiTemplate = null; //TODO:
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

	@Override public XMLNode getGuiTemplate()
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

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public String getNodeText()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override public XGAddressableSet<XGBulkDump> getBulks()
	{	return this.bulks;
	}
}
