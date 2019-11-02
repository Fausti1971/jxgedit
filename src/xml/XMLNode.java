package xml;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import application.ConfigurationConstants;
import application.Rest;
import tag.XGTagable;

public class XMLNode implements XGTagable, ConfigurationConstants
{
	private static Logger log = Logger.getAnonymousLogger();

	public static XMLNode init() throws XMLStreamException
	{	return XMLNode.parse(CONFIGFILEPATH.toFile());
	}

	public static XMLNode parse(File f) throws XMLStreamException
	{	XMLNode node = null, par = null, root = null;
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader rd = inputFactory.createXMLEventReader(new StreamSource(f));

		while(rd.hasNext())
		{	XMLEvent ev = rd.nextEvent();
			if(ev.isStartDocument())
				log.info("start parsing: " + f);
			if(ev.isStartElement())
			{//	log.info("start: " + ev);
				par = node;
				node = (new XMLNode(par, ev.asStartElement().getName().getLocalPart(), createProperties(ev.asStartElement().getAttributes())));
				if(par == null) root = node;
			}
			if(ev.isCharacters())
			{	node.setTextContent(ev.asCharacters().getData().trim());
			}
			if(ev.isEndElement())
			{	//log.info("end: " + ev);
				node = node.getParent();
			}
			if(ev.isEndDocument())
			{	log.info("parsing finished: " + f);
			}
		}
		return root;
	}

	private static Properties createProperties(Iterator<Attribute> i)
	{	Properties prop = new Properties();
		while(i.hasNext())
		{	Attribute a = i.next();
			prop.put(a.getName().getLocalPart(), a.getValue());
		}
		return prop;
	}

/*************************************************************************************************************/

	private final XMLNode parent;
	private final Set<XMLNode> children= new HashSet<>();
	private final String tag;
	private String text = "";
	private final Properties attributes;

	public XMLNode(XMLNode par, String tag, Properties prop)
	{	this.parent = par;
		this.tag = tag;
		this.attributes = prop;
		if(par != null)
		{	this.parent.addChild(this);
//			log.info("new node (" + this.tag + ") added to: " + par.getTag());
		}
	}

	private XMLNode getParent()
	{	return this.parent;
	}

	public void setTextContent(String s)
	{	this.text = s;
//		log.info("text (" + s + ") added to " + this);
	}

	public void addChild(XMLNode child)
	{	this.children.add(child);
	}

//	private void addSibling(XMLNode sibling)
//	{	this.parent.children.add(sibling);
//	}

	public Set<XMLNode> getChildren()
	{	return this.children;
	}

	public XMLNode getChildNode(String tag)
	{	for(XMLNode n : this.children) if(n.getTag().equals(tag)) return n;
		return null;
	}

	public String getTag()
	{	return this.tag;
	}

	public boolean isLeaf()
	{	return this.children.size() != 0;
	}

	public String getTextContent()
	{	return this.text;
	}

	public String getChildNodeTextContent(String tag, String def)
	{	XMLNode n = this.getChildNode(tag);
		if(n == null) return def;
		return n.getTextContent();
	}

	public int parseChildNodeTextContent(String tag, int def)
	{	XMLNode n = this.getChildNode(tag);
		if(n == null) return def;
		return Rest.parseIntOrDefault(n.getTextContent(),def);
	}

	public String getAttribute(String a)
	{	return this.attributes.getProperty(a);
	}

	@Override public String toString()
	{	if(this.isLeaf()) return this.tag + " = " + this.text;
		else return this.tag + " (" + this.children.size() + ")";
	}
}
