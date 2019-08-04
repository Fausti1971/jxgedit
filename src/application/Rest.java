package application;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface Rest
{	static Set<String> splitString(String s)
	{	if(s == null) return null;
		StringTokenizer t = new StringTokenizer(s, ",");
		Set<String> a = new HashSet<>();
		while(t.hasMoreElements()) a.add(t.nextToken());
		return a;
	}

	static int linearIO(int i, int in_min, int in_max, int out_min, int out_max)
	{	return((int)(((float)(i - in_min) / (float)(in_max - in_min) * (out_max - out_min)) + out_min));}

	static int parseIntOrDefault(String s, int def)
	{	try
		{	return Integer.parseInt(s);}
		catch(NumberFormatException e)
		{	return def;}
	}

	static String getFirstNodeChildTextContentByTagAsString(Node n, String tag)
	{	Node n1 = Rest.getFirstChildNodeByTag(n, tag);
		if(n1 != null) return n1.getTextContent(); 
		return null;
	}

	static Node getFirstChildNodeByTag(Node n, String tag)
	{	if(n != null)
		{	NodeList l = n.getChildNodes();
			for(int i = 0; i < l.getLength(); i++)
			{	Node n1 = l.item(i);
				if(n1.getNodeName().equals(tag)) return n1; 
			}
		}
		return null;
	}

	static String getStringOrDefault(String s, String def)
	{	if(s == null) return def;
		return s;
	}

	static Set<String> splitNodesAttributeMap(Node n, String tag)
	{	if(n.hasAttributes())
		{	NamedNodeMap map = n.getAttributes();
			return Rest.splitString(map.getNamedItem(tag).getTextContent());
		}
		return null;
	}
}
