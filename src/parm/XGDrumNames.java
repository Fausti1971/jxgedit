package parm;
import static application.ConfigurationConstants.XMLPATH;import application.JXG;import static application.XGLoggable.LOG;import application.XGStrings;import static parm.XGDefaultsTable.DEFAULTSTABLE;import xml.XMLNode;import static xml.XMLNodeConstants.*;import java.io.IOException;import java.util.HashMap;import java.util.Map;

public class XGDrumNames
{	public static XGDrumNames DRUMS = new XGDrumNames();

	public static void init()
	{	try
		{	XMLNode n = XMLNode.parse(JXG.getResourceStream(XMLPATH + XML_DRUMS));
			for(XMLNode d : n.getChildNodes(TAG_DRUMS))
			{	for(XMLNode k : d.getChildNodes(TAG_KEY))
				{	int key = k.getValueAttribute(ATTR_VALUE, 0);
					for(XMLNode i : k.getChildNodes(TAG_ITEM))
					{	int sel = i.getValueAttribute(ATTR_SELECTORVALUE, 0);
						DRUMS.put(key, sel, i.getStringAttribute(ATTR_NAME));
					}
				}
			}
		}
		catch(IOException e)
		{	LOG.info(e.getMessage());
		}
	}

/*********************************************************************************************************/

	private final int DEF_PRG = 127 << 14;
	private final Map<Integer, Map<Integer, String>> NAMES = new HashMap<>();

	public void put(int key, int prg, String name)
	{	Map<Integer, String> prgs = NAMES.getOrDefault(key, new HashMap<>());
		prgs.put(prg, name);
		NAMES.putIfAbsent(key, prgs);
	}

	public String get(int key, int prg)
	{	Map<Integer, String> prgs = NAMES.get(key);
		return prgs.getOrDefault(prg, prgs.getOrDefault(DEF_PRG, "No Sound"));
	}

	public void print()
	{	System.out.println("<drumnames>");
		for(int key : NAMES.keySet())
		{	System.out.println("\t<key value=\"" + key + "\">");
			for(int prg : NAMES.get(key).keySet())
			{	System.out.println("\t\t<item selectorValue=\"" + XGStrings.valueToString(prg) + "\" name=\"" + this.get(key, prg) + "\"/>");
			}
			System.out.println("\t</key>");
		}
		System.out.println("</drumnames>");
	}
}
