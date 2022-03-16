package value;

import java.util.LinkedHashSet;
import java.util.Set;
import adress.*;
import config.XGConfigurable;
import application.XGLoggable;
import application.XGStrings;
import parm.XGParameterConstants;import tag.*;
import xml.XGProperty;import xml.XMLNode;

public class XGValueType implements XGLoggable, XGAddressable, XGParameterConstants, XGConfigurable, XGTagable
{
//	public static final XGTagableAddressableSet<XGOpcode> OPCODES = new XGTagableAddressableSet<XGOpcode>();//Prototypen
	public static final String
		MP_PRG_VALUE_TAG = "mp_program",
		MP_PM_VALUE_TAG = "mp_partmode";

/*******************************************************************************************************************************/

	private final XMLNode config;
	private final String id, parameterSelectorTag, defaultSelectorTag;
	private final XGAddress address;
	private final ValueDataType dataType;
	private final String parameterTableName, defaultsTableName;
	private final Set<XACTION> actions = new LinkedHashSet<>();

	public XGValueType(XGAddress blk, XMLNode n) throws InvalidXGAddressException
	{	this.config = n;
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS)).complement(blk.getAddress());
		this.dataType = ValueDataType.valueOf(n.getStringAttributeOrDefault(ATTR_DATATYPE, DEF_DATATYPE.name()));
		this.id = n.getStringAttributeOrDefault(ATTR_ID, this.toString());
		this.parameterSelectorTag = n.getStringAttribute(ATTR_PARAMETERSELECTOR);
		this.parameterTableName = n.getStringAttribute(ATTR_PARAMETERTABLE);
		this.defaultSelectorTag = n.getStringAttribute(ATTR_DEFAULTSELECTOR);
		this.defaultsTableName = n.getStringAttribute(ATTR_DEFAULTSTABLE);

		for(String s: XGStrings.splitCSV(n.getStringAttribute(ATTR_ACTIONS))) this.actions.add(XACTION.valueOf(s));
	}

	public boolean hasMutableParameters(){	return this.parameterSelectorTag != null &&  this.parameterTableName != null;}

	public boolean hasMutableDefaults(){	return this.defaultSelectorTag != null && this.defaultsTableName != null;}

	public Set<XACTION> getActions(){	return this.actions;}

	@Override public XMLNode getConfig(){	return this.config;}

	@Override public void propertyChanged(XGProperty n){}

	public ValueDataType getDataType(){	return this.dataType;}

	@Override public XGAddress getAddress(){	return this.address;}

	public String getParameterSelectorTag(){	return this.parameterSelectorTag;}

	public String getParameterTableName(){	return this.parameterTableName;}

	public String getDefaultSelectorTag(){	return this.defaultSelectorTag;}

	public String getDefaultsTableName(){	return this.defaultsTableName;}

	@Override public String toString(){	return this.getClass().getSimpleName() + this.address;}

	public String getTag(){	return this.id;}
}
