package parm;

import java.util.HashMap;
import java.util.LinkedHashSet;
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
	private final boolean isMutable;

	public XGOpcode(XGDevice dev, XGBulkDump bulk, XMLNode n)//für init via xml, initialisiert für alle addresses ein XGValue
	{	this.device = dev;
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS).toString(), bulk.getAddress());
		this.dataType = ValueDataType.valueOf(n.getStringAttributeOrDefault(ATTR_DATATYPE, DEF_DATATYPE.name()));
		this.isMutable = MUTABLE.equals(n.getStringAttribute(ATTR_TYPE));

		if(this.isMutable)
		{	if(!n.hasAttribute(ATTR_PARAMETERSELECTOR)) throw new RuntimeException("opcode " + this.address + " is " + n.getStringAttribute(ATTR_TYPE) + " but has not declared " + ATTR_PARAMETERSELECTOR);

			this.parameterSelectorAddress = new XGAddress(n.getStringAttribute(ATTR_PARAMETERSELECTOR), null);
			for(XMLNode s : n.getChildNodes(TAG_PARAM))
			{
				int v = s.getValueAttribute(ATTR_VALUE, 0);
				XGParameter parm = dev.getParameters().get(s.getStringAttribute(ATTR_PARAMETER_ID));
				this.parameters.put(v, parm);
			}
		}
		else
		{	this.parameterSelectorAddress = null;
			this.parameters.put(DEF_SELECTORVALUE, dev.getParameters().get(n.getStringAttribute(ATTR_PARAMETER_ID)));
		}

//TODO: Krücke, XACTION_AFTER_EDIT="send" gehört normalerweise in die module.xml
		Set<String> set;
		if((set = this.actions.get(XACTION_AFTER_EDIT)) == null)
		{	set = new LinkedHashSet<>();
			this.actions.put(XACTION_AFTER_EDIT, set);
		}
		set.add("send");

		for(String s: XACTION)
		{	if(n.hasAttribute(s))
				this.actions.put(s, XGStrings.splitCSV(n.getStringAttribute(s).toString()));
		}
	}

	public boolean isMutable()
	{	return isMutable;
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

	public XGAddress getParameterSelectorAddress()
	{	return this.parameterSelectorAddress;
	}

	public XGDevice getDevice()
	{	return this.device;
	}

	public Map<String, Set<String>> getActions()
	{	return this.actions;
	}
}
