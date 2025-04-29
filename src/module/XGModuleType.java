package module;

import java.io.IOException;import java.util.LinkedHashSet;
import java.util.Set;
import adress.*;
import bulk.XGBulk;import bulk.XGBulkDumper;import bulk.XGBulkType;import xml.XGConfigurable;
import application.XGLoggable;
import msg.XGClippboard;import table.XGTable;import table.XGTableConstants;import table.XGVirtualTable;import tag.XGTagable;import tag.XGTagableSet;import xml.XMLNode;

/**
 * Moduletypen, keine Instanzen
 * @author thomas
 *
 */
public class XGModuleType implements XGModuleConstants, XGLoggable, XGBulkDumper, XGConfigurable, XGTagable
{
	public static final XGTagableSet<XGModuleType> MODULE_TYPES = new XGTagableSet<>();//Prototypen (inkl. XGAddress bulks); initialisiert auch XGOpcodes

/**
* instanziiert Moduletypen, Bulktypen und Valuetypen (=XGOpcodes)
*/
	public static void init()
	{	XMLNode xml;
		XGDrumsetModuleType.init();
		try
		{	xml = XMLNode.parse(XML_DEVICE);
		}
		catch(IOException e)
		{	LOG.severe(e.getMessage());
			return;
		}
		for(XMLNode n : xml.getChildNodes(TAG_MODULE))
		{	XGAddressRange adr = new XGAddressRange(n.getStringAttribute(ATTR_ADDRESS));
			if(adr.getHi().getMin() >= 48)//falls Drumset
			{	for(int h : adr.getHi())//erzeuge je Drumset ein ModuleType
				{	try
					{	MODULE_TYPES.add(new XGDrumsetModuleType(n, h));
					}
					catch(XGInvalidAddressException e)
					{	LOG.severe(e.getMessage());
					}
				}
				continue;
			}
			try
			{	MODULE_TYPES.add(new XGModuleType(n));
			}
			catch(XGInvalidAddressException e){	e.printStackTrace();}
		}
		LOG.info(MODULE_TYPES.size() + " Module-Types initialized");
	}

	public static XGModuleType getModuleType(XGAddress adr)
	{	for(XGModuleType mt : MODULE_TYPES)
			if(mt.getAddressRange().contains(adr)) return mt;
		return null;
	}

/********************************************************************************************************************/

	private final Set<String> infoTags = new LinkedHashSet<>();
	private final XGTagableSet<XGBulkType> bulkTypes = new XGTagableSet<>();
	private final XGIdentifiableSet<XGModule> modules = new XGIdentifiableSet<>();
	final XGAddressRange addressRange;
	protected final StringBuffer name;
	protected String tag;
	protected XGTable idTranslator;
	protected final XMLNode config;
	private final XGClippboard clippboard = new XGClippboard();

/**
* instanziiert Moduletypen, Bulktypen und Valuetypen
*/
	private XGModuleType(XMLNode cfg, String name)throws XGInvalidAddressException
	{	this.config = cfg;
		this.name = new StringBuffer(name);
		this.tag = cfg.getStringAttribute(ATTR_ID);
		this.idTranslator = XGTable.TABLES.getOrDefault(cfg.getStringAttribute(ATTR_TABLE), XGTable.TABLES.get(XGTableConstants.TABLE_NONE));
		
		this.addressRange = new XGAddressRange(cfg.getStringAttribute(ATTR_ADDRESS));

		for(XMLNode x : cfg.getChildNodes(TAG_BULK)) this.bulkTypes.add(new XGBulkType(this, x));

		for(XMLNode n : cfg.getChildNodes(TAG_INFO))
		{	String opc = n.getStringAttribute(ATTR_REF);
			if(opc != null) this.infoTags.add(opc);
		}
	}

	XGModuleType(XMLNode cfg, String name, int hi)throws XGInvalidAddressException
	{	this.config = cfg;
		this.name = new StringBuffer(name);
		this.tag = cfg.getStringAttribute(ATTR_ID);
		this.idTranslator = XGTable.TABLES.getOrDefault(cfg.getStringAttribute(ATTR_TABLE), XGVirtualTable.DEF_TABLE);

		XGAddressRange adr = new XGAddressRange(cfg.getStringAttribute(ATTR_ADDRESS));
		this.addressRange = new XGAddressRange(new XGAddressField(hi), adr.getMid(), adr.getLo());

		for(XMLNode x : cfg.getChildNodes(TAG_BULK))
		{	this.bulkTypes.add(new XGBulkType(this, x));
		}

		for(XMLNode n : cfg.getChildNodes(TAG_INFO))
		{	String opc = n.getStringAttribute(ATTR_REF);
			if(opc != null) this.infoTags.add(opc);
		}
	}

	private XGModuleType(XMLNode cfg)throws XGInvalidAddressException
	{	this(cfg, cfg.getStringAttributeOrDefault(ATTR_NAME, DEF_MODULENAME));
	}

	public XGClippboard getClippboard(){	return this.clippboard;}

	public XGAddressRange getAddressRange(){	return this.addressRange;}

	public XGIdentifiableSet<XGModule> getModules(){ return this.modules;}

	public Set<String> getInfoTags(){ return this.infoTags;}

	@Override public XMLNode getConfig(){ return this.config;}

	public String getName(){ return this.name.toString();}

	public void resetValues(){	for(XGModule m : this.getModules()) m.resetValues();}

	@Override public String toString(){ return this.name.toString();}

	public XGAddressableSet<XGBulk> getBulks()
	{	XGAddressableSet<XGBulk> set = new XGAddressableSet<>();
		for(XGModule m : this.modules) set.addAll(m.getBulks());
		return set;
	}

	public String getTag(){ return this.tag;}

	public Set<XGBulkType> getBulkTypes(){	return this.bulkTypes;}
}