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

/**
 * XMLNode (from Set) tag = "item", content = "text";
 * XMLNode (from Map) tag = "entry", tag = "key", tag = "value"
 * @author thomas
 *
 */
public class XMLNode implements XGTagable, ConfigurationConstants
{
	private static Logger log = Logger.getAnonymousLogger();

	public static XMLNode parse(File f)
	{	if(!f.canRead()) return null;
		XMLNode current_node = null, parent_node = null, root_node = null;
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, "true");

		try
		{	XMLEventReader rd = inputFactory.createXMLEventReader(new StreamSource(f));
			while(rd.hasNext())
			{	XMLEvent ev = rd.nextEvent();
				if(ev.isStartDocument()) log.info("parsing started: " + f);
				if(ev.isStartElement())
				{	parent_node = current_node;
					current_node = new XMLNode(ev.asStartElement().getName().getLocalPart(), XMLNode.createProperties(ev.asStartElement().getAttributes()));
					if(parent_node != null) parent_node.addChildNode(current_node);	//falls parent existiert, füge diesem diese node hinzu
					else root_node = current_node;	//falls nicht ist diese node root-node
				}
				if(ev.isCharacters()) if(current_node != null) current_node.setTextContent(ev.asCharacters().getData().trim());
				if(ev.isEndElement()) if(current_node != null) current_node = current_node.getParentNode();
				if(ev.isEndDocument()) log.info("parsing finished: " + f);
			}
			rd.close();
		}
		catch(XMLStreamException e)
		{	log.info(e.getMessage() + f);
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

/*************************************************************************************************************/

//	private XGTree tree;
	private XMLNode parent;
	private final Set<XMLNode> childNodes = new LinkedHashSet<>();
	private final String tag;
	private String content = "no content";
	private final Properties attributes;
//	private boolean isSelected = false;

	public XMLNode(String tag, Properties attr)
	{	this.tag = tag;
		this.attributes = attr;
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
//		log.info("text (" + s + ") added to " + this);
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

	public String getStringAttribute(String a)
	{	return this.attributes.getProperty(a);
	}

	public int getIntegerAttribute(String a)
	{	return Integer.parseInt((String)this.attributes.get(a));
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
			w.writeCharacters(n.content);
			if(n.attributes != null)
			{	for(Entry<Object,Object> e : n.attributes.entrySet())
					w.writeAttribute(e.getKey().toString(), e.getValue().toString());
			}
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

/*	XGTreeNode

	@Override public String getNodeText()
	{	if(this.tag.equals("item")) return this.content;
		if(this.tag.equals("entry")) return this.getChildNodeTextContent("value", "no value for " + this.getChildNodeTextContent("key", "no key"));
		return this.tag;
	}

	@Override public TreeNode getParent()
	{	return this.getParentNode();
	}

	@Override public boolean getAllowsChildren()
	{	return true;
	}

	@Override public Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(this.getChildNodes());
	}

	@Override public Set<String> getContexts()
	{	Set<String> set = new LinkedHashSet<>();
		set.add("select");
		return set;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	this.setSelected(true);
	}

	@Override public void nodeFocussed(boolean b)
	{	this.setSelected(b);
	}

	@Override public void setSelected(boolean s)
	{	System.out.println(this.content + " " + s);
		if(s) this.getParentNode().attributes.put(ATTR_SELECTED, this.content);
		else this.getParentNode().attributes.put(ATTR_SELECTED, "");
	}

	@Override public boolean isSelected()
	{	String s = this.getParentNode().getStringAttribute(ATTR_SELECTED);
		return s.equals(this.content);
	}

	@Override public void setTreeComponent(XGTree t)
	{	this.tree = t;
	}

	@Override public XGTree getTreeComponent()
	{	return this.tree;
	}
*/
}
