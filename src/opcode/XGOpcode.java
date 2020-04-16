package opcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGBulkDump;
import application.ConfigurationConstants;
import device.XGDevice;
import module.XGModule;
import module.XGModuleConstants.XGModuleTag;
import parm.XGParameter;
import tag.XGTagable;
import value.XGValue;
import xml.XMLNode;

public class XGOpcode implements ConfigurationConstants, XGOpcodeConstants, XGTagable, XGAddressable
{	private static Logger log = Logger.getAnonymousLogger();

	public static void init(XGDevice dev)
	{	
		File file;
		try
		{	file = dev.getResourceFile(XML_PARAMETER);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
			return;
		}

		XMLNode xml = XMLNode.parse(file);
		for(XMLNode m : xml.getChildNodes(TAG_MODULE))
		{	XGModule mod = XGModule.newInstances(dev, m);
			dev.getModules().add(mod);
			for(XMLNode b : m.getChildNodes(TAG_BULK))
			{	XGBulkDump blk = new XGBulkDump(mod, b);
				dev.getBulks().add(blk);
				for(XMLNode o : b.getChildNodes(TAG_OPCODE))
				{	XGOpcode opc = new XGOpcode(dev, mod, blk, o);
					dev.getOpcodes().add(opc);
				}
			}
		}
		log.info(dev.getOpcodes().size() + " opcodes initialized");
		return;
	}

	public static ValueDataType getDataType(String s)
	{	if(s == null) return DEF_DATATYPE;
		try
		{	return ValueDataType.valueOf(s);
		}
		catch(IllegalArgumentException e)
		{	return DEF_DATATYPE;
		}
	}

/*********************************************************************************************************/

	private final String tag;
	private final XGModule module;
	private final XGBulkDump bulk;
	private final XGAddress address;
	private final ValueDataType dType;
	private final XGParameter parameter;

	private XGOpcode(XGDevice dev, XGModule mod, XGBulkDump bulk, XMLNode n)//für init via xml, initialisiert für alle addresses ein XGValue
	{	this.module = mod;
		this.bulk = bulk;
		this.address = new XGAddress(bulk.getAddress(), n);
		this.tag = n.getStringAttribute(ATTR_ID);
		this.dType = getDataType(n.getStringAttribute(ATTR_DATATYPE, DEF_DATATYPE.name()));
		this.parameter = new XGParameter(n);
		XGValue.init(dev, this);
		log.info("opcode initialized: " + this.getInfo());

	}

/*	public XGOpcode(XGDevice dev, XGAddress adr)
	{	this.tag = DEF_OPCODENAME + adr;
		this.module = dev.getModules().get(adr);
		this.bulk = null;
		this.address = adr;
		this.dType = DEF_DATATYPE;
		this.parameters = new HashMap<>();
		this.parameters.put(0, new XGParameter(adr.toString()));
		this.dependencyTag = null;
		this.dependencyType = null;
		log.info("opcode initialized: " + this.getInfo());
		}
*/
	public XGOpcode(String name, int v)//Dummy-Opcode für Festwerte
	{	this.tag = name;
		this.module = null;
		this.bulk = null;
		this.address = null;
		this.dType = ValueDataType.LSB;
		this.parameter = null;
		log.info("dummy opcode initialized: " + this.getInfo());
	}

	public String getInfo()
	{	return this.tag + " " + this.address;
	}

	@Override public String getTag()
	{	return this.tag;
	}

	public ValueDataType getDataType()
	{	return this.dType;
	}

	@Override public String toString()
	{	return this.getTag();
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public XGModule getModule()
	{	return this.module;
	}

	public XGBulkDump getBulk()
	{	return this.bulk;
	}

	public XGModuleTag getModuleTag()
	{	return (XGModuleTag)this.module.getTag();
	}

	public XGParameter getParameter()
	{	return this.parameter;
	}
}
