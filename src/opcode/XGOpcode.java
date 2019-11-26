package opcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import adress.XGAdress;
import adress.XGAdressable;
import application.ConfigurationConstants;
import device.XGDevice;
import tag.XGTagable;
import tag.XGTagdressableSet;
import xml.XMLNode;

public class XGOpcode implements ConfigurationConstants, XGOpcodeConstants, XGAdressable, XGTagable
{	private static Logger log = Logger.getAnonymousLogger();

	public static XGTagdressableSet<XGOpcode> init(XGDevice dev)
	{	XGTagdressableSet<XGOpcode> opcodes = new XGTagdressableSet<>();
		File file;
		try
		{	file = dev.getResourceFile(XML_OPCODE);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
			return opcodes;
		}
		XMLNode xml = XMLNode.parse(file);
			for(XMLNode n : xml.getChildren())
				if(n.getTag().equals(TAG_OPCODE))
					opcodes.add(new XGOpcode(n));
		log.info(opcodes.size() + " opcodes initialized from: " + file);
		return opcodes;
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

	private final String name;
	private final XGAdress adress;
	private final int byteCount;
	private final DataType dType;
	private final ValueDataClass vType;

	public XGOpcode(XGAdress adr)
	{	this.name = DEF_OPCODENAME + adr;
		this.adress = adr;
		this.byteCount = DEF_BYTECOUNT;
		this.dType = DEF_DATATYPE;
		this.vType = DEF_VALUECLASS;
	}

	private XGOpcode(String name)
	{	this.name = DEF_OPCODENAME + name;
		this.adress = null;
		this.byteCount = DEF_BYTECOUNT;
		this.dType = DEF_DATATYPE;
		this.vType = DEF_VALUECLASS;
	}

	public XGOpcode(XMLNode n)
	{	String s;

		this.name = n.getChildNode(TAG_NAME).getTextContent();

		this.adress = new XGAdress(n.getChildNode(TAG_ADRESS));

		this.byteCount = n.parseChildNodeIntegerContent(TAG_BYTECOUNT, DEF_BYTECOUNT);

		s = n.getChildNodeTextContent(TAG_DATATYPE, "");
		DataType t;
		try
		{	t = DataType.valueOf(s);
		}
		catch(IllegalArgumentException e)
		{	t = DEF_DATATYPE;
		}
		this.dType = t;

		s = n.getChildNodeTextContent(TAG_VALUECLASS, "");
		ValueDataClass c;
		try
		{	c = ValueDataClass.valueOf(s);
		}
		catch(IllegalArgumentException e)
		{	c = DEF_VALUECLASS;
		}
		this.vType = c;

		log.info("opcode initialized: " + this);
	}

	public XGAdress getAdress()
	{	return this.adress;
	}

	public String getInfo()
	{	return this.name;
	}

	public ValueDataClass getValueClass()
	{	return this.vType;
	}

	public int getByteCount()
	{	return this.byteCount;
	}

	public String getTag()
	{	return this.name;
	}

	public DataType getDataType()
	{	return dType;
	}

	@Override public String toString()
	{	return this.getTag() + this.getAdress();
	}
}
