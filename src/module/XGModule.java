package module;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.Set;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.XGLoggable;
import device.XGDevice;
import gui.XGTemplate;
import msg.XGBulkDump;
import parm.XGTable;
import xml.XMLNode;

public class XGModule implements XGAddressable, XGModuleConstants, XGLoggable
{	static final Set<String> ACTIONS = new LinkedHashSet<>();

	static
	{	ACTIONS.add(ACTION_EDIT);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

	public static XGAddressableSet<XGModule> init(XGDevice dev)
	{	XGAddressableSet<XGModule> set = new XGAddressableSet<>();
		File file;
		try
		{	file = dev.getResourceFile(XML_STRUCTURE);
		}
		catch(FileNotFoundException e)
		{	LOG.warning(e.getMessage());
			return set;
		}
		XMLNode xml = XMLNode.parse(file);
		for(XMLNode n : xml.getChildNodes(TAG_MODULE))
		{	XGModule mod = new XGModule(dev, n);
			dev.getStructure().add(mod);
		}
		LOG.info(dev.getStructure().size() + " modules initialized for " + dev);
		return set;
	}

/********************************************************************************************************************/

	private final XGDevice device;
	private final Set<XGAddress> infoAddresses = new LinkedHashSet<>();
	protected final String name;
	protected final XGAddress address;
	private final XGTemplate guiTemplate;
	protected final XGTable idTranslator;
	private final XMLNode config;
	private final XGAddressableSet<XGBulkDump> bulks;

	protected XGModule(XGDevice dev, XMLNode cfg)//für Prototypen
	{	this.config = cfg;
		this.device = dev;
		this.address = new XGAddress(cfg.getStringAttribute(ATTR_ADDRESS));
		this.name = cfg.getStringAttributeOrDefault(ATTR_NAME, DEF_MODULENAME);
		this.idTranslator = this.device.getTables().get(cfg.getStringAttribute(ATTR_TRANSLATOR));

		XGAddressableSet<XGTemplate> tSet = this.device.getTemplates().getAllIncluded(this.address);
		if(tSet.size() != 1) throw new RuntimeException("found " + tSet.size() + " templates for address " + this.address);
		this.guiTemplate = tSet.iterator().next();

		this.getInfoAddresses().add(new XGAddress(cfg.getStringAttribute(ATTR_INFO1)));
		this.getInfoAddresses().add(new XGAddress(cfg.getStringAttribute(ATTR_INFO2)));
		this.getInfoAddresses().add(new XGAddress(cfg.getStringAttribute(ATTR_INFO3)));
		 
		this.bulks = XGBulkDump.init(this);
	}

	public XGDevice getDevice()
	{	return this.device;
	}

	public XGTemplate getGuiTemplate()
	{	return this.guiTemplate;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public Set<XGAddress> getInfoAddresses()
	{
		return infoAddresses;
	}

	public XGAddressableSet<XGBulkDump> getBulks()
	{	return this.bulks;
	}

	public XMLNode getConfig()
	{	return this.config;
	}

	public String getName()
	{	return this.name;
	}
}