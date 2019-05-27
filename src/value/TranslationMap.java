package value;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public interface TranslationMap extends XGValueConstants
{	static final Logger log = Logger.getAnonymousLogger();
	static final File FILE = new File("rsc/XGTranslationMaps.xml");

	public static Map<Integer, String> getTranslationMap(String name, String... filter)
	{	Map<Integer, String> map = new TreeMap<>();
		if(!FILE.canRead())
		{	log.info("can't read file: " + FILE);
			return map;
		}

		try
		{	SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setValidating(true);
			SAXParser sp = spf.newSAXParser();
			sp.parse(FILE, new XMLHandler(name, map, filter));
		}
		catch(ParserConfigurationException|SAXException | IOException e)
		{	e.printStackTrace();
		}
		return map;
	}

	static class XMLHandler extends DefaultHandler
	{	private String tag;
		private Map<Integer, String> map;
		private String filter[];
		private String value, text, temp;
		private boolean isMyMap = false;

		XMLHandler(String tag, Map<Integer, String> m, String... filter)
		{	this.tag = tag;
			this.map = m;
			this.filter = filter;
		}

		@Override public void startDocument() throws SAXException
		{	super.startDocument();
			log.info("parsing started: " + tag + " in " + FILE);
		}

		@Override public void endDocument() throws SAXException
		{	super.endDocument();
			log.info("parsing finished: " + tag + " in " + FILE);
		}

		@Override public void startElement(String uri,String localName,String qName,Attributes attributes) throws SAXException
		{	if(qName.equals(TAG_MAP) && attributes.getValue("name").equals(tag)) this.isMyMap = true;
		}

		@Override public void endElement(String namespaceURI, String localName, String qName)
		{	if(qName.equals(TAG_MAP) && this.isMyMap) this.isMyMap = false;
			if(qName.equals(TAG_ENTRY) && this.isMyMap) map.put(Integer.parseInt(this.value), this.text);
			if(qName.equals(TAG_VALUE) && this.isMyMap) this.value = this.temp;
			if(qName.equals(TAG_TEXT) && this.isMyMap) this.text = this.temp;
		}

		@Override public void characters(char[] ch, int start, int length)
		{	this.temp = String.copyValueOf(ch, start, length).strip();}
	};
}
