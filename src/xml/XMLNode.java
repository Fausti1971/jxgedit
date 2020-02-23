package xml;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.tree.TreeNode;
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
import gui.XGTree;
import gui.XGTreeNode;
import tag.XGTagable;

public class XMLNode implements XGTagable, ConfigurationConstants, XGTreeNode
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
					node = new XMLNode(ev.asStartElement().getName().getLocalPart(), XMLNode.createProperties(ev.asStartElement().getAttributes()));
					if(par != null) par.addChildNode(node);
					if(par == null) root = node;
				}
				if(ev.isCharacters())
				{	if(node != null) node.setTextContent(ev.asCharacters().getData().trim());
				}
				if(ev.isEndElement())
				{	//log.info("end: " + ev);
					if(node != null) node = node.getParentNode();
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

	public static XMLNode fromSet(String name, Set<?> set)
	{	XMLNode n = new XMLNode(name, null);
		for(Object o : set) n.addChildNode(new XMLNode(o.toString(), null));
		return n;
	}

/*************************************************************************************************************/

	private XGTree tree;
	private XMLNode parent;
	private final Set<XMLNode> childNodes = new LinkedHashSet<>();
	private final String tag;
	private String text = "";
	private final Properties attributes;
	private boolean isSelected = false;

	public XMLNode(String tag, Properties attr)
	{	this.tag = tag;
		this.attributes = attr;
	}

	private XMLNode getParentNode()
	{	return this.parent;
	}

	public void setTextContent(String s)
	{	this.text = s;
//		log.info("text (" + s + ") added to " + this);
	}

	public void setTextContent(int v)
	{	this.text = String.valueOf(v);
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
			this.addChildNode(n);
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
			for(XMLNode n2 : n.childNodes) n2.writeNode(w, n2);
			w.writeEndElement();
		}
		catch(XMLStreamException e1)
		{	e1.printStackTrace();
		}
	}

	@Override public String toString()
	{	if(this.isLeaf()) return this.tag;
		else return this.tag + " (" + this.childNodes.size() + ")";
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
	{	this.isSelected = s;
	}

	@Override public boolean isSelected()
	{	return this.isSelected;
	}

	@Override public void setTreeComponent(XGTree t)
	{	this.tree = t;
	}

	@Override public XGTree getTreeComponent()
	{	return this.tree;
	}
}
