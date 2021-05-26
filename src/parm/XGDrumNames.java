package parm;
import static application.ConfigurationConstants.XMLPATH;import application.JXG;import static application.XGLoggable.LOG;import application.XGStrings;import xml.XMLNode;import static xml.XMLNodeConstants.*;import java.io.IOException;import java.util.HashMap;import java.util.Map;

public class XGDrumNames
{	public static Map<Integer, XGRealTable> DRUMNAMES = new HashMap<>();//key, prg, name
	private static final int FALLBACKMASK = 127 << 14;

	public static void init()
	{	try
		{	XMLNode n = XMLNode.parse(JXG.getResourceStream(XMLPATH + XML_DRUMS));
			for(XMLNode k : n.getChildNodes(TAG_KEY))
			{	int key = k.getValueAttribute(ATTR_VALUE, -1);
				XGRealTable t = DRUMNAMES.getOrDefault(key, new XGRealTable(Integer.toString(key), "", FALLBACKMASK));
				for(XMLNode i : k.getChildNodes(TAG_ITEM))
				{	t.add(new XGTableEntry(i.getValueAttribute(ATTR_SELECTORVALUE, -1), i.getStringAttribute(ATTR_NAME)));
					DRUMNAMES.putIfAbsent(key, t);
				}
				LOG.info(t + " initialized");
			}
		}
		catch(IOException e)
		{	LOG.severe(e.getMessage());
		}
	}

/*********************************************************************************************************/

	//private final int DEF_PRG = 127 << 14;
	//private final Map<Integer, Map<Integer, String>> NAMES = new HashMap<>();
	//
	//public void put(int key, int prg, String name)
	//{	Map<Integer, String> prgs = NAMES.getOrDefault(key, new HashMap<>());
	//	prgs.put(prg, name);
	//	NAMES.putIfAbsent(key, prgs);
	//}
	//
	//public String get(int key, int prg)
	//{	Map<Integer, String> prgs = NAMES.get(key);
	//	return prgs.getOrDefault(prg, prgs.getOrDefault(DEF_PRG, "No Sound"));
	//}

	//public void print()
	//{	System.out.println("<drumnames>");
	//	for(int key : NAMES.keySet())
	//	{	System.out.println("\t<key value=\"" + key + "\">");
	//		for(int prg : NAMES.get(key).keySet())
	//		{	System.out.println("\t\t<item selectorValue=\"" + XGStrings.valueToString(prg) + "\" name=\"" + this.get(key, prg) + "\"/>");
	//		}
	//		System.out.println("\t</key>");
	//	}
	//	System.out.println("</drumnames>");
	//}
}
