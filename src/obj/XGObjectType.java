package obj;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressConstants;
import adress.XGAdressableSet;
import application.ConfigurationConstants;
import device.XGDevice;
import msg.XGMessenger;
import value.XGValue;
import xml.XMLNode;

public class XGObjectType implements ConfigurationConstants, XGObjectConstants, XGAdressConstants
{	private static Logger log = Logger.getAnonymousLogger();

	private static Map<XGDevice, Set<XGObjectType>> STORAGE = new HashMap<>();

	public static void init(XGDevice dev)
	{	Set<XGObjectType> set = new HashSet<>();
		File file = dev.getResourceFile(XML_OBJECT);
		try
		{	XMLNode xml = XMLNode.parse(file);
			for(XMLNode x : xml.getChildren())
			{	if(x.getTag().equals(TAG_OBJECT))
				{	XGObjectType t = new XGObjectType(dev, x);
				set.add(t);
				}
			}
		}
		catch(XMLStreamException e1)
		{	e1.printStackTrace();
		}
		STORAGE.put(dev, set);
		log.info(set.size() + " object-types initialized");
	}

	public static Set<XGObjectType> getAllObjectTypes(XGDevice dev)
	{	return STORAGE.get(dev);
	}
	
	public static XGObjectType getObjectTypeOrNew(XGDevice dev, XGAdress adr)
	{	for(XGObjectType t : getAllObjectTypes(dev))
		{	if(t.include(adr)) return t;
		}
		return new XGObjectType(dev, adr);
	}
	
	public static XGObjectType getObjectType(XGDevice dev, String name)
	{	for(XGObjectType t : getAllObjectTypes(dev)) if(t.getName().equals(name)) return t;
		return new XGObjectType(dev, INVALIDADRESS);
	}
	
	public static void requestAll(XGMessenger src, XGMessenger dest)
	{	for(XGObjectType o : getAllObjectTypes(src.getDevice()))
			for(XGBulkDumpSequence s : o.getDumpsequences()) s.requestAll(src, dest);
		log.info("request completetd");
	}

/**********************************************************************************************************/

	private final XGDevice device;
	private final String name;
	private final Set<XGBulkDumpSequence> bulks;
	private final XGAdressableSet<XGObjectInstance> instances = new XGAdressableSet<XGObjectInstance>();

	XGObjectType(XGDevice dev, XGAdress adr)
	{	this(dev, DEF_OBJECTTYPENAME, null);
	}

	private XGObjectType(XGDevice dev, String name, Set<XGBulkDumpSequence> dseq)
	{	this.device = dev;
		this.name = name;
		this.bulks = dseq;
		log.info("object initialized: " + this);
	}

	XGObjectType(XGDevice dev, XMLNode n)
	{	this(dev, n.getChildNode(TAG_NAME).getTextContent(), new HashSet<>());

		for(XMLNode seq : n.getChildren())
			if(seq.getTag().equals(TAG_DUMPSEQ)) this.bulks.add(new XGBulkDumpSequence(seq));
	}

	public Set<XGBulkDumpSequence> getDumpsequences()
	{	return this.bulks;}

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
		{	this.instances.add(new XGObjectInstance(this.device, v.getAdress()));}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
	}

	public XGAdressableSet<XGObjectInstance> getXGObjectInstances()
	{	return this.instances;}

	public XGObjectInstance getInstance(XGAdress adr)
	{	XGObjectInstance i = this.instances.getFirstValid(adr);
		if(i == null)
			try
			{	this.instances.add(i = new XGObjectInstance(this.device, adr));}
			catch(InvalidXGAdressException e)
			{	e.printStackTrace();}
		return i;
	}

	public boolean hasInstances()
	{	return this.instances.size() != 0;}

	public String getName()
	{	return this.name;}

	@Override public String toString()
	{	return this.name;}
}
