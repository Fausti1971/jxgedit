package value;

import java.util.LinkedHashMap;import java.util.Map;import java.util.Vector;import java.util.function.Consumer;
import adress.*;
import application.XGStrings;import bulk.XGBulkType;import config.XGConfigurable;
import application.XGLoggable;
import msg.XGMessageCodec;
import tag.*;
import xml.XGProperty;import xml.XMLNode;

public class XGValueType implements XGLoggable, XGConfigurable, XGTagable
{
//	enum ValueAction{change,change_program,change_partmode,dump,none}
	static Map<String, Consumer<XGValue>> ACTIONS = new LinkedHashMap<>();
	static
	{	ACTIONS.put(ATTR_CHANGE_ACTION, XGValue::sendAction);
		ACTIONS.put(ATTR_CHANGE_PROGRAM_ACTION, XGProgramBuffer::bufferProgram);
		ACTIONS.put(ATTR_CHANGE_PARTMODE_ACTION, XGProgramBuffer::restoreProgram);
		ACTIONS.put(ATTR_DUMP_ACTION, XGValue::dumpAction);
		ACTIONS.put(ATTR_NONE_ACTION, XGValue::noneAction);
	}
	
//	static final SendAction DEF_ACTION = SendAction.none;

/**
* MSB=Most Significant Byte, 
* LSB=Least Significant Byte,
* MSN=Most Significant Nibble,
* LSN=Least Significant Nibble,
*/
	public enum ValueDataType{MSB, LSB, MSN, LSN}
	static final ValueDataType DEF_DATATYPE = ValueDataType.LSB;

	public static final String
		MP_PRG_VALUE_TAG = "mp_program",
		MP_PM_VALUE_TAG = "mp_partmode",
		DS_PRG_VALUE_TAG = "ds_program",
		ID_VALUE_TAG = ATTR_ID;

/*******************************************************************************************************************************/

	final XMLNode config;
	final XGBulkType bulkType;
	final int lo, size;
	final String tag, parameterSelectorTag, defaultSelectorTag;
	final String parameterTableName, defaultsTableName;
	final Vector<Consumer<XGValue>> actions = new Vector<>();
	final XGMessageCodec codec;


	public XGValueType(XGBulkType blk, XMLNode n)
	{	this.config = n;
		this.bulkType = blk;

		XGAddressField adr = new XGAddressRange(n.getStringAttribute(ATTR_ADDRESS)).getLo();
		this.lo = adr.getMin();
		this.size = adr.getSize();

		this.codec = XGMessageCodec.getCodec(ValueDataType.valueOf(n.getStringAttributeOrDefault(ATTR_DATATYPE, DEF_DATATYPE.name())));
		this.tag = n.getStringAttributeOrDefault(ATTR_ID, this.toString());
		this.parameterSelectorTag = n.getStringAttribute(ATTR_PARAMETERSELECTOR);
		this.parameterTableName = n.getStringAttribute(ATTR_PARAMETERTABLE);
		this.defaultSelectorTag = n.getStringAttribute(ATTR_DEFAULTSELECTOR);
		this.defaultsTableName = n.getStringAttribute(ATTR_DEFAULTSTABLE);
		for(String s : XGStrings.splitCSV(n.getStringAttribute(ATTR_ACTIONS)))
			this.actions.add(ACTIONS.getOrDefault(s, XGValue::noneAction));
	}

	public int getSize(){	return this.size;}

	public boolean hasMutableParameters(){	return this.parameterSelectorTag != null &&  this.parameterTableName != null;}

	public boolean hasMutableDefaults(){	return this.defaultSelectorTag != null && this.defaultsTableName != null;}

	@Override public XMLNode getConfig(){	return this.config;}

	@Override public void propertyChanged(XGProperty n){}

	@Override public String toString(){	return this.tag + " - " + this.lo;}

	@Override public String getTag(){	return this.tag;}
}
