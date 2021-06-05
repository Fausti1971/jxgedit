package parm;

import java.util.LinkedHashSet;
import java.util.Set;
import adress.*;
import config.Configurable;
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
	private final XGAddress address;
	private final ValueDataType dataType;
	private final String parameterTableName, defaultsTableName;
	private final Set<XACTION> actions = new LinkedHashSet<>();
	private final boolean isMutable, hasMutableDefaults;

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

			this.parameterSelectorTag = n.getStringAttribute(ATTR_PARAMETERSELECTOR);
			this.parameterTableName = n.getStringAttribute(ATTR_PARAMETERS);
		}
		else
		{	this.parameterSelectorTag = null;
			this.parameterTableName = null;
		}

		this.hasMutableDefaults = n.hasAttribute(ATTR_DEFAULTS);// && n.hasAttribute(ATTR_DEFAULTSELECTOR);
		if(this.hasMutableDefaults)
		{	this.defaultSelectorTag = n.getStringAttribute(ATTR_DEFAULTSELECTOR);
			this.defaultsTableName = n.getStringAttribute(ATTR_DEFAULTS);
		}
		else
		{	this.defaultSelectorTag = null;
			this.defaultsTableName = null;
		}

		for(String s: XGStrings.splitCSV(n.getStringAttribute(ATTR_ACTIONS))) this.actions.add(XACTION.valueOf(s));
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

	@Override public void configurationChanged(XMLNode n)
	{
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

	public String getParameterTableName()
	{	return this.parameterTableName;
	}

	public String getDefaultSelectorTag()
	{	return this.defaultSelectorTag;
	}

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
