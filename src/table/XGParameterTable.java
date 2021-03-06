package table;

import java.io.*;
import java.util.HashMap;
import application.*;
import parm.XGParameter;
import parm.XGParameterConstants;
import tag.*;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGParameterTable extends HashMap<Integer, XGParameter> implements XGTagable, XMLNodeConstants, XGLoggable, XGParameterConstants
{
	public static final XGTagableSet<XGParameterTable> PARAMETERTABLES = new XGTagableSet<>();
	private static final long serialVersionUID = 1L;

	public static void init()
	{	try
		{	XMLNode n = XMLNode.parse(XML_PARAMETER);
			for(XMLNode t : n.getChildNodes(TAG_PARAMETERTABLE))
				PARAMETERTABLES.add(new XGParameterTable(t));
			
		}
		catch(IOException e)
		{	LOG.info(e.getMessage());
		}
	}

/*****************************************************************************************/

	private final String name;

	public XGParameterTable(XMLNode n)
	{	this.name = n.getStringAttribute(ATTR_NAME);
		for(XMLNode pn : n.getChildNodes(TAG_PARAMETER))
		{	XGParameter p = new XGParameter(pn);
			int index = pn.getValueAttribute(ATTR_SELECTORVALUE, DEF_SELECTORVALUE);
			this.put(index, p);
		}
		LOG.info(this.getClass().getSimpleName() + " " + this.name + " initialized");
	}

	public XGParameterTable(String name){	this.name = name;}//Dummy-Parameter-Table für immutable Opcodes

	@Override public String getTag(){	return name;}
}
