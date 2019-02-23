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

public interface TranslationMap
{	static final File FILE = new File("rsc/Maps.xml");

	public static Map<Integer, String> channelMap = initMap("midi_channels");

	public static Map<Integer, String> initMap(String tag)
	{	Map<Integer, String> map = new TreeMap<>();
		if(!FILE.canRead())
		{	System.out.println("cant read file: " + FILE);
			return map;
		}

		try
		{	SAXParserFactory.newInstance().newSAXParser().parse(FILE, new XMLHandler(tag, map));
		}
		catch(ParserConfigurationException|SAXException | IOException e)
		{	e.printStackTrace();
		}
		return map;
	}

	static class XMLHandler extends DefaultHandler
	{	private boolean tagIsOpened = false;
		private String entryName = "";
		private String tag;
		private Map<Integer, String> map;
	
		XMLHandler(String tag, Map<Integer, String> m)
		{	this.tag = tag;
			this.map = m;
		}

		@Override public void startElement(String uri,String localName,String qName,Attributes attributes) throws SAXException
		{	if(qName.equals(tag)) tagIsOpened = true;
			if(tagIsOpened)
			{	if(qName.equals("entry")) entryName = attributes.getValue("name");
			}
		}

		@Override public void endElement(String namespaceURI, String localName, String qName)
		{	if(qName.equals(tag)) tagIsOpened = false;
		}

		@Override public void characters(char[] ch, int start, int length)
		{	if(!tagIsOpened) return;
			int value = 0;
			String number = String.copyValueOf(ch, start, length);
			try
			{	value = Integer.parseInt(number, 10);
				map.put(value, entryName);
			} 
			catch(NumberFormatException e)
			{
			}
		}
	};
}
