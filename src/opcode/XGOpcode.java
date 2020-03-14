package opcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import adress.XGAddress;
import adress.XGAddressable;
import application.ConfigurationConstants;
import device.XGDevice;
import module.XGModule;
import module.XGModuleConstants.XGModuleTag;
import tag.XGTagable;
import tag.XGTagableAdressableSet;
import xml.XMLNode;

public class XGOpcode implements ConfigurationConstants, XGOpcodeConstants, XGAddressable, XGTagable
{	private static Logger log = Logger.getAnonymousLogger();

	public static XGTagableAdressableSet<XGOpcode> init(XGDevice dev)
	{	XGTagableAdressableSet<XGOpcode> opcodes = new XGTagableAdressableSet<>();
		File file;
		try
		{	file = dev.getResourceFile(XML_OPCODE);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
			return opcodes;
		}
		XMLNode xml = XMLNode.parse(file);
		if(xml.getTag().equals(TAG_OPCODE))
		{	for(XMLNode n : xml.getChildNodes())
				if(n.getTag().equals(TAG_ITEM)) opcodes.add(new XGOpcode(n));
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

	public static DataType getDataType(String s)
	{	try
		{	return DataType.valueOf(s);
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

	private final String tag;
	private final XGAddress adress;
	private final int byteCount;
	private final DataType dType;
	private final ValueDataClass vType;
	private final XGModuleTag module;

	public XGOpcode(XGAddress adr)
	{	this.tag = DEF_OPCODENAME + adr;
		this.adress = adr;
		this.byteCount = DEF_BYTECOUNT;
		this.dType = DEF_DATATYPE;
		this.vType = DEF_VALUECLASS;
		XGModuleTag temp = XGModuleTag.unknown;
		temp = XGModule.getModuleTag(adr);
		this.module = temp;
	}

	private XGOpcode(String name)
	{	this.tag = DEF_OPCODENAME + name;
		this.adress = null;
		this.byteCount = DEF_BYTECOUNT;
		this.dType = DEF_DATATYPE;
		this.vType = DEF_VALUECLASS;
		this.module = XGModuleTag.unknown;
	}

	public XGOpcode(XMLNode n)
	{	this.tag = n.getAttribute(ATTR_ID);
		this.adress = new XGAddress(n.getChildNode(TAG_ADRESS));
		this.byteCount = n.parseChildNodeIntegerContent(TAG_BYTECOUNT, DEF_BYTECOUNT);
		this.dType = getDataType(n.getChildNodeTextContent(TAG_DATATYPE, ""));
		this.vType = getValueDataClass(n.getChildNodeTextContent(TAG_VALUECLASS, ""));
		this.module = XGModule.getModuleTag(this.adress);
		log.info("opcode initialized: " + this.getInfo());
	}

	@Override public XGAddress getAdress()
	{	return this.adress;
	}

	public String getInfo()
	{	return this.tag + " " + this.adress;
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

	public DataType getDataType()
	{	return dType;
	}

	@Override public String toString()
	{	return this.getTag();
	}
}
