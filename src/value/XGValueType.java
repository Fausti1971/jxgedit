package value;

import java.util.function.Consumer;
import adress.*;
import config.XGConfigurable;
import application.XGLoggable;
import msg.XGMessageCodec;
import tag.*;
import xml.XGProperty;import xml.XMLNode;

public class XGValueType implements XGLoggable, XGAddressable, XGConfigurable, XGTagable
{
	enum SendAction{change,dump,none}
//	static final SendAction DEF_ACTION = SendAction.none;

	public enum ValueDataType{	MSB, LSB, MSN, LSN}
	static final ValueDataType DEF_DATATYPE = ValueDataType.LSB;

	public static final String
		MP_PRG_VALUE_TAG = "mp_program",
		MP_PM_VALUE_TAG = "mp_partmode";

/*******************************************************************************************************************************/

	final XMLNode config;
	final String tag, parameterSelectorTag, defaultSelectorTag;
	final XGAddress address;
	final String parameterTableName, defaultsTableName;
	final Consumer<XGValue> action;
	final XGMessageCodec codec;


	public XGValueType(XGAddress blk, XMLNode n) throws InvalidXGAddressException
	{	this.config = n;
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS)).complement(blk.getAddress());
		this.codec = XGMessageCodec.getCodec(ValueDataType.valueOf(n.getStringAttributeOrDefault(ATTR_DATATYPE, DEF_DATATYPE.name())));
		this.tag = n.getStringAttributeOrDefault(ATTR_ID, this.toString());
		this.parameterSelectorTag = n.getStringAttribute(ATTR_PARAMETERSELECTOR);
		this.parameterTableName = n.getStringAttribute(ATTR_PARAMETERTABLE);
		this.defaultSelectorTag = n.getStringAttribute(ATTR_DEFAULTSELECTOR);
		this.defaultsTableName = n.getStringAttribute(ATTR_DEFAULTSTABLE);
		switch(SendAction.valueOf(n.getStringAttribute(ATTR_ACTIONS)))
		{	case change:	this.action = XGValue::sendAction; break;
			case dump:		this.action = XGValue::dumpAction; break;
			case none:
			default:		this.action = XGValue::noneAction; break;
		}
	}

	public boolean hasMutableParameters(){	return this.parameterSelectorTag != null &&  this.parameterTableName != null;}

	public boolean hasMutableDefaults(){	return this.defaultSelectorTag != null && this.defaultsTableName != null;}

	@Override public XMLNode getConfig(){	return this.config;}

	@Override public void propertyChanged(XGProperty n){}

	@Override public XGAddress getAddress(){	return this.address;}

	@Override public String toString(){	return this.getClass().getSimpleName() + this.address;}

	@Override public String getTag(){	return this.tag;}
}
