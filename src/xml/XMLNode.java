package xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import application.XGLoggable;
import application.XGStrings;
import tag.XGTagable;import tag.XGTagableSet;

public class XMLNode implements XGTagable,  XGLoggable, XGStrings
{
	private static final String ERRORSTRING = " contains invalid character";

	public static XMLNode parse(java.io.InputStream xml)throws IOException
	{	if(xml == null) throw new IOException();
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
				if(ev.isEndElement()) if(current_node != null) current_node = current_node.parent;
				if(ev.isEndDocument()) LOG.info("parsing finished: " + xml);
			}
			rd.close();
		}
		catch(XMLStreamException e)
		{	LOG.severe(e.getMessage() + xml);
			new JOptionPane(e.getMessage() + xml);
			System.exit(1);
		}
		return root_node;
	}

	private static XGTagableSet<XGProperty> createProperties(Iterator<Attribute> i)
	{	XGTagableSet<XGProperty> prop = new XGTagableSet<>();
		String name;
		while(i.hasNext())
		{	Attribute a = i.next();
			name = a.getName().getLocalPart();
			if(!XGStrings.isAlNum(name)) throw new RuntimeException(name + ERRORSTRING);
			prop.add(new XGProperty(name, a.getValue()));
		}
		return prop;
	}

/*************************************************************************************************************/

	private XMLNode parent;
	private final Set<XMLNode> childNodes = new LinkedHashSet<>();
	private final String tag;
	private final StringBuffer content = new StringBuffer("");
	private final XGTagableSet<XGProperty> attributes = new XGTagableSet<>();


	public XMLNode(String tag)
	{	this.tag = tag;
	}

	public XMLNode(String tag, XGProperty attr)
	{	this(tag, attr, "");
	}

	private XMLNode(String tag, XGProperty attr, String txt)
	{	//if(!XGStrings.isAlNum(tag)) throw new RuntimeException(tag + ERRORSTRING);
		this.tag = XGStrings.toAlNum(tag);
		this.attributes.add(attr);
		this.content.replace(0, this.content.length(), txt);
	}

	private XMLNode(String tag, XGTagableSet<XGProperty> attr)
	{	this(tag);
		this.attributes.addAll(attr);
	}

	public void traverse(String tag, Consumer<XMLNode> func)
	{	for(XMLNode x : this.getChildNodes(tag))
		{	if(x.hasChildNode(tag)) x.traverse(tag, func);
			else func.accept(x);
		}
	}

	public XMLNode getParentNode()
	{	return this.parent;
	}

	public void setTextContent(final String s)
	{	this.content.replace(0, this.content.length(), s);
	}

	public void setTextContent(int v)
	{	this.content.replace(0, this.content.length(), String.valueOf(v));
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

	public boolean hasChildNode(String tag)
	{	return this.getChildNode(tag) != null;
	}

	public final XMLNode getChildNode(String tag)
	{	for(XMLNode n : this.childNodes) if(n.tag.equals(tag)) return n;
		return null;
	}

/**
 * liefert ein Set von (ausschließlich direkten) Kindern mit zutreffendem Tag
 * @param tag	zu findender Tag
 * @return Set von direkten Kindern mit zutreffendem Tag
 */
	public Set<XMLNode> getChildNodes(String tag)
	{	Set<XMLNode> set = new LinkedHashSet<>();
		for(XMLNode x : this.childNodes)
			if(x.tag.equals(tag))
				set.add(x);
		return set;
	}

	public final XMLNode getChildNodeOrNew(String tag)
	{	XMLNode n = this.getChildNode(tag);
		if(n == null)
		{	n = new XMLNode(tag);
			this.addChildNode(n);
		}
		return n;
	}

	public final XMLNode getLastChild(String tag)
	{	XMLNode x = null;
		for(XMLNode node: this.getChildNodes(tag)) x = node;
		return x;
	}

	public final XMLNode getLastChildOrNew(String tag)
	{	XMLNode last = this.getLastChild(tag);
		if(last == null)
		{	last = new XMLNode(tag);
			this.addChildNode(last);
		}
		return last;
	}

	@Override public String getTag()
	{	return this.tag;
	}

	public final StringBuffer getTextContent()
	{	return this.content;
	}

	public final XGTagableSet<XGProperty> getAttributes()
	{	return this.attributes;
	}

/**
 * returniert den StringBuffer des Attributes attr, legt dieses bei Abstinenz an
 * @param attr Attributname
 * @param text Inhalt für eine eventuell neu anzulegendes Attribut
 * @return StringBuffer des vorhandenen oder neu angelegten Attributes
 */
	public final StringBuffer getStringBufferAttributeOrNew(String attr, String text)
	{	if(!this.attributes.containsKey(attr)) this.setStringAttribute(attr, text);
		return this.attributes.get(attr).getValue();
	}

/**
 * returniert den Inhalt des StringBuffers des Attributes attr als String, bei Abstinenz null
 * @param attr Attributname
 * @return StringBuffer des vorhandenen Attributes oder null
 */
	public final String getStringAttribute(String attr)
	{	if(this.attributes.containsKey(attr)) return this.attributes.get(attr).getValue().toString();
		else return null;
	}

/**
 * returnierte den Inhalt des Attributes attr, legt bei Abstinenz diese Attribut jedoch nicht an sondern liefert den String def zurück
 * @param attr Attributname
 * @param def Defaultwert, falls Attribut nicht vorhanden ist
 * @return Inhalt des angegebenen Attributes als String oder String def
 */
	public final String getStringAttributeOrDefault(String attr, String def)
	{	if(this.attributes.containsKey(attr)) return this.attributes.get(attr).getValue().toString();
		else return def;
	}

	public void setStringAttribute(final String tag, final String attr)
	{	if(!XGStrings.isAlNum(tag)) throw new RuntimeException(tag + ERRORSTRING);
		if(this.attributes.containsKey(tag)) this.attributes.get(tag).setValue(attr);
		else this.attributes.add(new XGProperty(tag, attr));
	}

	public int getValueAttribute(String attr, int def)
	{	return XGStrings.parseValue(this.getStringAttribute(attr), def);
	}

	public final int getIntegerAttribute(String a, int def)
	{	if(this.attributes.containsKey(a)) return Integer.parseInt(this.attributes.get(a).getValue().toString());
		else return def;
	}

	public void setIntegerAttribute(String attr, final int t)
	{	this.setStringAttribute(attr, String.valueOf(t));
	}

	public final double getDoubleAttribute(String attr, double def)
	{	if(this.attributes.containsKey(attr))
			return Double.parseDouble(this.attributes.get(attr).toString());
		else return def;
	}

	public void save(File file) throws IOException, XMLStreamException
	{	XMLOutputFactory factory = XMLOutputFactory.newInstance();
		if(!file.exists()) file.createNewFile();
		XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(file));

		writer.writeStartDocument();
		this.writeNode(writer, this);
		writer.close();
		LOG.info(this + " saved to: " + file);
	}

	private void writeNode(XMLStreamWriter w, XMLNode n)
	{	try
		{	w.writeStartElement(n.tag);
			//Attribute müssen VOR dem text geschrieben werden, sonst javax.xml.stream.XMLStreamException: Attribute not associated with any element
			for(XGProperty e : n.attributes) w.writeAttribute(e.getTag(), e.getValue().toString());
			if(n.content.length() != 0) w.writeCharacters(n.content.toString());
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
