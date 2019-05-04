package parm;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
//TODO statische Maps entfernen und online parsen; filterTags als attibutes einfügen (mu80, rev, cho, var, ins...)
public interface TranslationMap
{	static final File FILE = new File("rsc/XGTranslationMaps.xml");

	public static Map<Integer, String> getTranslationMap(String name, String... filter)
	{	Map<Integer, String> map = new TreeMap<>();
		if(!FILE.canRead())
		{	System.out.println("can't read file: " + FILE);
			return map;
		}

		try
		{	SAXParserFactory.newInstance().newSAXParser().parse(FILE, new XMLHandler(name, map, filter));
		}
		catch(ParserConfigurationException|SAXException | IOException e)
		{	e.printStackTrace();
		}
		return map;
	}

	static class XMLHandler extends DefaultHandler
	{	private boolean mapTagIsOpened = false;
		private String entryName = "";
		private String tag;
		private Map<Integer, String> map;
		private String filter[];
	
		XMLHandler(String tag, Map<Integer, String> m, String... filter)
		{	this.tag = tag;
			this.map = m;
			this.filter = filter;
		}

		@Override public void startElement(String uri,String localName,String qName,Attributes attributes) throws SAXException
		{	if(qName.equals("map") && attributes.getValue("name").equals(this.tag)) this.mapTagIsOpened = true;
			if(this.mapTagIsOpened)
			{	if(qName.equals("entry")) entryName = attributes.getValue("name");
			}
		}

		@Override public void endElement(String namespaceURI, String localName, String qName)
		{	if(qName.equals("map")) this.mapTagIsOpened = false;
		}

		@Override public void characters(char[] ch, int start, int length)
		{	if(!this.mapTagIsOpened) return;
			String number = String.copyValueOf(ch, start, length).strip();
			if(number.isEmpty()) return;
			try
			{	map.put(Integer.parseInt(number, 10), entryName);} 
			catch(NumberFormatException e)
			{	e.printStackTrace();}
			System.out.println("char: " + number);
		}
	};
}
