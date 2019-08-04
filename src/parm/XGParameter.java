package parm;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import adress.XGAdress;
import adress.XGAdressable;
import adress.XGAdressableSet;
import application.Rest;
import msg.XGByteArray;
import msg.XGByteArray.DataType;
import obj.XGObjectType;
import value.XGValueTranslator;
import value.XGValueTranslationMap;

public class XGParameter implements XGParameterConstants, XGAdressable
{	static Logger log = Logger.getAnonymousLogger();
	static final File FILE = new File(XML_FILE);
	static final XGAdressableSet<XGParameter> PARAMETERS = new XGAdressableSet<>();
	
	public static XGParameter getParameter(XGAdress adr)
	{	return PARAMETERS.getFirstValidOrDefault(adr, new XGParameter(adr));}

	public static XGAdressableSet<XGParameter> getAllValidParameterSet(XGAdress adr)
	{	return PARAMETERS.getAllValid(adr);}

	public static XGAdressableSet<XGParameter> getAllValidParameterSet(String type)
	{	XGAdressableSet<XGParameter> s = new XGAdressableSet<XGParameter>();
		for(XGParameter p : PARAMETERS) if(p.getObjectType().getName().equals(type)) s.add(p);
		PARAMETERS.addListener(s);
		return s;
	}

	public static void initParameterSet()
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
			while(n.getNextSibling() != null)
			{	if(n.getNodeName().equals(TAG_PARAMETER))
				{	XGParameter p = new XGParameter(n);
					PARAMETERS.add(p);
				}
				n = n.getNextSibling();
			}
		}
		catch (IOException | ParserConfigurationException | SAXException e)
		{ e.printStackTrace();}
	}

/*****************************************************************************************************/

	private final XGObjectType objectType;
	private final XGAdress adress, masterParameterAdress;
	private final int mutableMapIndex, minValue, maxValue, byteCount;
	private final String longName, shortName;
	private final XGValueTranslator valueTranslator;
	private final Map<Integer, String> translationMap;
	private final XGByteArray.DataType byteType;
	private final ValueDataClass valueClass;

	public XGParameter(XGAdress adr)	//automatischer Konstruktor - unbekannter Parameter
	{	this.objectType = XGObjectType.getObjectTypeOrNew(adr);
		this.adress = adr;
		this.minValue = 0;
		this.maxValue = 127;
		this.byteCount = DEF_BYTECOUNT;
		this.mutableMapIndex = 0;
		this.masterParameterAdress = null;
		this.valueTranslator = DEF_TRANSLATOR;
		this.translationMap = null;
		this.byteType = DEF_BYTE_TYPE;
		this.valueClass = DEF_VALUECLASS;
		this.longName = DEF_LONGNAME + " " + adr;
		this.shortName = DEF_SHORTNAME;
	}

	public XGParameter(Node e)
	{	Node n = Rest.getFirstChildNodeByTag(e, TAG_ADRESS);
		this.adress = new XGAdress(n);

		this.objectType = XGObjectType.getObjectTypeOrNew(this.adress);

		String s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_MIN);
		this.minValue = Rest.parseIntOrDefault(s, DEF_MIN);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_MAX);
		this.maxValue = Rest.parseIntOrDefault(s, DEF_MAX);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_LONGNAME);
		this.longName = Rest.getStringOrDefault(s, DEF_LONGNAME);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_SHORTNAME);
		this.shortName = Rest.getStringOrDefault(s, DEF_SHORTNAME);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_BYTECOUNT);
		this.byteCount = Rest.parseIntOrDefault(s, DEF_BYTECOUNT);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_BYTETYPE);
		this.byteType = DataType.valueOf(Rest.getStringOrDefault(s, DEF_BYTE_TYPE.name()));

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_VALUECLASS);
		this.valueClass = ValueDataClass.valueOf(Rest.getStringOrDefault(s, DEF_VALUECLASS.name()));
		
		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_DESCMAPINDEX);
		this.mutableMapIndex = Rest.parseIntOrDefault(s, 0);

		n = Rest.getFirstChildNodeByTag(e,TAG_DEPENDSOFADRESS);
		this.masterParameterAdress = new XGAdress(n);

		s = Rest.getFirstNodeChildTextContentByTagAsString(e, TAG_TRANSLATOR);
		this.valueTranslator = XGValueTranslator.getTranslator(s);

		n = Rest.getFirstChildNodeByTag(e, TAG_TRANSLATIONMAP);
		if(n != null) this.translationMap = XGValueTranslationMap.getTranslationMap(n.getTextContent(), Rest.splitNodesAttributeMap(n, ATTR_FILTER));
		else this.translationMap = null;
	}

	public XGAdress getAdress()
	{	return this.adress;}

	public int getMinValue()
	{	return minValue;}

	public int getMaxValue()
	{	return maxValue;}

	public boolean isLimitizable()
	{	return !this.valueTranslator.equals(XGValueTranslator.translateMap);}

	public XGObjectType getObjectType()
	{	return this.objectType;}

	public XGAdress getMasterParameterAdress()
	{	return this.masterParameterAdress;}

	public int getMutableIndex()
	{	return this.mutableMapIndex;}

	public int getByteCount()
	{	return byteCount;}

	public String getShortName()
	{	return shortName;}

	public String getLongName()
	{	return this.longName;}

	public XGByteArray.DataType getByteType()
	{	return byteType;}

	public ValueDataClass getValueClass()
	{	return valueClass;}

	public XGValueTranslator getValueTranslator()
	{	return valueTranslator;}

	public Map<Integer, String> getTranslationMap()
	{	if(this.translationMap == null) log.info("no translationmap for " + this.longName);
		return this.translationMap;}

	@Override public String toString()
	{	return this.longName;}
}
