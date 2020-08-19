package parm;
import java.io.File;
import java.io.FileNotFoundException;
import application.XGLoggable;
import device.XGDevice;
import tag.XGTagable;
import xml.XMLNode;

public class XGParameter implements XGLoggable, XGParameterConstants, XGTagable
{
	public static void init(XGDevice dev)
	{	File file;
		try
		{	file = dev.getResourceFile(XML_MODULE);
		}
		catch(FileNotFoundException e)
		{	LOG.info(e.getMessage());
			return;
		}

		XMLNode xml = XMLNode.parse(file);
		XMLNode t = xml.getChildNode(TAG_PARAMETERS);
		for(XMLNode p : t.getChildNodes(TAG_PARAMETER)) dev.getParameters().add(new XGParameter(dev, p));

		LOG.info(dev.getParameters().size() + " parameters initialized");
		return;
	}

/******************************************************************************************************************/

	private final String tag;
	private final String longName, shortName;
	private final int origin;
	private final XGTable translationTable;
	private final String unit;

	protected XGParameter(XGDevice dev, XMLNode n)
	{	this.tag = n.getStringAttribute(ATTR_ID).toString();
		this.translationTable = dev.getTables().getOrDefault(n.getStringAttribute(ATTR_TRANSLATOR).toString(), XGVirtualTable.DEF_TABLE).filter(n);// muss wegen validate() vor origin-Zuweisung ausgeführt werden; ist origin evtl. besser im Component aufgehoben? (template.xml)
		this.origin = this.validate(this.translationTable.getIndex(n.getIntegerAttribute(ATTR_ORIGIN, 0)));
		this.longName = n.getStringAttribute(ATTR_LONGNAME).toString();
		this.shortName = n.getStringAttribute(ATTR_SHORTNAME).toString();
		this.unit = n.getStringAttribute(ATTR_UNIT, this.translationTable.getUnit()).toString();
		LOG.info("parameter initialized: " + this);
		if(this.translationTable == null) throw new RuntimeException("no table: " + this.toString());
	}

	public XGParameter(String name, int v)//Dummy-Parameter für Festwerte
	{	this.tag = name;
		this.longName = DEF_PARAMETERNAME;
		this.shortName = name;
		this.translationTable = XGVirtualTable.DEF_TABLE;
		this.origin = 0;
		this.unit = "*";
		LOG.info("parameter initialized: " + this);
		if(this.translationTable == null) throw new RuntimeException("no table: " + this.toString());
	}

	public int getMinIndex()
	{	return this.translationTable.getMinIndex();
	}

	public int getMaxIndex()
	{	return this.translationTable.getMaxIndex();
	}

	public XGTable getTranslationTable()
	{	return this.translationTable;
	}

	public int getOrigin()
	{	return this.origin;
	}

	public int validate(int i)
	{	return Math.max(Math.min(i, this.getMaxIndex()), this.getMinIndex());
	}

	public String getShortName()
	{	return this.shortName;
	}

	public String getLongName()
	{	return this.longName;
	}

	public String getUnit()
	{	return this.unit;
	}

	@Override public String toString()
	{	return this.longName;
	}

	@Override public String getTag()
	{	return this.tag;
	}
}
