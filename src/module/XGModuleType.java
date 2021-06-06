package module;

import java.io.IOException;import java.util.LinkedHashSet;
import java.util.Set;
import adress.*;
import static application.JXG.XMLPATH;import config.Configurable;
import application.JXG;import application.XGLoggable;
import msg.XGBulkDumper;
import parm.XGOpcode;
import parm.XGTable;
import static parm.XGTable.TABLES;
import static parm.XGVirtualTable.DEF_TABLE;import tag.*;
import xml.XGProperty;import xml.XMLNode;

/**
 * Moduletypen, keine Instanzen
 * @author thomas
 *
 */
public class XGModuleType implements XGAddressable, XGModuleConstants, XGLoggable, XGBulkDumper, Configurable, XGTagable
{
	public static final XGTagableAddressableSet<XGModuleType> TYPES = new XGTagableAddressableSet<>();//Prototypen (inkl. XGAddress bulks); initialisiert auch XGOpcodes
	static final Set<String> ACTIONS = new LinkedHashSet<>();

/**
* instanziiert Moduletypen, Bulktypen und Valuetypen (=XGOpcodes)
*/
	public static void init()
	{	XMLNode xml;
		try
		{	xml = XMLNode.parse(JXG.getResourceStream(XMLPATH + XML_STRUCTURE));
		}
		catch(IOException e)
		{	LOG.severe(e.getMessage());
			return;
		}
		for(XMLNode n : xml.getChildNodes(TAG_MODULE))
		{	XGAddress adr = new XGAddress(n.getStringAttribute(ATTR_ADDRESS));
			if(adr.getHi().getMin() >= 48)//falls Drumset
			{	for(int h : adr.getHi())//erzeuge für jedes Drumset ein ModuleType
				{	try
					{	TYPES.add(new XGDrumsetModuleType(n, new XGAddress(new XGAddressField(h), adr.getMid(), adr.getLo())));
					}
					catch(InvalidXGAddressException e)
					{	LOG.severe(e.getMessage());
					}
				}
				continue;
			}
			TYPES.add(new XGModuleType(n));
		}
		LOG.info(TYPES.size() + " Module-Types initialized");
	}

	static
	{	ACTIONS.add(ACTION_EDIT);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

/********************************************************************************************************************/

	private final Set<String> infoOpcodes = new LinkedHashSet<>();
	private final XGTagableAddressableSet<XGOpcode> opcodes = new XGTagableAddressableSet<>();
	private final XGAddressableSet<XGModule> modules = new XGAddressableSet<>();
	protected final StringBuffer name;
	protected String tag;
	protected final XGAddress address;
	protected XGTable idTranslator;
	private final XMLNode config;
	private final XGAddressableSet<XGAddress> bulks = new XGAddressableSet<>();

/**
* instanziiert Moduletypen, Bulktypen und Valuetypen (XGOpcode)
*/
	public XGModuleType(XMLNode cfg, XGAddress adr, String name)
	{	this.config = cfg;
		this.address = adr;
		this.name = new StringBuffer(name);
		this.tag = cfg.getStringAttribute(ATTR_ID);
		this.idTranslator = TABLES.getOrDefault(cfg.getStringAttribute(ATTR_TABLE), DEF_TABLE);

		for(XMLNode x : cfg.getChildNodes(TAG_BULK))
		{	XGAddress a = new XGAddress(x.getStringAttribute(ATTR_ADDRESS), this.address);
			this.bulks.add(a);
			for(XMLNode o : x.getChildNodes(TAG_OPCODE))
			{	try
				{	this.opcodes.add(new XGOpcode(this, a, o));
				}
				catch(InvalidXGAddressException e)
				{	LOG.severe(e.getMessage());
				}
			}
		}

		for(XMLNode n : cfg.getChildNodes(TAG_INFO))
		{	String opc = n.getStringAttribute(ATTR_REF);
			if(opc != null) this.infoOpcodes.add(opc);
		}
	}

	public XGModuleType(XMLNode cfg)
	{	this(cfg, new XGAddress(cfg.getStringAttribute(ATTR_ADDRESS)), cfg.getStringAttributeOrDefault(ATTR_NAME, DEF_MODULENAME));
	}

	public XGAddressableSet<XGModule> getModules()
	{	return this.modules;
	}

	public XGTagableAddressableSet<XGOpcode> getOpcodes()
	{	return this.opcodes;
	}

	public Set<String> getInfoOpcodes()
	{	return this.infoOpcodes;
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public void propertyChanged(XGProperty n)
	{
	}

	public String getName()
	{	return this.name.toString();
	}

	//public XGAddressableSet<XGOpcode> getOpcodes()
	//{	return XGOpcode.OPCODES.getAllIncluded(this.address);
	//}

	public XGAddressableSet<XGAddress> getBulkAdresses()
	{	return this.bulks;
	}

	public void resetValues()
	{	for(XGModule m : this.getModules()) m.resetValues();
	}

	@Override public String toString()
	{	return this.name.toString();
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	XGAddressableSet<XGAddress> set = new XGAddressableSet<>();
		for(XGModule m : this.getModules()) set.addAll(m.getBulks());
		return set;
	}

	public String getTag()
	{	return this.tag.toString();
	}
}