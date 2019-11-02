package opcode;

import java.io.File;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import adress.XGAdress;
import adress.XGAdressable;
import application.ConfigurationConstants;
import device.XGDevice;
import tag.XGTagable;
import tag.XGTagdressableSet;
import xml.XMLNode;

public class XGOpcode implements ConfigurationConstants, XGOpcodeConstants, XGAdressable, XGTagable
{	private static Logger log = Logger.getAnonymousLogger();

	private static final XGTagdressableSet<XGOpcode> OPCODES = new XGTagdressableSet<>();

	public static void init(XGDevice dev)
	{	File file = dev.getResourceFile(XML_OPCODE);
		try
		{	XMLNode xml = XMLNode.parse(file);
			for(XMLNode n : xml.getChildren())
				if(n.getTag().equals(TAG_OPCODE))
					OPCODES.add(new XGOpcode(n));
		}
		catch(XMLStreamException e)
		{	e.printStackTrace();
		}
		log.info(OPCODES.size() + " opcodes initialized from: " + file);
	}

	public static XGOpcode getOpcode(XGAdress adr) throws NoSuchOpcodeException
	{	if(OPCODES.containsKey(adr)) return OPCODES.get(adr);
		else throw new NoSuchOpcodeException(adr.toString());
	}

	public static XGOpcode getOpcode(String tag) throws NoSuchOpcodeException
	{	if(OPCODES.containsKey(tag)) return OPCODES.get(tag);
		else throw new NoSuchOpcodeException(tag);
	}

/*********************************************************************************************************/

	private final String name;
	private final XGAdress adress;
	private final int byteCount;
	private final DataType dType;
	private final ValueDataClass vType;


	public XGOpcode(XMLNode n)
	{	String s;

		this.name = n.getChildNode(TAG_NAME).getTextContent();

		this.adress = new XGAdress(n.getChildNode(TAG_ADRESS));

		this.byteCount = n.parseChildNodeTextContent(TAG_BYTECOUNT, DEF_BYTECOUNT);

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
