package parm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressableSet;

public interface XGParameterMap extends XGParameterConstants
{	static Logger log = Logger.getAnonymousLogger();
	static final File FILE = new File(XML_FILE);
	static XGAdressableSet<XGParameter> parameterSet = new XGAdressableSet<>();

	public static XGAdressableSet<XGParameter> getParameterSet(String name)
	{	return parameterSet;}

	public static XGParameter getParameter(XGAdress adr)
	{	return parameterSet.getValid(adr);}
	
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
			{	case(TAG_MAP):				return;
				case(TAG_TRANSLATIONMAP):	param.put(TAG_FILTER, attributes.getValue(TAG_FILTER)); return;
				case(TAG_PARAMETER):		param = new HashMap<>(); return;
				default:					this.key = qName;
			}
		}

		@Override public void endElement(String namespaceURI, String localName, String qName)
		{	switch(qName)
			{	case(TAG_MAP):				return;
				case(TAG_PARAMETER):		try
											{	parameterSet.add(new XGParameter(param));
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
