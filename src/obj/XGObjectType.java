package obj;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressConstants;
import adress.XGAdressableSet;
import application.Rest;
import value.XGValue;

public class XGObjectType implements XGObjectConstants, XGAdressConstants
{	private static Logger log = Logger.getAnonymousLogger();
	private static final File FILE = new File(XML_FILE);
	private static final Set<XGObjectType> OBJECTTYPES = new HashSet<>();

	public static XGObjectType getObjectTypeOrNew(XGAdress adr)
	{	for(XGObjectType t : OBJECTTYPES)
		{	if(t.include(adr)) return t;}
		return new XGObjectType(adr);
	}

	public static XGObjectType getObjectType(String name)
	{	for(XGObjectType t : OBJECTTYPES) if(t.getName().equals(name)) return t;
		return new XGObjectType(INVALIDADRESS);
	}

	public static void initObjectTypeMap()
	{	if(!FILE.canRead())
		{	log.info("can't read file: " + FILE);
			return;
		}
	
		try
		{	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	//		dbf.setValidating(true);
	//		dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, null);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(FILE); 
	
			Element rootElement = doc.getDocumentElement(); 
	
			Node n = rootElement.getFirstChild();
			while(n != null)
			{	if(n.getNodeName().equals(TAG_OBJECT))
				{	XGObjectType t = new XGObjectType(n);
					OBJECTTYPES.add(t);
				}
				n = n.getNextSibling();
			}
		}
		catch (IOException | ParserConfigurationException | SAXException e)
		{	e.printStackTrace();}
	}

/******************************************************************************************************************/

	private final String name;
	private final Set<XGBulkDumpSequence> dumpSequences;
	private final XGAdressableSet<XGObjectInstance> instances = new XGAdressableSet<XGObjectInstance>();

	private XGObjectType(XGAdress adr)
	{	this(DEF_OBJECTTYPENAME, null);
	}

	private XGObjectType(String name, Set<XGBulkDumpSequence> dseq)
	{	this.name = name;
		this.dumpSequences = dseq;
	}

	private XGObjectType(Node n)
	{	this(Rest.getFirstNodeChildTextContentByTagAsString(n, TAG_NAME), new HashSet<>());

		Node seq = Rest.getFirstChildNodeByTag(n, TAG_DUMPSEQ);
		while(seq != null)
		{	if(seq.getNodeName().equals(TAG_DUMPSEQ)) this.dumpSequences.add(new XGBulkDumpSequence(seq));
			seq = seq.getNextSibling();
		}
	}

	public Set<XGBulkDumpSequence> getDumpsequences()
	{	return this.dumpSequences;}

	public boolean include(XGAdress adr)
	{	for(XGBulkDumpSequence s : this.dumpSequences) if(s.include(adr)) return true;
		return false;
	}

	public void addInstance(XGValue v)
	{	if(this.instances.contains(v.getAdress())) return;
		try
		{	this.instances.add(new XGObjectInstance(v.getAdress()));}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
	}

	public XGAdressableSet<XGObjectInstance> getXGObjectInstances()
	{	return this.instances;}

	public XGObjectInstance getInstance(XGAdress adr)
	{	XGObjectInstance i = this.instances.getFirstValid(adr);
		if(i == null)
			try
			{	this.instances.add(i = new XGObjectInstance(adr));}
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
