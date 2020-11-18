package parm;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import application.XGLoggable;
import application.XGStrings;
import device.XGDevice;
import module.XGModuleType;
import tag.XGCategorizeable;
import value.XGValue;
import xml.XMLNode;

public class XGOpcode implements XGLoggable, XGAddressable, XGParameterConstants, XGCategorizeable
{

/*******************************************************************************************************************************/

	private final XGModuleType moduleType;
	private final String category;
	private final XGAddress address, parameterSelectorAddress, defaultSelectorAddress;
	private final ValueDataType dataType;
	private final XGParameterTable parameters;
	private final XGDefaultsTable defaults;
	private final Map<String, Set<String>> actions = new HashMap<>();
	private final boolean isMutable, hasMutableDefaults;

//	public XGOpcode()
//	{	this.moduleType = null;
//		this.category = null;
//		this.address = null;
//		this.parameterSelectorAddress = null;
//		this.defaultSelectorAddress = null;
//		this.dataType = DEF_DATATYPE;
//		this.parameters = null;
//		this.defaults = null;
//		this.isMutable = false;
//		this.hasMutableDefaults = false;
//	}

	public XGOpcode(XGModuleType mod, XGAddress blk, XMLNode n) throws InvalidXGAddressException
	{	this.moduleType = mod;
		XGDevice dev = mod.getDevice();
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS).toString()).complement(blk.getAddress());
		this.dataType = ValueDataType.valueOf(n.getStringAttributeOrDefault(ATTR_DATATYPE, DEF_DATATYPE.name()));
		this.category = n.getStringAttributeOrDefault(ATTR_CATEGORY, DEF_CATEGORY);

		this.isMutable = MUTABLE.equals(n.getStringAttribute(ATTR_TYPE));
		if(this.isMutable)
		{	if(!n.hasAttribute(ATTR_PARAMETERSELECTOR)) throw new RuntimeException("opcode " + this.address + " is " + n.getStringAttribute(ATTR_TYPE) + " but has not declared " + ATTR_PARAMETERSELECTOR);
			if(!n.hasAttribute(ATTR_PARAMETERS)) throw new RuntimeException("opcode " + this.address + " is " + n.getStringAttribute(ATTR_TYPE) + " but has not declared " + ATTR_PARAMETERS);

			this.parameterSelectorAddress = new XGAddress(n.getStringAttribute(ATTR_PARAMETERSELECTOR));
			String parTabName = n.getStringAttribute(ATTR_PARAMETERS);
			this.parameters = dev.getParameterTables().get(parTabName);
			if(this.parameters == null) throw new RuntimeException(ATTR_PARAMETERS + " " + parTabName + " not found!");
		}
		else
		{	this.parameterSelectorAddress = null;
			this.parameters = new XGParameterTable(dev);
			this.parameters.put(DEF_SELECTORVALUE, new XGParameter(dev, n));
		}

		this.hasMutableDefaults = n.hasAttribute(ATTR_DEFAULTS) && n.hasAttribute(ATTR_DEFAULTSELECTOR);
		if(this.hasMutableDefaults)
		{	this.defaultSelectorAddress = new XGAddress(n.getStringAttribute(ATTR_DEFAULTSELECTOR));
			String defTabName = n.getStringAttribute(ATTR_DEFAULTS);
			this.defaults = dev.getDefaultsTables().get(defTabName);
			if(this.defaults == null) throw new RuntimeException(ATTR_DEFAULTS + " " + defTabName + " not found!");
		}
		else
		{	this.defaultSelectorAddress = null;
			this.defaults = new XGDefaultsTable(n);
			this.defaults.put(DEF_SELECTORVALUE, n.getValueAttribute(ATTR_DEFAULT, 0));
		}


//TODO: Krücke, XACTION_AFTER_EDIT="send" gehört normalerweise in die structure.xml
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
//		LOG.info(this + " initialized");
	}

	public boolean isMutable()
	{	return isMutable;
	}

	public boolean hasMutableDefaults()
	{	return this.hasMutableDefaults;
	}

	public ValueDataType getDataType()
	{	return this.dataType;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public XGAddress getParameterSelectorAddress()
	{	return this.parameterSelectorAddress;
	}

	public XGAddress getDefaultSelectorAddress()
	{	return this.defaultSelectorAddress;
	}

	public XGParameter getParameter(XGValue selector)
	{	if(this.isMutable) return this.parameters.getOrDefault(selector.getValue(), NO_PARAMETER);
		else return this.parameters.get(DEF_SELECTORVALUE);
	}

	public int getDefaultValue(XGValue selector)
	{	int val = selector.getValue();
		if(this.hasMutableDefaults && this.defaults.containsKey(val)) return this.defaults.get(val);
		else return this.defaults.getOrDefault(DEF_SELECTORVALUE, 0);
	}

	public XGDevice getDevice()
	{	return this.moduleType.getDevice();
	}

	public Map<String, Set<String>> getActions()
	{	return this.actions;
	}

	@Override public String toString()
	{	return this.getClass().getSimpleName() + this.address;
	}

	@Override public String getCategory()
	{	return this.category;
	}
}
