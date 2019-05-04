package obj;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public interface XGObjectDescriptionSet extends XGObjectConstants
{	static Logger log = Logger.getAnonymousLogger();
	static final File FILE = new File(XML_FILE);

	public static Set<XGObjectDescription> getObjectDescriptionSet()
	{	Set<XGObjectDescription> set = new HashSet<>();
	
		if(!FILE.canRead())
		{	log.info("can't read file: " + FILE);
			return set;
		}
	
		try
		{	SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setValidating(true);
			SAXParser sp = spf.newSAXParser();
			log.info("validating=" + sp.isValidating());
			sp.parse(FILE, new XMLHandler(set));
		}
		catch(ParserConfigurationException|SAXException | IOException e)
		{	e.printStackTrace();
		}
		return set;
	}
	
	public static void main(String[] args)
	{	log.info("" + getObjectDescriptionSet());}
	
	static class XMLHandler extends DefaultHandler
	{	private boolean mapTagIsOpened = false, entryTagIsOpened = false;;
		private String tag, key, value;
		private Set<XGObjectDescription> ods;
		private Set<String> names = new HashSet<>();
		private Map<String, String> entry = new HashMap<>();
	
		XMLHandler(Set<XGObjectDescription> s)
		{	this.ods = s;}
	
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
			{	this.mapTagIsOpened = true;
				return;
			}
			if(this.mapTagIsOpened && (qName.equals(TAG_OBJECT)))
			{	this.entryTagIsOpened = true;
				this.entry.clear();
				return;
			}
			if(this.mapTagIsOpened && this.entryTagIsOpened)
			{	this.key = qName;
			}
		}
	
		@Override public void endElement(String namespaceURI, String localName, String qName)
		{	if(qName.equals("map") && this.mapTagIsOpened)
			{	for(String s : names) CACHE.put(s, this.map);
				this.mapTagIsOpened = false;
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
		{	if(!this.mapTagIsOpened) return;
			if(!this.entryTagIsOpened) return;
			this.value = String.copyValueOf(ch, start, length).strip();
		}
	};
}
