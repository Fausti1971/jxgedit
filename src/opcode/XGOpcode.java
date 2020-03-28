package opcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.logging.Logger;
import adress.XGAddress;
import adress.XGAddressable;
import application.ConfigurationConstants;
import device.XGDevice;
import module.XGModule;
import module.XGModuleConstants.XGModuleTag;
import parm.XGParameter;
import tag.XGTagable;
import tag.XGTagableAddressableSet;
import xml.XMLNode;

public class XGOpcode implements ConfigurationConstants, XGOpcodeConstants, XGTagable, XGAddressable
{	private static Logger log = Logger.getAnonymousLogger();

	public static XGTagableAddressableSet<XGOpcode> init(XGDevice dev)
	{	
		XGTagableAddressableSet<XGOpcode> opcodes = new XGTagableAddressableSet<>();
		File file;
		try
		{	file = dev.getResourceFile(XML_OPCODE);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
			return opcodes;
		}
		XMLNode xml = XMLNode.parse(file);
		XGOpcode o = null;
		if(xml.getTag().equals(TAG_OPCODES))
		{	for(XMLNode n : xml.getChildNodes())
			{	if(n.getTag().equals(TAG_OPCODE))
				{	o = new XGOpcode(dev, n);
					opcodes.add(o);
				}
			}
		}
		log.info(opcodes.size() + " opcodes initialized from: " + file);
		return opcodes;
	}

	public static ValueDataClass getValueDataClass(String s)
	{	try
		{	return ValueDataClass.valueOf(s);
		}
		catch(IllegalArgumentException e)
		{	return DEF_VALUECLASS;
		}
	}

	public static ValueDataType getDataType(String s)
	{	try
		{	return ValueDataType.valueOf(s);
		}
		catch(IllegalArgumentException e)
		{	return DEF_DATATYPE;
		}
	}
/*
	public static XGOpcode getOpcode(XGAdress adr)
	{	if(OPCODES.containsKey(adr)) return OPCODES.get(adr);
		else return new XGOpcode(adr);
	}

	public static XGOpcode getOpcode(String tag)
	{	if(OPCODES.containsKey(tag)) return OPCODES.get(tag);
		return new XGOpcode(tag);
	}
*/
/*********************************************************************************************************/

	private final XGDevice device;
	private final String tag, dependencyTag;
	private final XGOpcodeConstants.ValueDataType dependencyType;
	private final XGAddress address, bulkAddress;
	private final int byteCount;
	private final ValueDataType dType;
	private final ValueDataClass vType;
	private final XGModuleTag moduleTag;
	private final Map<Integer, XGParameter> parameters;

	public XGOpcode(XGDevice dev, XGAddress adr)
	{	this.device = dev;
		this.tag = DEF_OPCODENAME + adr;
		this.address = adr;
		this.bulkAddress = adr;
		this.byteCount = DEF_BYTECOUNT;
		this.dType = DEF_DATATYPE;
		this.vType = DEF_VALUECLASS;
		this.moduleTag = XGModule.getModuleTag(adr);
		this.parameters = null;
		this.dependencyTag = "";
		this.dependencyType = ValueDataType.LSB;
	}

	private XGOpcode(XGDevice dev, String name)
	{	this.device = dev;
		this.tag = DEF_OPCODENAME + name;
		this.address = INVALIDADRESS;
		this.bulkAddress = INVALIDADRESS;
		this.byteCount = DEF_BYTECOUNT;
		this.dType = DEF_DATATYPE;
		this.vType = DEF_VALUECLASS;
		this.moduleTag = XGModuleTag.unknown;
		this.parameters = null;
		this.dependencyTag = "";
		this.dependencyType = ValueDataType.LSB;
	}

	private XGOpcode(XGDevice dev, XMLNode n)
	{	this.device = dev;
		this.address = new XGAddress(n);
		this.bulkAddress = new XGAddress(n.getParentNode());
		this.moduleTag = XGModuleTag.valueOf(n.getParentNode().getParentNode().getStringAttribute(ATTR_ID));
		this.tag = n.getStringAttribute(ATTR_ID);
		this.byteCount = n.getIntegerAttribute(ATTR_BYTECOUNT);
		this.dType = getDataType(n.getStringAttribute(ATTR_DATATYPE));
		this.vType = getValueDataClass(n.getStringAttribute(ATTR_VALUECLASS));
		this.dependencyTag = n.getStringAttribute(ATTR_DEPENDING);
		this.dependencyType = ValueDataType.valueOf(n.getStringAttribute(ATTR_DEP_TYPE));
		this.parameters = XGParameter.init(this, n);
		log.info("opcode initialized: " + this.getInfo());
	}

	public String getInfo()
	{	return this.tag + " " + this.address;
	}

	public ValueDataClass getValueClass()
	{	return this.vType;
	}

	public int getByteCount()
	{	return this.byteCount;
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

	public XGAddress getBulkAddress()
	{	return this.bulkAddress;
	}

	public XGModuleTag getModuleTag()
	{	return moduleTag;
	}

	public XGParameter getParameter(int i)
	{	if(this.parameters == null) return null;
		return this.parameters.get(i);
	}

	public Map<Integer, XGParameter> getParameters()
	{	return parameters;
	}
}
