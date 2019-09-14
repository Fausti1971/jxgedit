package parm;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import adress.XGAdress;
import adress.XGAdressableSet;
import msg.XGByteArray;

public class XGParameterStorage implements XGParameterConstants
{	static Logger log = Logger.getAnonymousLogger();
	static final XGParameter XGMODELNAMEPARAMETER = new XGParameter(XGMODELNAMEADRESS, 32, 127, 13, XGByteArray.DataType.MIDIBYTE, ValueDataClass.String);

/*****************************************************************************************************/

	final File xml;
	private final XGAdressableSet<XGParameter> parameters = new XGAdressableSet<>();

	public XGParameterStorage(String filename)
	{	this.xml = new File(filename);
		this.parameters.add(XGMODELNAMEPARAMETER);
		this.initParameterSet();
	}

	public XGParameter getParameter(XGAdress adr)
	{	XGParameter p = parameters.getFirstValid(adr);
		if(p == null)
		{	//log.info("no valid parameter found: " + adr);
			return new XGParameter(adr);
		}
		return p;
	}
	
	public XGAdressableSet<XGParameter> getAllValidParameterSet(XGAdress adr)
	{	return parameters.getAllValid(adr);}
	
	public XGAdressableSet<XGParameter> getAllValidParameterSet(String type)
	{	XGAdressableSet<XGParameter> s = new XGAdressableSet<XGParameter>();
		for(XGParameter p : parameters) if(p.getObjectType().getName().equals(type)) s.add(p);
		parameters.addListener(s);
		return s;
	}
	
	private void initParameterSet()
	{	if(!this.xml.canRead())
		{	log.info("can't read file: " + this.xml);
			return;
		}
	
		try
		{	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	//		dbf.setValidating(true);
	//		dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, null);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(this.xml); 
	
			Element rootElement = doc.getDocumentElement(); 
	
			Node n = rootElement.getFirstChild();
			while(n.getNextSibling() != null)
			{	if(n.getNodeName().equals(TAG_PARAMETER))
				{	XGParameter p = new XGParameter(n);
					parameters.add(p);
				}
				n = n.getNextSibling();
			}
		}
		catch (IOException | ParserConfigurationException | SAXException e)
		{ e.printStackTrace();}
	}
}
