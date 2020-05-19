package parm;

import java.io.File;
import java.io.FileNotFoundException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressable;
import adress.XGBulkDump;
import application.XGLoggable;
import device.XGDevice;
import module.XGModule;
import module.XGSuperModule;
import xml.XMLNode;

public class XGOpcode implements XGLoggable, XGAddressable, XGParameterConstants
{
	public static void init(XGDevice dev) throws InvalidXGAddressException
	{	
		File file;
		try
		{	file = dev.getResourceFile(XML_OPCODE);
		}
		catch(FileNotFoundException e)
		{	log.info(e.getMessage());
			return;
		}

		XMLNode xml = XMLNode.parse(file);
		for(XMLNode m : xml.getChildNodes(TAG_MODULE))
		{	XGModule mod = new XGSuperModule(dev, m);
			dev.getModules().add(mod);
			for(XMLNode b : m.getChildNodes(TAG_BULK))
			{	XGBulkDump blk = new XGBulkDump(mod, b);
//				dev.getBulks().add(blk);
				for(XMLNode o : b.getChildNodes(TAG_OPCODE))
				{	XGOpcode opc = new XGOpcode(dev, mod, blk, o);
					dev.getOpcodes().add(opc);
				}
			}
		}
		log.info(dev.getOpcodes().size() + " opcodes initialized");
		return;
	}

/*******************************************************************************************************************************/

	private final XGDevice device;
	private final XGModule module;
	private final XGBulkDump bulk;
	private final XGAddress address;
	private final ValueDataType dataType;
	private final int dataSize;
	private final String parameterID;

	protected XGOpcode(XGDevice dev, XGModule mod, XGBulkDump bulk, XMLNode n)//für init via xml, initialisiert für alle addresses ein XGValue
	{	this.device = dev;
		this.module = mod;
		this.bulk = bulk;
		XGAddress adr = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), bulk.getAddress());
		this.dataSize = adr.getLo().getSize();
		this.address = new XGAddress(adr.getHi(), adr.getMid(), new XGAddressField(adr.getLo().getMin()));
		this.dataType = ValueDataType.valueOf(n.getStringAttribute(ATTR_DATATYPE, DEF_DATATYPE.name()));
		this.parameterID = n.getStringAttribute(ATTR_PARAMETER_ID, "no id");
	}

	public ValueDataType getDataType()
	{	return this.dataType;
	}

	public int getDataSize()
	{	return this.dataSize;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public String getParameterID()
	{	return this.parameterID;
	}

	public XGBulkDump getBulk()
	{	return this.bulk;
	}

	public XGModule getModule()
	{	return this.module;
	}

	public XGDevice getDevice()
	{	return this.device;
	}
}
