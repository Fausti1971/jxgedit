package parm;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import adress.*;
import application.Configurable;
import application.XGLoggable;
import application.XGStrings;
import module.XGModuleType;
import tag.*;
import xml.XMLNode;

public class XGOpcode implements XGLoggable, XGAddressable, XGParameterConstants, XGCategorizeable, Configurable, XGTagable
{

//	public static final XGTagableAddressableSet<XGOpcode> OPCODES = new XGTagableAddressableSet<XGOpcode>();//Prototypen


/*******************************************************************************************************************************/

	private final XMLNode config;
	private final XGModuleType moduleType;
	private final String category, id, parameterSelectorTag, defaultSelectorTag;
	private final XGAddress address;// parameterSelectorAddress, defaultSelectorAddress;
	private final ValueDataType dataType;
	private final String parameterTableName, defaultsTableName;
//	private final XGParameterTable parameters;
//	private final XGDefaultsTable defaults;
	private final Set<XACTION> actions = new LinkedHashSet<>();
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
	{	this.config = n;
		this.moduleType = mod;
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS).toString()).complement(blk.getAddress());
		this.dataType = ValueDataType.valueOf(n.getStringAttributeOrDefault(ATTR_DATATYPE, DEF_DATATYPE.name()));
		this.category = n.getStringAttributeOrDefault(ATTR_CATEGORY, DEF_CATEGORY);
		this.id = n.getStringAttributeOrDefault(ATTR_ID, this.toString());

		this.isMutable = MUTABLE.equals(n.getStringAttribute(ATTR_TYPE));
		if(this.isMutable)
		{	if(!n.hasAttribute(ATTR_PARAMETERSELECTOR)) throw new RuntimeException("opcode " + this.address + " is " + n.getStringAttribute(ATTR_TYPE) + " but has not declared " + ATTR_PARAMETERSELECTOR);
			if(!n.hasAttribute(ATTR_PARAMETERS)) throw new RuntimeException("opcode " + this.address + " is " + n.getStringAttribute(ATTR_TYPE) + " but has not declared " + ATTR_PARAMETERS);

//			this.parameterSelectorAddress = new XGAddress(n.getStringAttribute(ATTR_PARAMETERSELECTOR));
			this.parameterSelectorTag = n.getStringAttribute(ATTR_PARAMETERSELECTOR);
			this.parameterTableName = n.getStringAttribute(ATTR_PARAMETERS);
//			this.parameters = dev.getParameterTables().get(parTabName);
//			if(this.parameters == null) throw new RuntimeException(ATTR_PARAMETERS + " " + parTabName + " not found!");
		}
		else
		{	//this.parameterSelectorAddress = null;
			this.parameterSelectorTag = null;
			this.parameterTableName = null;
//			this.parameters = new XGParameterTable(dev);
//			this.parameters.put(DEF_SELECTORVALUE, new XGParameter(dev, n));
		}

		this.hasMutableDefaults = n.hasAttribute(ATTR_DEFAULTS);// && n.hasAttribute(ATTR_DEFAULTSELECTOR);
		if(this.hasMutableDefaults)
		{	//this.defaultSelectorAddress = new XGAddress(n.getStringAttribute(ATTR_DEFAULTSELECTOR));
			this.defaultSelectorTag = n.getStringAttribute(ATTR_DEFAULTSELECTOR);
			this.defaultsTableName = n.getStringAttribute(ATTR_DEFAULTS);
//			this.defaults = dev.getDefaultsTables().get(defTabName);
//			if(this.defaults == null) throw new RuntimeException(ATTR_DEFAULTS + " " + defTabName + " not found!");
		}
		else
		{	//this.defaultSelectorAddress = null;
			this.defaultSelectorTag = null;
			this.defaultsTableName = null;
//			this.defaults = new XGDefaultsTable(n);
//			this.defaults.put(DEF_SELECTORVALUE, n.getValueAttribute(ATTR_DEFAULT, 0));
		}

		for(String s: XGStrings.splitCSV(n.getStringAttribute(ATTR_ACTIONS))) this.actions.add(XACTION.valueOf(s));
//		LOG.info(this + " initialized");
	}

	public boolean isMutable()
	{	return this.isMutable;
	}

	public boolean hasMutableDefaults()
	{	return this.hasMutableDefaults;
	}

	public Set<XACTION> getActions()
	{	return this.actions;
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	public ValueDataType getDataType()
	{	return this.dataType;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public String getParameterSelectorTag()
	{	return this.parameterSelectorTag;
	}

	//public XGAddress getParameterSelectorAddress()
	//{	return this.parameterSelectorAddress;
	//}

	public String getParameterTableName()
	{	return this.parameterTableName;
	}

	public String getDefaultSelectorTag()
	{	return this.defaultSelectorTag;
	}

	//public XGAddress getDefaultSelectorAddress()
	//{	return this.defaultSelectorAddress;
	//}

	public String getDefaultsTableName()
	{	return this.defaultsTableName;
	}

	@Override public String toString()
	{	return this.getClass().getSimpleName() + this.address;
	}

	@Override public String getCategory()
	{	return this.category;
	}

	public String getTag()
	{	return this.id;
	}

	public XGModuleType getModuleType()
	{	return moduleType;
	}
}
