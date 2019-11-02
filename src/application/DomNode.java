package application;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DomNode implements ConfigurationChangeListener
{	private static DomNode ROOT;
	private static final Logger log = Logger.getAnonymousLogger();

	public static DomNode getConfig()
	{	if(ROOT == null) init();
		return ROOT;
	}

	private static void init()
	{	Document doc;
		Element root;
		File file = CONFIGFILEPATH.toFile();
		if(!file.canRead())
		{	log.info("can't read file: " + file);
			try
			{	doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				doc.appendChild(doc.createElement(APPNAME));
				ROOT = new DomNode(doc.getDocumentElement());
				return;
			}
			catch(ParserConfigurationException e)
			{	e.printStackTrace();
			}
		}
		try
		{	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	//		dbf.setValidating(true);
	//		dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, null);
			doc = dbf.newDocumentBuilder().parse(file);
			root = doc.getDocumentElement();
			if(!root.getTagName().equals(APPNAME)) throw new RuntimeException("this is not a valid config-file: " + file);
			ROOT = new DomNode(root);
		}
		catch (IOException | ParserConfigurationException | SAXException e)
		{ e.printStackTrace();
		}
	}

	public static void exit()
	{	getConfig().save();
	}

/***********************************************************************************************/

	private Node node;

	public DomNode(Node root)
	{	this.node = root;
	}

	public Node toNode()
	{	return this.node;
	}

	private void save()
	{	Transformer t;
		DOMSource s = new DOMSource(this.node.getOwnerDocument());
		try
		{	t = TransformerFactory.newDefaultInstance().newTransformer();
			t.transform(s, new StreamResult(CONFIGFILEPATH.toFile()));
		}
		catch(TransformerException e1)
		{	e1.printStackTrace();
		}
	}

	public void configurationChanged(ConfigurationEvent e)
	{	log.info(e.name());
	}
	public boolean isElement()
	{	return this.node.getNodeType() == Node.ELEMENT_NODE;
	}

	public Set<DomNode> getChildNodes(String tag)
	{	Set<DomNode> set = new TreeSet<>();
		Node n = this.node;
		n = n.getFirstChild();
		while(n != null)
		{	if(n.getNodeName().equals(tag)) set.add(new DomNode(n));
			n = n.getNextSibling();
		}
		return set;
	}

	public DomNode getChildNode(String tag)
	{	Node t = this.node.getFirstChild();
		while(t != null)
		{	if(t.getNodeType() == Node.ELEMENT_NODE && t.getNodeName().equals(tag)) return new DomNode(t);
			t = t.getNextSibling();
		}
		return new DomNode(t);
	}

	public String getChildNodeContent(String tag)
	{	Node t = this.getChildNode(tag).toNode();
		if(t == null) return null;
		if(t.getNodeType() == Node.ELEMENT_NODE) return t.getNodeValue();
		return null;
	}

	public String getChildNodeContent(String tag, String def)
	{	String s = this.getChildNodeContent(tag);
		if(s == null) s = def;
		return s;
	}

	public String getAttribute(String name)
	{	if(this.node != null && this.node.hasAttributes())
			return this.node.getAttributes().getNamedItem(name).getTextContent();
	else return "";
	
	}
	public Set<String> getAttributes()
	{	Set<String> set = new HashSet<>();
		Node n = this.node;
		if(n != null && n.hasAttributes())
		{	NamedNodeMap m = n.getAttributes();
			for(int i = 0; i < m.getLength(); i++)
				set.add(m.item(i).getTextContent());
		}
		return set;
	}

	public String getTextContent()
	{	return null;
	}
}