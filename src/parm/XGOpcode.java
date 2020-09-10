package parm;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.XGLoggable;
import application.XGStrings;
import device.XGDevice;
import msg.XGBulkDump;
import xml.XMLNode;

public class XGOpcode implements XGLoggable, XGAddressable, XGParameterConstants
{
	public static XGAddressableSet<XGOpcode> init(XGBulkDump blk, XMLNode xml)
	{
		XGAddressableSet<XGOpcode> set = new XGAddressableSet<>();
		for(XMLNode n : xml.getChildNodes(TAG_OPCODE))
			set.add(new XGOpcode(blk, n));
		LOG.info(set.size() + " opcodes initialized for " + blk);
		return set;
	}

/*******************************************************************************************************************************/

	private final XGAddress address, parameterSelectorAddress, defaultSelectorAddress;
	private final ValueDataType dataType;
	private final Map<Integer, XGParameter> parameters = new HashMap<>();
	private final Map<Integer, Integer> defaults;
	private final Map<String, Set<String>> actions = new HashMap<>();
	private final boolean isMutable, hasMutableDefaults;
	private final XGBulkDump bulk;

	public XGOpcode(XGBulkDump blk, XMLNode n)//für init via xml, initialisiert für alle addresses ein XGValue
	{	XGDevice dev = blk.getModule().getDevice();
		this.bulk = blk;
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS).toString(), null);
		this.dataType = ValueDataType.valueOf(n.getStringAttributeOrDefault(ATTR_DATATYPE, DEF_DATATYPE.name()));
		this.isMutable = MUTABLE.equals(n.getStringAttribute(ATTR_TYPE));

		if(this.isMutable)
		{	if(!n.hasAttribute(ATTR_PARAMETERSELECTOR)) throw new RuntimeException("opcode " + this.address + " is " + n.getStringAttribute(ATTR_TYPE) + " but has not declared " + ATTR_PARAMETERSELECTOR);
			if(!n.hasAttribute(ATTR_PARAMETERS)) throw new RuntimeException("opcode " + this.address + " is " + n.getStringAttribute(ATTR_TYPE) + " but has not declared " + ATTR_PARAMETERS);

			this.parameterSelectorAddress = new XGAddress(n.getStringAttribute(ATTR_PARAMETERSELECTOR));
			String tabName = n.getStringAttribute(ATTR_PARAMETERS);
			XMLNode t = dev.getParameterTables().getChildNodeWithName(TAG_PARAMETERTABLE, tabName);
			for(XMLNode p : t.getChildNodes(TAG_PARAMETER))
			{
				int v = p.getValueAttribute(ATTR_VALUE, DEF_SELECTORVALUE);
				XGParameter parm = new XGParameter(dev, p);
				this.parameters.put(v, parm);
			}
		}
		else
		{	this.parameterSelectorAddress = null;
			this.parameters.put(DEF_SELECTORVALUE, new XGParameter(dev, n));
		}

		this.hasMutableDefaults = n.hasAttribute(ATTR_DEFAULTS) && n.hasAttribute(ATTR_DEFAULTSELECTOR);
		if(this.hasMutableDefaults)
		{	this.defaultSelectorAddress = new XGAddress(n.getStringAttribute(ATTR_DEFAULTSELECTOR));
		}
		else this.defaultSelectorAddress = null;
		this.defaults = XGDefaults.getDefaultsTable(dev, n);


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

	public XGAddress getDefaultSelectorAddress()
	{	return this.defaultSelectorAddress;
	}

	public Map<Integer, Integer> getDefaults()
	{	return this.defaults;
	}

	public boolean hasMutableDefaults()
	{	return this.hasMutableDefaults;
	}

	public XGDevice getDevice()
	{	return this.bulk.getModule().getDevice();
	}

	public Map<String, Set<String>> getActions()
	{	return this.actions;
	}
}
