package table;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import application.*;
import static parm.XGParameterConstants.DEF_SELECTORVALUE;
import tag.XGTagable;
import tag.XGTagableSet;
import xml.XMLNode;
import static xml.XMLNodeConstants.*;
/**
 * simple taggable HashMap<Integer, HashMap<Integer, Integer>>, deren erster int der Wert des Selektors, der zweite int der Wert des zugehörigen Defaults ist
 * @author thomas
 *
 */
public class XGDefaultsTable implements XGLoggable, XGTagable, XGTableConstants
{
	public static final int DEF_DRUMSETPROGRAM = XGStrings.parseValue("127.0.0", 127 << 14);
	public static final int NO_ID = DEF_SELECTORVALUE;
	public static final XGTagableSet<XGDefaultsTable> DEFAULTSTABLES = new XGTagableSet<>();

	public static void init()
	{	try
		{	File f = JXG.getDeviceXMLResourceFile(XML_DEFAULTS);
			XMLNode n = XMLNode.parse(f);
			for(XMLNode t : n.getChildNodes(TAG_DEFAULTSTABLE))
			{	DEFAULTSTABLES.add(new XGDefaultsTable(t));
			}
		}
		catch(IOException e)
		{	LOG.info(e.getMessage());
		}

/**
* eine Dummy-Defaultstable, die lediglich die id returniert;
*/
		DEFAULTSTABLES.add(new XGDefaultsTable(ATTR_ID)
			{	@Override public int get(int id, int sel)
				{	return (id);
				}
			}
		);

/**
* eine Dummy-Defaultstable für die Programmdefaults der verschiedenen Partmodes
*/
		DEFAULTSTABLES.add(new XGDefaultsTable(TABLE_PARTMODE)
			{	@Override public int get(int id, int sel)
				{	switch(id)
					{	case 9:		return 2;
						case 25:	return 4;
						case 41:
						case 57:	return 1;
						default:	return 0;
					}
				}
			}
		);

/**
* eine Dummy-Defaultstable für Defaultprogramme aller Multiparts
*/
		DEFAULTSTABLES.add(new XGDefaultsTable(TABLE_PROGRAM)
			{	@Override public int get(int id, int sel)
				{	switch(id)
					{	case 9:
						case 25:
						case 41:
						case 57:	return DEF_DRUMSETPROGRAM;
						default:	return 0;
					}
				}
			}
		);
	}

/*************************************************************************************************************/

	private final String tag;
	private final Map<Integer, Map<Integer, Integer>> idMap = new HashMap<>();//id, selectorValue, defaultValue

	public XGDefaultsTable(String tag)
	{	this.tag = tag;
	}

	public XGDefaultsTable(XMLNode n)
	{	this(n.getStringAttribute(ATTR_NAME));

		int id;
		this.put(NO_ID, DEF_SELECTORVALUE, n.getValueAttribute(ATTR_DEFAULT, 0));//für XGOpcodes ohne mutableDefaults

		if(n.hasChildNode(TAG_ID))
		{	for(XMLNode i : n.getChildNodes(TAG_ID))
			{	id = i.getIntegerAttribute(ATTR_VALUE, NO_ID);
				this.put(id, DEF_SELECTORVALUE, 0);
				for(XMLNode d : i.getChildNodes(TAG_ITEM))
				{	this.put(id, d.getValueAttribute(ATTR_SELECTORVALUE, DEF_SELECTORVALUE), d.getValueAttribute(ATTR_VALUE, 0));
				}
			}
		}
		else
			for(XMLNode d : n.getChildNodes(TAG_ITEM))
				this.put(NO_ID, d.getValueAttribute(ATTR_SELECTORVALUE, DEF_SELECTORVALUE), d.getValueAttribute(ATTR_VALUE, 0));

//		if(!this.idMap.containsKey(NO_ID)) throw new RuntimeException("table " + this.tag + " has no fallback");
		LOG.info(this.getClass().getSimpleName() + " " + this.tag + " initialized");
	}

	public void put(int id, int sel, int def)
	{	Map<Integer, Integer> defMap;
		if(this.idMap.containsKey(id)) defMap = this.idMap.get(id);
		else defMap = new HashMap<>();
		defMap.put(sel, def);
		this.idMap.putIfAbsent(id, defMap);
	}

	public int get(int id, int sel)
	{	if(!this.idMap.containsKey(id)) id = NO_ID;
		if(!this.idMap.get(id).containsKey(sel)) sel = DEF_SELECTORVALUE;
		return this.idMap.get(id).get(sel);
	}

	@Override public String getTag(){	return this.tag;}

//	public XMLNode toXMLNode()//Überbleibsel zum dekodieren der *.ods
//	{	int min = -1, max = -1;
//		XMLNode table = new XMLNode(TAG_DEFAULTSTABLE, new XGProperty(ATTR_NAME, this.tag));
//		for(int m : this.idMap.keySet())
//		{	XMLNode id = new XMLNode(TAG_ID, new XGProperty(ATTR_VALUE, XGStrings.valueToString(m)));
//			table.addChildNode(id);
//			for(int s : this.idMap.get(m).keySet())
//			{	XMLNode item = new XMLNode(TAG_ITEM, new XGProperty(ATTR_SELECTORVALUE, XGStrings.valueToString(s)));
//				int v = this.idMap.get(m).get(s);
//				if(min == -1) min = v;
//				min = Math.min(min, v);
//				if(max == -1) max = v;
//				max = Math.max(max, v);
//				item.getAttributes().getOrNew(ATTR_VALUE, new XGProperty(ATTR_VALUE, XGStrings.valueToString(v)));
//				id.addChildNode(item);
//			}
//		}
//		table.getAttributes().add(new XGProperty("range", min + "-" + max));
////		System.out.println("table=" + this.tag + " range = " + min + "/" + max);
//		return table;
//	}
}