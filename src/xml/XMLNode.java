package xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.helpers.DefaultHandler;
import application.ConfigurationConstants;
import application.Rest;
import application.XGLoggable;
import tag.XGTagable;

public class XMLNode implements XGTagable, ConfigurationConstants, XGLoggable
{
	public static XMLNode parse(File xml)
	{	if(xml == null || !xml.canRead()) return null;
		XMLNode current_node = null, parent_node = null, root_node = null;

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, true);//nur für DTDs

		try
		{	XMLEventReader rd = inputFactory.createXMLEventReader(new StreamSource(xml));
			while(rd.hasNext())
			{	XMLEvent ev = rd.nextEvent();
				if(ev.isStartDocument()) LOG.info("parsing started: " + xml);
				if(ev.isStartElement())
				{	parent_node = current_node;
					current_node = new XMLNode(ev.asStartElement().getName().getLocalPart(), XMLNode.createProperties(ev.asStartElement().getAttributes()));
					if(parent_node != null) parent_node.addChildNode(current_node);	//falls parent existiert, füge diesem diese node hinzu
					else root_node = current_node;	//falls nicht ist diese node root-node
				}
				if(ev.isCharacters()) if(current_node != null) current_node.setTextContent(ev.asCharacters().getData().trim());
				if(ev.isEndElement()) if(current_node != null) current_node = current_node.getParentNode();
				if(ev.isEndDocument()) LOG.info("parsing finished: " + xml);
			}
			rd.close();
		}
		catch(XMLStreamException e)
		{	LOG.severe(e.getMessage() + xml);
		}
		return root_node;
	}

	private static Properties createProperties(Iterator<Attribute> i)
	{	Properties prop = new Properties();
		while(i.hasNext())
		{	Attribute a = i.next();
			prop.put(a.getName().getLocalPart(), a.getValue());
		}
		return prop;
	}
	public class SAXLocalNameCount extends DefaultHandler
	{
		
	}

/*************************************************************************************************************/

	private XMLNode parent;
	private final Set<XMLNode> childNodes = new LinkedHashSet<>();
	private final String tag;
	private String content = null;
	private final Properties attributes;


	public XMLNode(String tag, Properties attr)
	{	this.tag = tag;
		if(attr != null) this.attributes = attr;
		else this.attributes = new Properties();
	}

	public XMLNode(String tag, Properties attr, String txt)
	{	this(tag, attr);
		this.content = txt;
	}

	public XMLNode getParentNode()
	{	return this.parent;
	}

	public void setTextContent(String s)
	{	this.content = s;
	}

	public void setTextContent(int v)
	{	this.content = String.valueOf(v);
	}

	public void addChildNode(XMLNode child)
	{	this.childNodes.add(child);
		child.parent = this;
	}

	public void removeNode()
	{	this.parent.removeChildNode(this);
	}

	public void removeChildNode(XMLNode c)
	{	this.childNodes.remove(c);
	}

	public Set<XMLNode> getChildNodes()
	{	return this.childNodes;
	}

	public XMLNode getChildNode(String tag)
	{	for(XMLNode n : this.childNodes) if(n.getTag().equals(tag)) return n;
		return null;
	}

	public Set<XMLNode> getChildNodes(String tag)
	{	Set<XMLNode> set = new LinkedHashSet<>();
		for(XMLNode x : this.childNodes)
			if(x.getTag().equals(tag))
				set.add(x);
		return set;
	}

	public XMLNode getChildNodeOrNew(String tag)
	{	XMLNode n = this.getChildNode(tag);
		if(n == null)
		{	n = new XMLNode(tag, null);
			this.addChildNode(n);
		}
		return n;
	}

	@Override public String getTag()
	{	return this.tag;
	}

	public String getTextContent()
	{	return this.content;
	}

	public String getChildNodeTextContent(String tag, String def)
	{	XMLNode n = this.getChildNode(tag);
		if(n == null) return def;
		return n.getTextContent();
	}

	public int parseChildNodeIntegerContent(String tag, int def)
	{	XMLNode n = this.getChildNode(tag);
		if(n == null) return def;
		return Rest.parseIntOrDefault(n.getTextContent(), def);
	}

	public int parseChildNodeIntegerContentOrNew(String tag, int def)
	{	XMLNode n = this.getChildNode(tag);
		if(n == null)
		{	n = new XMLNode(tag, null);
			n.setTextContent(def);
			this.addChildNode(n);
		}
		return Rest.parseIntOrDefault(n.getTextContent(), def);
	}

	public XMLNode getChildNodeWithID(String tag, String id)
	{	for(XMLNode x : this.childNodes)
			if(x.getTag().equals(tag) && id.equals(x.getStringAttribute(ATTR_ID)))
				return x;
		return null;
	}

	public Properties getAttributes()
	{	return this.attributes;
	}

	public boolean hasAttribute(String name)
	{	return this.attributes.containsKey(name);
	}

	public String getStringAttribute(String a, String def)
	{	String s = this.attributes.getProperty(a);
		if(s == null) return def;
		return s;
	}

	public String getStringAttribute(String a)
	{	return this.attributes.getProperty(a);
	}

	public void setStringAttribute(String attrMidioutput, String outputName)
	{	this.attributes.put(attrMidioutput, outputName);
	}

	public int getIntegerAttribute(String a, int def)
	{	String s = (String)this.attributes.get(a);
		if(s == null) return def;
		return Integer.parseInt((String)this.attributes.get(a));
	}

	public int getIntegerAttribute(String a) throws NumberFormatException
	{	return Integer.parseInt((String)this.attributes.get(a));
	}

	public void setIntegerAttribute(String attr, int t)
	{	this.attributes.put(attr, String.valueOf(t));
	}

	public double getDoubleAttribute(String attr, double def)
	{	if(this.attributes.contains(attr))
			return Double.parseDouble((String)this.attributes.get(attr));
		else return def;
	}

	public void save(File file) throws IOException, XMLStreamException
	{	XMLOutputFactory factory = XMLOutputFactory.newInstance();
		if(!file.exists()) file.createNewFile();
		XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(file));

		this.writeNode(writer, this);
		writer.close();
		LOG.info(this + " saved to: " + file);
	}

	private void writeNode(XMLStreamWriter w, XMLNode n)
	{	try
		{	w.writeStartElement(n.tag);
			if(n.attributes != null)//Attribute müssen VOR dem text geschrieben werden, sonst javax.xml.stream.XMLStreamException: Attribute not associated with any element
			{	for(Entry<Object,Object> e : n.attributes.entrySet())
					w.writeAttribute(e.getKey().toString(), e.getValue().toString());
			}
			if(n.content != null) w.writeCharacters(n.content);
			for(XMLNode n2 : n.childNodes) n2.writeNode(w, n2);
			w.writeEndElement();
		}
		catch(XMLStreamException e1)
		{	e1.printStackTrace();
		}
	}

	@Override public String toString()
	{	return this.tag;
	}
}
