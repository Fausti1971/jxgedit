package opcode;

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
import tag.XGTagableAddressableSet;
import xml.XMLNode;

public class XGOpcode implements ConfigurationConstants, XGOpcodeConstants, XGTagable, XGAddressable
{	private static Logger log = Logger.getAnonymousLogger();

	public static XGTagableAddressableSet<XGOpcode> init(XGBulkDump bulk, XMLNode x)
	{	
		XGTagableAddressableSet<XGOpcode> set = new XGTagableAddressableSet<>();
		XGOpcode o = null;
		for(XMLNode n : x.getChildNodes())
		{	if(n.getTag().equals(TAG_OPCODE))
			{	o = new XGOpcode(bulk, n);
				set.add(o);
			}
		}
		log.info(set.size() + " opcodes initialized");
		return set;
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
	private final XGBulkDump bulk;
	private final XGAddress address;
	private final ValueDataType dType;
	private final ValueDataClass vType;
	private final XGModuleTag moduleTag;
	private final Map<Integer, XGParameter> parameters;

	public XGOpcode(XGDevice dev, XGAddress adr)
	{	this.tag = DEF_OPCODENAME + adr;
		this.address = adr;
		this.bulk = null;
		this.dType = DEF_DATATYPE;
		this.vType = DEF_VALUECLASS;
		this.moduleTag = XGModule.getModuleTag(adr);
		this.parameters = null;
		this.dependencyTag = "";
		this.dependencyType = ValueDataType.LSB;
	}

	private XGOpcode(XGBulkDump bulk, XMLNode n)
	{	this.bulk = bulk;
		this.address = new XGAddress(bulk.getAddress(), n);
		this.moduleTag = (XGModuleTag)this.bulk.getModule().getTag();
		this.tag = n.getStringAttribute(ATTR_ID);
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

	public XGBulkDump getBulk()
	{	return this.bulk;
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
