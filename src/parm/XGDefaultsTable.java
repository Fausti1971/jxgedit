package parm;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import application.*;
import device.XGDevice;
import tag.*;
import xml.XMLNode;
/**
 * simple taggable HashMap<Integer, HashMap<Integer, Integer>>, deren erster int der Wert des Selektors, der zweite int der Wert des zugehörigen Defaults ist
 * @author thomas
 *
 */
public class XGDefaultsTable implements XGParameterConstants, XGLoggable, XGTagable
{
	public static final int NO_ID = DEF_SELECTORVALUE;
	public static final XGTagableSet<XGDefaultsTable> DEFAULTSTABLE = new XGTagableSet<>();

	public static void init()
	{
		try
		{	XMLNode n = XMLNode.parse(JXG.getResourceFile(XML_DEFAULT));
			for(XMLNode t : n.getChildNodes(TAG_DEFAULTTABLE))
			{	DEFAULTSTABLE.add(new XGDefaultsTable(t));
			}
		}
		catch(IOException e)
		{	LOG.info(e.getMessage());
		}

		DEFAULTSTABLE.add(new XGDefaultsTable("id")
			{	@Override public int get(int id, int sel)
				{	return (id);
				}
			}
		);

		DEFAULTSTABLE.add(new XGDefaultsTable("mp_partmode")
			{	@Override public int get(int id, int sel)
				{	if(id == 9) return 2;
					if(id == 25) return 4;
					if(id == 41 || id == 57) return(1);
					return 0;
				}
			}
		);

		DEFAULTSTABLE.add(new XGDefaultsTable("mp_program")
			{	@Override public int get(int id, int sel)
				{	if(id == 9 || id == 25 || id == 41 || id == 57) return 127 << 14;
					return 0;
				}
			}
		);
	}

/*************************************************************************************************************/

	private final String tag;
	private final Map<Integer, Map<Integer, Integer>> idMap = new HashMap<>();

	private XGDefaultsTable(String tag)
	{	this.tag = tag;
	}

	public XGDefaultsTable(XMLNode n)
	{	this(n.getStringAttribute(ATTR_NAME));

		int id = NO_ID;
		this.put(NO_ID, DEF_SELECTORVALUE, n.getValueAttribute(ATTR_DEFAULT, 0));

		Map<Integer, Integer> defMap = new HashMap<>();
		if(n.hasChildNode(TAG_ID))
		{	for(XMLNode i : n.getChildNodes(TAG_ID))
			{	id = i.getIntegerAttribute(ATTR_ID, NO_ID);
				defMap = new HashMap<>();
				for(XMLNode d : i.getChildNodes(TAG_DEFAULT))
				{	defMap.put(d.getValueAttribute(ATTR_SELECTORVALUE, DEF_SELECTORVALUE), d.getValueAttribute(ATTR_DEFAULT, 0));
				}
				this.idMap.put(id, defMap);
			}
		}
		else
			for(XMLNode d : n.getChildNodes(TAG_DEFAULT))
				this.put(NO_ID, d.getValueAttribute(ATTR_SELECTORVALUE, DEF_SELECTORVALUE), d.getValueAttribute(ATTR_DEFAULT, 0));
		if(!this.idMap.containsKey(NO_ID)) throw new RuntimeException("table " + this.tag + " has no fallback");
	}

	public void put(int id, int sel, int def)
	{	if(this.idMap.containsKey(id))
			this.idMap.get(id).put(sel, def);
		else
		{	Map<Integer, Integer> defMap = new HashMap<>();
			defMap.put(sel, def);
			this.idMap.put(id, defMap);
		}
	}

	public int get(int id, int sel)
	{	if(!this.idMap.containsKey(id)) id = NO_ID;
		if(!this.idMap.get(id).containsKey(sel)) sel = DEF_SELECTORVALUE;
		return this.idMap.get(id).get(sel);
	}

	@Override public String getTag()
	{	return this.tag;
	}
}