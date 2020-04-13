package opcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
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
				{	XGOpcode opc = new XGOpcode(mod, blk, o);
					dev.getOpcodes().add(opc);
				}
			}
		}
		log.info(dev.getOpcodes().size() + " opcodes initialized");
		return;
	}

	public static ValueDataClass getValueDataClass(String s)
	{	if(s == null) return DEF_VALUECLASS;
		try
		{	return ValueDataClass.valueOf(s);
		}
		catch(IllegalArgumentException ex)
		{	return DEF_VALUECLASS;
		}
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

	private final String tag, dependencyTag;
	private final XGOpcodeConstants.ValueDataType dependencyType;
	private final XGModule module;
	private final XGBulkDump bulk;
	private final XGAddress address;
	private final ValueDataType dType;
	private final Map<Integer, XGParameter> parameters;

	private XGOpcode(XGModule mod, XGBulkDump bulk, XMLNode n)
	{	this.module = mod;
		this.bulk = bulk;
		this.address = new XGAddress(bulk.getAddress(), n);
		this.tag = n.getStringAttribute(ATTR_ID);
		this.dType = getDataType(n.getStringAttribute(ATTR_DATATYPE));
		this.dependencyTag = n.getStringAttribute(ATTR_DEPENDING);
		this.dependencyType = ValueDataType.valueOf(n.getStringAttribute(ATTR_DEP_TYPE, DEF_DATATYPE.name()));
		this.parameters = XGParameter.init(this, n);
		log.info("opcode initialized: " + this.getInfo());
	}

	public XGOpcode(XGDevice dev, XGAddress adr)
	{	this.tag = DEF_OPCODENAME + adr;
		this.module = dev.getModules().get(adr);
		this.bulk = null;
		this.address = adr;
		this.dType = DEF_DATATYPE;
		this.parameters = new HashMap<>();
		this.parameters.put(0, new XGParameter(adr.toString()));
		this.dependencyTag = null;
		this.dependencyType = null;
	}

	public String getInfo()
	{	return this.tag + " " + this.address;
	}

	@Override public String getTag()
	{	return this.tag;
	}

	public String getDependencyTag()
	{	return this.dependencyTag;
	}

	public ValueDataType getDataType()
	{	return this.dType;
	}

	public XGOpcodeConstants.ValueDataType getDependencyType()
	{	return this.dependencyType;
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

	public XGParameter getParameter(int i)
	{	if(this.parameters == null) return null;
		return this.parameters.get(i);
	}

	public Map<Integer, XGParameter> getParameters()
	{	return parameters;
	}
}
