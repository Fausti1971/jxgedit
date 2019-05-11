package parm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
		{	log.info("parameter map not found: " + name);
			return null;
		}
	}

	public static void initParameterMaps()
	{	String name;

		if(!FILE.canRead())
		{	log.info("can't read file: " + FILE);
			return;
		}

		try
		{	SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setValidating(true);
			SAXParser sp = spf.newSAXParser();
			log.info("validating=" + sp.isValidating());
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
	{	private boolean entryTagIsOpened = false;;
		private String tag, key, value;
		private Set<String> mapNames= new HashSet<>();
		private Map<String, String> entry = new HashMap<>();
		private Map<Integer, XGParameter> map;
	
		XMLHandler()
		{	this.tag = tag;
//			this.map = m;
		}

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
			{	StringTokenizer t = new StringTokenizer(attributes.getValue("name"), ",");
				while(t.hasMoreElements()) mapNames.add(t.nextToken());
				map = new HashMap<>();
				return;
			}
			if(qName.equals(TAG_PARAMETER) || qName.equals(TAG_OPCODE) || qName.equals(TAG_DESCRIPTION))
			{	this.entryTagIsOpened = true;
				this.entry.clear();
				return;
			}
			if(this.entryTagIsOpened)
			{	this.key = qName;
			}
		}

		@Override public void endElement(String namespaceURI, String localName, String qName)
		{	if(qName.equals("map"))
			{	if(this.map.size() > 0)
				{	for(String s : mapNames)
					{	parameterMaps.put(s, this.map);
						log.info("parameter-map added " + s);
					}
				}
				return;
			}
			if(qName.equals(TAG_DESCRIPTION) && this.entryTagIsOpened)
			{	XGParameter p = new XGParameter(new XGParameterDescription(Integer.parseInt(this.entry.get(TAG_OFFSET)), Integer.parseInt(this.entry.get(TAG_MIN)), Integer.parseInt(this.entry.get(TAG_MAX)),
					this.entry.get(TAG_LONGNAME), this.entry.get(TAG_SHORTNAME), this.entry.get(TAG_TRANSLATOR), this.entry.get(TAG_TRANSLATIONMAP), this.entry.get(TAG_TRANSLATIONMAPFILTER)));;
				this.map.put(Integer.parseInt(this.entry.get(TAG_OFFSET)), p);
				this.entry.clear();
				this.entryTagIsOpened = false;
			}
			if(qName.equals(TAG_OPCODE) && this.entryTagIsOpened)
			{	XGParameter p = new XGParameter(new XGParameterOpcode( Integer.parseInt(this.entry.get(TAG_OFFSET)), Integer.parseInt(this.entry.get(TAG_BYTECOUNT)), this.entry.get(TAG_VALUETYPE),
					this.entry.get(TAG_BYTETYPE)), Integer.parseInt(this.entry.get(TAG_DEPENDSOF), 16), Integer.parseInt(this.entry.get(TAG_DESCMAPINDEX)));
				this.map.put(p.getOffset(), p);
				this.entry.clear();
				this.entryTagIsOpened = false;
			}
			if(qName.equals(TAG_PARAMETER) && this.entryTagIsOpened)
			{	XGParameter p = new XGParameter(new XGParameterOpcode(Integer.parseInt(this.entry.get(TAG_OFFSET)), Integer.parseInt(this.entry.get(TAG_BYTECOUNT)), this.entry.get(TAG_VALUETYPE), this.entry.get(TAG_BYTETYPE)),
					new XGParameterDescription(Integer.parseInt(this.entry.get(TAG_OFFSET)), Integer.parseInt(this.entry.get(TAG_MIN)), Integer.parseInt(this.entry.get(TAG_MAX)), this.entry.get(TAG_LONGNAME),
						this.entry.get(TAG_SHORTNAME), this.entry.get(TAG_TRANSLATOR), this.entry.get(TAG_TRANSLATIONMAP), this.entry.get(TAG_TRANSLATIONMAPFILTER)));
				this.map.put(p.getOffset(), p);
				this.entry.clear();
				this.entryTagIsOpened = false;
			}
			if(qName.equals(this.key)) this.entry.put(this.key, this.value);
		}

		@Override public void characters(char[] ch, int start, int length)
		{	if(!this.entryTagIsOpened) return;
			this.value = String.copyValueOf(ch, start, length).strip();
		}
	};
}
