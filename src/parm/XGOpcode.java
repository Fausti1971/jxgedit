package parm;

import java.util.HashMap;
import java.util.Map;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGBulkDump;
import application.XGLoggable;
import device.XGDevice;
import xml.XMLNode;

public class XGOpcode implements XGLoggable, XGAddressable, XGParameterConstants
{
//	public static void init(XGDevice dev) throws InvalidXGAddressException
//	{	
//		File file;
//		try
//		{	file = dev.getResourceFile(XML_OPCODE);
//		}
//		catch(FileNotFoundException e)
//		{	log.info(e.getMessage());
//			return;
//		}
//
//		XMLNode xml = XMLNode.parse(file);
//		for(XMLNode m : xml.getChildNodes(TAG_MODULE))
//		{	XGModule mod = new XGSuperModule(dev, m);
//			dev.getModules().add(mod);
//			for(XMLNode b : m.getChildNodes(TAG_BULK))
//			{	XGBulkDump blk = new XGBulkDump(mod, b);
////				dev.getBulks().add(blk);
//				for(XMLNode o : b.getChildNodes(TAG_OPCODE))
//				{	XGOpcode opc = new XGOpcode(dev, mod, blk, o);
//					dev.getOpcodes().add(opc);
//				}
//			}
//		}
//		log.info(dev.getOpcodes().size() + " opcodes initialized");
//		return;
//	}

/*******************************************************************************************************************************/

	private final XGDevice device;
	private final XGAddress address, parameterSelectorAddress;
	private final ValueDataType dataType;
	private final Map<Integer, XGParameter> parameters = new HashMap<>();

	public XGOpcode(XGDevice dev, XGBulkDump bulk, XMLNode n)//für init via xml, initialisiert für alle addresses ein XGValue
	{	this.device = dev;
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), bulk.getAddress());
		this.dataType = ValueDataType.valueOf(n.getStringAttribute(ATTR_DATATYPE, DEF_DATATYPE.name()));

		if(n.hasAttribute(ATTR_PARAMETERSELECTOR))
		{	this.parameterSelectorAddress = new XGAddress(n.getStringAttribute(ATTR_PARAMETERSELECTOR), null);
			for(XMLNode s : n.getChildNodes(TAG_PARAM))
			{	int msb = s.getIntegerAttribute(ATTR_MSB, 0)  << 7;
				int lsb = s.getIntegerAttribute(ATTR_LSB, 0);
				int v = msb | lsb;
				if(s.hasAttribute(ATTR_VALUE)) v = s.getIntegerAttribute(ATTR_VALUE);
				XGParameter parm = dev.getParameters().get(s.getStringAttribute(ATTR_PARAMETER_ID));
				this.parameters.put(v, parm);
			}
		}
		else
		{	this.parameterSelectorAddress = null;
			this.parameters.put(DEF_SELECTORVALUE, dev.getParameters().get(n.getStringAttribute(ATTR_PARAMETER_ID)));
		}
	}

	public ValueDataType getDataType()
	{	return this.dataType;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public Map<Integer, XGParameter> getParameters()
	{	return this.parameters;
	}

//	public String getParameterID()
//	{	return this.parameterID;
//	}

	public XGAddress getParameterSelectorAddress()
	{	return this.parameterSelectorAddress;
	}

//	public XGBulkDump getBulk()
//	{	return this.bulk;
//	}
//
//	public XGModule getModule()
//	{	return this.module;
//	}

	public XGDevice getDevice()
	{	return this.device;
	}
}
