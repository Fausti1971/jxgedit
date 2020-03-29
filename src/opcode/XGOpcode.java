package opcode;

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

	public static XGTagableAddressableSet<XGOpcode> init(XMLNode x)
	{	
		XGTagableAddressableSet<XGOpcode> set = new XGTagableAddressableSet<>();
		XGOpcode o = null;
		for(XMLNode n : x.getChildNodes())
		{	if(n.getTag().equals(TAG_OPCODE))
			{	o = new XGOpcode(n);
				set.add(o);
			}
		}
		log.info(set.size() + " opcodes initialized");
		return set;
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

//	private final XGDevice device;
	private final String tag, dependencyTag;
	private final XGOpcodeConstants.ValueDataType dependencyType;
	private final XGAddress address, bulkAddress;
	private final int size;
	private final ValueDataType dType;
	private final ValueDataClass vType;
	private final XGModuleTag moduleTag;
	private final Map<Integer, XGParameter> parameters;

	public XGOpcode(XGDevice dev, XGAddress adr)
	{	this.tag = DEF_OPCODENAME + adr;
		this.address = adr;
		this.bulkAddress = adr;
		this.size = DEF_OPCODESIZE;
		this.dType = DEF_DATATYPE;
		this.vType = DEF_VALUECLASS;
		this.moduleTag = XGModule.getModuleTag(adr);
		this.parameters = null;
		this.dependencyTag = "";
		this.dependencyType = ValueDataType.LSB;
	}

	private XGOpcode(XGDevice dev, String name)
	{	this.tag = DEF_OPCODENAME + name;
		this.address = XGALLADDRESS;
		this.bulkAddress = XGALLADDRESS;
		this.size = DEF_OPCODESIZE;
		this.dType = DEF_DATATYPE;
		this.vType = DEF_VALUECLASS;
		this.moduleTag = XGModuleTag.unknown;
		this.parameters = null;
		this.dependencyTag = "";
		this.dependencyType = ValueDataType.LSB;
	}

	private XGOpcode(XMLNode n)
	{	this.address = new XGAddress(n);
		this.bulkAddress = new XGAddress(n.getParentNode());
		this.moduleTag = XGModuleTag.valueOf(n.getParentNode().getParentNode().getStringAttribute(ATTR_ID));
		this.tag = n.getStringAttribute(ATTR_ID);
		this.size = n.getIntegerAttribute(ATTR_SIZE);
		this.dType = getDataType(n.getStringAttribute(ATTR_DATATYPE));
		this.vType = getValueDataClass(n.getStringAttribute(ATTR_VALUECLASS));
		this.dependencyTag = n.getStringAttribute(ATTR_DEPENDING);
		this.dependencyType = ValueDataType.valueOf(n.getStringAttribute(ATTR_DEP_TYPE, DEF_DATATYPE.name()));
		this.parameters = XGParameter.init(this, n);
		log.info("opcode initialized: " + this.getInfo());
	}

	public String getInfo()
	{	return this.tag + " " + this.address;
	}

	public ValueDataClass getValueClass()
	{	return this.vType;
	}

	public int getSize()
	{	return this.size;
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
