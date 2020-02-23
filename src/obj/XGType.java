package obj;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressConstants;
import adress.XGAddressableSet;
import application.ConfigurationConstants;
import device.XGDevice;
import gui.XGTree;
import gui.XGTreeNode;
import tag.XGTagable;
import tag.XGTagableSet;
import value.XGValue;
import xml.XMLNode;
//TODO: unsupported types (nicht in object.xml vorgefunden, sondern aus "XG" entnommen) hervorheben (in rot darstellen); zum laden "falscher" bulks...
public class XGType implements ConfigurationConstants, XGTagable, XGTypeConstants, XGAddressConstants, XGTreeNode
{	private static Logger log = Logger.getAnonymousLogger();

	public static XGTagableSet<XGType> init(XGDevice dev)
	{	XGTagableSet<XGType> set = new XGTagableSet<>();
		File file;
		try
		{	file = dev.getResourceFile(XML_TYPE);
		}
		catch(FileNotFoundException e)
		{	return set;
		}
		XMLNode xml = XMLNode.parse(file);
			for(XMLNode x : xml.getChildNodes())
			{	if(x.getTag().equals(TAG_TYPE))
				{	XGType t = new XGType(dev, x);
					set.add(t);
				}
			}
		log.info(set.size() + " object-types initialized");
		return set;
	}
/*
	public static XGTagableSet<XGObjectType> getAllObjectTypes(XGDevice dev)
	{	if(STORAGE.containsKey(dev)) return STORAGE.get(dev);
		if(STORAGE.containsKey(XGDevice.getDefaultDevice())) return STORAGE.get(XGDevice.getDefaultDevice());
		return new XGTagableSet<XGObjectType>();
	}
	
	public static XGObjectType getObjectTypeOrNew(XGDevice dev, XGAdress adr)
	{	for(XGObjectType t : getAllObjectTypes(dev))
		{	if(t.include(adr)) return t;
		}
		return new XGObjectType(dev, adr);
	}
	
	public static XGObjectType getObjectTypeOrNew(XGDevice dev, String name)
	{	XGTagableSet<XGObjectType> set = getAllObjectTypes(dev);
		if(set.containsKey(name)) return set.get(name);
		return new XGObjectType(dev, name, null);
	}
	
	public static void requestAll(XGMessenger src, XGMessenger dest)
	{	for(XGObjectType o : getAllObjectTypes(src.getDevice()))
			for(XGBulkDumpSequence s : o.getDumpsequences()) s.requestAll(src, dest);
		log.info("request completetd");
	}
*/
/**********************************************************************************************************/

	private XGTree tree;
	private final XGDevice device;
	private boolean isSelected = false;
	private final String name;
	private final Set<XGBulkDumpSequence> bulks;
	private final XGAddressableSet<XGInstance> instances = new XGAddressableSet<XGInstance>();

	public XGType(XGDevice dev, XGType t)	//für "unsupported" types
	{	this.device = dev;
		this.name = "(unsupported) " + t.name;
		this.bulks = null;
	}

	public XGType(XGDevice dev, XGAddress adr)
	{	this(dev, DEF_OBJECTTYPENAME + adr, null);
	}

	private XGType(XGDevice dev, String name, Set<XGBulkDumpSequence> dseq)
	{	this.device = dev;
		this.name = name;
		this.bulks = dseq;
		log.info("type initialized: " + this);
	}

	XGType(XGDevice dev, XMLNode n)
	{	this(dev, n.getChildNode(TAG_NAME).getTextContent(), new HashSet<>());

		for(XMLNode seq : n.getChildNodes())
			if(seq.getTag().equals(TAG_DUMPSEQ)) this.bulks.add(new XGBulkDumpSequence(seq));
	}

	public Set<XGBulkDumpSequence> getDumpsequences()
	{	return this.bulks;
	}

	public boolean include(XGAddress adr)
	{	for(XGBulkDumpSequence s : this.bulks) if(s.contains(adr)) return true;
		return false;
	}

	public int maxInstanceCount()
	{	int i = 0;
		for(XGBulkDumpSequence s : this.bulks) i = Math.max(s.maxInstanceCount(), i);
		return i;
	}

	public void addInstance(XGValue v)
	{	if(this.instances.contains(v.getAdress())) return;
		try
		{	this.instances.add(new XGInstance(this.device, v.getAdress()));
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
		}
	}

	public XGAddressableSet<XGInstance> getXGObjectInstances()
	{	return this.instances;
	}

	public XGInstance getInstance(XGAddress adr)
	{	XGInstance i = this.instances.getFirstValid(adr);
		if(i == null)
			try
			{	this.instances.add(i = new XGInstance(this.device, adr));
			}
			catch(InvalidXGAddressException e)
			{	e.printStackTrace();
			}
		return i;
	}

	public boolean hasInstances()
	{	return this.instances.size() != 0;
	}

	@Override public String getNodeText()
	{	return this.name + " (" + this.instances.size() + ")";
	}

	@Override public String toString()
	{	return this.name;
	}

	@Override public TreeNode getChildAt(int childIndex)
	{	return this.instances.get(childIndex);
	}

	@Override public TreeNode getParent()
	{	return this.device;
	}

	@Override public boolean getAllowsChildren()
	{	return true;
	}

	@Override public Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(this.instances.values());
	}

	public XMLNode getTemplate()
	{	return null;
	}

	@Override public String getTag()
	{	return this.name;
	}

	@Override public boolean isSelected()
	{	return this.isSelected;
	}

	@Override public void setSelected(boolean s)
	{	this.isSelected = s;
	}

	@Override public Set<String> getContexts()
	{
		return new LinkedHashSet<>();
	}

	@Override public void actionPerformed(ActionEvent e)
	{
		System.out.println("action: " + e.getActionCommand() + " " + this.getClass().getSimpleName());
	}

	@Override public void setTreeComponent(XGTree t)
	{	this.tree = t;
	}

	@Override public XGTree getTreeComponent()
	{	return this.tree;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}
}
