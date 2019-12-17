package obj;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressConstants;
import adress.XGAdressableSet;
import application.ConfigurationConstants;
import device.XGDevice;
import gui.XGFrame;
import gui.XGWindow;
import gui.XGWindowSourceTreeNode;
import tag.XGTagable;
import tag.XGTagableSet;
import value.XGValue;
import xml.XMLNode;
//TODO: unsupported types (nicht in object.xml vorgefunden, sondern aus "XG" entnommen) hervorheben (in rot darstellen); zum laden "falscher" bulks...
public class XGObjectType implements ConfigurationConstants, XGTagable, XGObjectConstants, XGAdressConstants, XGWindowSourceTreeNode
{	private static Logger log = Logger.getAnonymousLogger();

	public static XGTagableSet<XGObjectType> init(XGDevice dev)
	{	XGTagableSet<XGObjectType> set = new XGTagableSet<>();
		File file;
		try
		{	file = dev.getResourceFile(XML_OBJECT);
		}
		catch(FileNotFoundException e)
		{	return set;
		}
		XMLNode xml = XMLNode.parse(file);
			for(XMLNode x : xml.getChildren())
			{	if(x.getTag().equals(TAG_OBJECT))
				{	XGObjectType t = new XGObjectType(dev, x);
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

	private final XGDevice device;
	private boolean isSelected = false;
	private XGWindow window = null;
	private final String name;
	private final Set<XGBulkDumpSequence> bulks;
	private final XGAdressableSet<XGObjectInstance> instances = new XGAdressableSet<XGObjectInstance>();

	public XGObjectType(XGDevice dev, XGAdress adr)
	{	this(dev, DEF_OBJECTTYPENAME + adr, null);
	}

	private XGObjectType(XGDevice dev, String name, Set<XGBulkDumpSequence> dseq)
	{	this.device = dev;
		this.name = name;
		this.bulks = dseq;
		log.info("object-type initialized: " + this);
	}

	XGObjectType(XGDevice dev, XMLNode n)
	{	this(dev, n.getChildNode(TAG_NAME).getTextContent(), new HashSet<>());

		for(XMLNode seq : n.getChildren())
			if(seq.getTag().equals(TAG_DUMPSEQ)) this.bulks.add(new XGBulkDumpSequence(seq));
	}

	public Set<XGBulkDumpSequence> getDumpsequences()
	{	return this.bulks;
	}

	public boolean include(XGAdress adr)
	{	for(XGBulkDumpSequence s : this.bulks) if(s.include(adr)) return true;
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
		{	this.instances.add(new XGObjectInstance(this.device, v.getAdress()));
		}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
		}
	}

	public XGAdressableSet<XGObjectInstance> getXGObjectInstances()
	{	return this.instances;
	}

	public XGObjectInstance getInstance(XGAdress adr)
	{	XGObjectInstance i = this.instances.getFirstValid(adr);
		if(i == null)
			try
			{	this.instances.add(i = new XGObjectInstance(this.device, adr));
			}
			catch(InvalidXGAdressException e)
			{	e.printStackTrace();
			}
		return i;
	}

	public boolean hasInstances()
	{	return this.instances.size() != 0;
	}

	public String getName()
	{	return this.name;
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

	@Override public XGWindow getWindow()
	{	return this.window;
	}

	@Override public void setWindow(XGWindow win)
	{	this.window = win;
	}

	@Override public JComponent getChildWindowContent()
	{	return this.getConfigurationGuiComponents();
	}

	public XMLNode getTemplate()
	{	return null;
	}

	@Override public String getTag()
	{	return this.name;
	}

	public void nodeClicked()
	{	if(this.getWindow() != null) this.getWindow().toFront();
		else new XGWindow(this, XGWindow.getRootWindow(), false, this.getTreePath().toString()); //TODO: nur zum testen, WindowSource evtl. wieder entferenen
	}

	public JComponent getConfigurationGuiComponents()
	{	return new XGFrame(this.name);
	}

	public JTree getTree()
	{	return (JTree)XGWindow.getRootWindow().getRootComponent();
	}

	@Override public boolean isSelected()
	{	return this.isSelected;
	}

	@Override public void setSelected(boolean s)
	{	this.isSelected = s;
	}

}
