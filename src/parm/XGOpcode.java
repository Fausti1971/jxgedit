package parm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import adress.XGAddress;
import adress.XGAddressable;
import application.XGLoggable;
import application.XGStrings;
import device.XGDevice;
import msg.XGBulkDump;
import xml.XMLNode;

public class XGOpcode implements XGLoggable, XGAddressable, XGParameterConstants
{

/*******************************************************************************************************************************/

	private final XGDevice device;
	private final XGAddress address, parameterSelectorAddress;
	private final ValueDataType dataType;
	private final Map<Integer, XGParameter> parameters = new HashMap<>();
	private final Map<String, Set<String>> actions = new HashMap<>();

	public XGOpcode(XGDevice dev, XGBulkDump bulk, XMLNode n)//für init via xml, initialisiert für alle addresses ein XGValue
	{	this.device = dev;
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS).toString(), bulk.getAddress());
		this.dataType = ValueDataType.valueOf(n.getStringAttributeOrDefault(ATTR_DATATYPE, DEF_DATATYPE.name()).toString());

		if(n.hasAttribute(ATTR_PARAMETERSELECTOR))
		{	this.parameterSelectorAddress = new XGAddress(n.getStringAttribute(ATTR_PARAMETERSELECTOR).toString(), null);
			for(XMLNode s : n.getChildNodes(TAG_PARAM))
			{	int msb = s.getIntegerAttribute(ATTR_MSB, 0)  << 7;
				int lsb = s.getIntegerAttribute(ATTR_LSB, 0);
				int v = msb | lsb;
				if(s.hasAttribute(ATTR_ADDRESS)) v = s.getIntegerAttribute(ATTR_ADDRESS);
				XGParameter parm = dev.getParameters().get(s.getStringAttribute(ATTR_PARAMETER_ID).toString());
				this.parameters.put(v, parm);
			}
		}
		else
		{	this.parameterSelectorAddress = null;
			this.parameters.put(DEF_SELECTORVALUE, dev.getParameters().get(n.getStringAttribute(ATTR_PARAMETER_ID).toString()));
		}
		for(String s: XACTION)
		{	if(n.hasAttribute(s))
				this.actions.put(s, XGStrings.splitCSV(n.getStringAttribute(s).toString()));
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

	public Map<String, Set<String>> getActions()
	{	return this.actions;
	}
}
