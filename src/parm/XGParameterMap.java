package parm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public interface XGParameterMap extends XGParameterConstants
{	static Logger log = Logger.getAnonymousLogger();
	static final File FILE = new File(XML_FILE);
	static Map<String, Map<Integer, XGParameter>> parameterMaps = new HashMap<>();

	public static Map<Integer, XGParameter> getParameterMap(String name)
	{	if(parameterMaps.containsKey(name)) return parameterMaps.get(name);
		else
		{	log.info("new parametermap created: " + name);
			Map<Integer, XGParameter> m = new HashMap<>();
			parameterMaps.put(name, m);
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
	{	private String key, value;
		private String[] mapNames, filters;
		private XGParameter p;
		private Map<Integer, XGParameter> map;
	
		@Override public void startDocument() throws SAXException
			{	super.startDocument();
				log.info("start parsing " + FILE);
			}

		@Override public void endDocument() throws SAXException
			{	super.endDocument();
				log.info("parsing finished " + FILE);
			}

		@Override public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{	if(qName.equals("map"))
			{	mapNames = splitString(attributes.getValue("name"));
				map = new HashMap<>();
				return;
			}
			if(qName.equals(TAG_TRANSLATIONMAP))
			{	filters = splitString(attributes.getValue("filter"));
				return;
			}
			if(qName.equals(TAG_PARAMETER))
			{	p = new XGParameter(0);
				return;
			}
		this.key = qName;
		}

		@Override public void endElement(String namespaceURI, String localName, String qName)
		{	if(qName.equals("map"))
			{	if(this.map.size() > 0)
				{	for(String s : mapNames)
					{	parameterMaps.put(s, this.map);
						log.info("parameter-map added " + s);
					}
					mapNames = null;
				}
				return;
			}
			if(qName.equals(TAG_PARAMETER))
			{	this.map.put(p.getOffset(), p);
				p = null;
			}
			if(qName.equals(this.key) && p != null) p.setParameterProperty(this.key, this.value, filters);
		}

		@Override public void characters(char[] ch, int start, int length)
		{	this.value = String.copyValueOf(ch, start, length).strip();}
		
		private String[] splitString(String s)
		{	if(s == null) return null;
			StringTokenizer t = new StringTokenizer(s, ",");
			String[] a = new String[t.countTokens()];
			for(int i = 0; i < a.length && t.hasMoreElements(); i++) a[i] = t.nextToken();
			return a;
		}
	};
}
