package xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import application.ConfigurationConstants;
import application.Rest;
import tag.XGTagable;

public class XMLNode implements XGTagable, ConfigurationConstants
{
	private static Logger log = Logger.getAnonymousLogger();

	public static XMLNode parse(File f)
	{	if(!f.canRead()) return null;
		XMLNode node = null, par = null, root = null;
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, "true");

		try
		{	XMLEventReader rd = inputFactory.createXMLEventReader(new StreamSource(f));
			while(rd.hasNext())
			{	XMLEvent ev = rd.nextEvent();
				if(ev.isStartDocument())
					log.info("parsing started: " + f);
				if(ev.isStartElement())
				{//	log.info("start: " + ev);
					par = node;
					node = new XMLNode(ev.asStartElement().getName().getLocalPart(), createProperties(ev.asStartElement().getAttributes()));
					if(par != null) par.addChild(node);
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
			rd.close();
		}
		catch(XMLStreamException e)
		{	log.info(e.getMessage() + f);
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

	private XMLNode parent;
	private final Set<XMLNode> children = new LinkedHashSet<>();
	private final String tag;
	private String text = "";
	private final Properties attributes;

	public XMLNode(String tag, Properties attr)
	{	this.tag = tag;
		this.attributes = attr;
	}

	private XMLNode getParent()
	{	return this.parent;
	}

	public void setTextContent(String s)
	{	this.text = s;
//		log.info("text (" + s + ") added to " + this);
	}

	public void setTextContent(int v)
	{	this.text = String.valueOf(v);
	}

	public void addChild(XMLNode child)
	{	this.children.add(child);
		child.parent = this;
	}

	public void removeNode()
	{	this.parent.removeChild(this);
	}

	public void removeChild(XMLNode c)
	{	this.children.remove(c);
	}

	public Set<XMLNode> getChildren()
	{	return this.children;
	}

	public XMLNode getChildNode(String tag)
	{	for(XMLNode n : this.children) if(n.getTag().equals(tag)) return n;
		return null;
	}

	public XMLNode getChildNodeOrNew(String tag)
	{	XMLNode n = this.getChildNode(tag);
		if(n == null)
		{	n = new XMLNode(tag, null);
			this.addChild(n);
		}
		return n;
	}

	@Override public String getTag()
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
			this.addChild(n);
		}
		return Rest.parseIntOrDefault(n.getTextContent(), def);
	}

	public String getAttribute(String a)
	{	return this.attributes.getProperty(a);
	}

	public void save(File file) throws IOException, XMLStreamException
	{	XMLOutputFactory factory = XMLOutputFactory.newInstance();
		if(!file.exists()) file.createNewFile();
		XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(file));

		this.writeNode(writer, this);
		writer.close();
		log.info(this + " saved to: " + file);
	}

	private void writeNode(XMLStreamWriter w, XMLNode n)
	{	try
		{	w.writeStartElement(n.tag);
			w.writeCharacters(n.text);
			if(n.attributes != null)
			{	for(Entry<Object,Object> e : n.attributes.entrySet())
					w.writeAttribute(e.getKey().toString(), e.getValue().toString());
			}
			for(XMLNode n2 : n.children) n2.writeNode(w, n2);
			w.writeEndElement();
		}
		catch(XMLStreamException e1)
		{	e1.printStackTrace();
		}
	}

	@Override public String toString()
	{	if(this.isLeaf()) return this.tag + " = " + this.text;
		else return this.tag + " (" + this.children.size() + ")";
	}
}
