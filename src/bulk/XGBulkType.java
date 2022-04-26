package bulk;

import adress.InvalidXGAddressException;import adress.XGAddressRange;
import static application.XGLoggable.LOG;import config.XGConfigurable;import module.XGModuleType;import tag.XGTagableSet;import value.XGValueType;import tag.XGTagable;import xml.XGProperty;import xml.XMLNode;

public class XGBulkType implements XGTagable, XGConfigurable
{
	final XMLNode config;
	final XGAddressRange addressRange;
	final String tag;
	final XGModuleType moduleType;
	final XGTagableSet<XGValueType> valueTypes = new XGTagableSet<>();

	public XGBulkType(XGModuleType mt, XMLNode x)throws InvalidXGAddressException
	{	this.config = x;
		this.tag = x.getStringAttribute(ATTR_ID);
		this.addressRange = new XGAddressRange(x.getStringAttribute(ATTR_ADDRESS), mt.getAddressRange());
		this.moduleType = mt;

		for(XMLNode o : x.getChildNodes(TAG_OPCODE))
		{		this.valueTypes.add(new XGValueType(this, o));
		}
	}

	public XGTagableSet<XGValueType> getValueTypes(){	return this.valueTypes;}

	public String getTag(){	return this.tag;}

	public XMLNode getConfig(){	return this.config;}

	public void propertyChanged(XGProperty p){}
}