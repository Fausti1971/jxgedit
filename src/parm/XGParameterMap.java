package parm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import adress.InvalidXGAdressException;
import adress.XGAdressableSet;
import application.Rest;

public interface XGParameterMap extends XGParameterConstants
{	static Logger log = Logger.getAnonymousLogger();
	static final File FILE = new File(XML_FILE);
	static Map<String, XGAdressableSet<XGParameter>> parameterSets = new HashMap<>();

	public static XGAdressableSet<XGParameter> getParameterMap(String name)
	{	if(parameterSets.containsKey(name)) return parameterSets.get(name);
		else
		{	XGAdressableSet<XGParameter> m = new XGAdressableSet<>();
			parameterSets.put(name, m);
			return m;
		}
	}

	public static void initParameterMaps()
	{	if(!FILE.canRead())
		{	log.info("can't read file: " + FILE);
			return;
		}
		try
		{	SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setValidating(true);
			SAXParser sp = spf.newSAXParser();
			sp.parse(FILE, new XMLHandler());
		}
		catch(ParserConfigurationException|SAXException | IOException e)
		{	e.printStackTrace();
		}
		return;
	}
/*
	public static void main(String[] args)
	{	getParameterMap("fx1_parameters");
	}
*/
	static class XMLHandler extends DefaultHandler
	{	private Map<String, String> param;
		private String key, value;
		private Set<String> mapNames;
		private XGAdressableSet<XGParameter> set;
	
		@Override public void startDocument() throws SAXException
			{	super.startDocument();
				log.info("parsing started " + FILE);
			}

		@Override public void endDocument() throws SAXException
			{	super.endDocument();
				log.info("parsing finished " + FILE);
			}

		@Override public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{	switch(qName)
			{	case(TAG_MAP):				mapNames = Rest.splitString(attributes.getValue("name"));
											this.set = new XGAdressableSet<>();
											return;
				case(TAG_TRANSLATIONMAP):	param.put(TAG_FILTER, attributes.getValue(TAG_FILTER)); return;
				case(TAG_PARAMETER):		param = new HashMap<>(); return;
				default:					this.key = qName;
			}
		}

		@Override public void endElement(String namespaceURI, String localName, String qName)
		{	switch(qName)
			{	case(TAG_MAP):				if(this.set.size() > 0)
											{	for(String s : mapNames)
												{	parameterSets.put(s, this.set);
													log.info("parameter-map added " + s);
												}
												mapNames = null;
											}
											return;
				case(TAG_PARAMETER):		try
											{	this.set.add(new XGParameter(param));
											}
											catch(InvalidXGAdressException e)
											{	e.printStackTrace();}
											return;
				default:					this.param.put(this.key, this.value);
			}
		}

		@Override public void characters(char[] ch, int start, int length)
		{	this.value = String.copyValueOf(ch, start, length).strip();}
	};
}
