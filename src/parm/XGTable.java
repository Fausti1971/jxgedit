package parm;

import java.io.*;
import java.util.Set;
import application.*;
import tag.*;
import xml.XMLNode;

public interface XGTable extends ConfigurationConstants, XGLoggable, XGParameterConstants, XGTagable, Iterable<XGTableEntry>
{
	XGTagableSet<XGTable> TABLES = new XGTagableSet<>();
	int DEF_FALLBACKMASK = 127;
	enum Preference{BELOW, EQUAL, ABOVE, CLOSEST, FALLBACK};

	static void init()
	{	try
		{	XMLNode xml = XMLNode.parse(JXG.getResourceStream(XMLPATH + XML_TABLES));
			for(XMLNode x : xml.getChildNodes(TAG_TABLE))
			{	TABLES.add(new XGRealTable(x));
			}
		}
		catch(IOException e)
		{	LOG.severe(e.getMessage());
		}
		XGRealTable t = new XGRealTable(XGParameterConstants.TABLE_FX_PARTS);
		t.add(new XGTableEntry(127, "Off"));//TODO: zum Zeitpunkt der Parameter-Instanziierung ist die Size der Table 1 (minIndex=0, maxIndex=0); vielleicht muss doch bei fehlendem XML-Attribut "min" und "max" die min- und max-Values (-Indizes) der TranslationTable herhalten (DEF_MIN und DEF_MAX löschen)
		TABLES.add(t);
System.out.println(t);

		t = new XGRealTable(XGParameterConstants.TABLE_PARTMODE);//wird bei XGDrumsetModuleType.init() um die Drumsets erweitert
		t.add(new XGTableEntry(0, "Normal"));
		t.add(new XGTableEntry(1, "Drum"));
		TABLES.add(t);
System.out.println(t);


		XGVirtualTable.init();
		}

/*************************************************************************************************************/

	XGTableEntry getByIndex(int i);
	XGTableEntry getByValue(int v);
	XGTableEntry getByName(String name);
	int getIndex(int v, Preference pref);
	int getIndex(String name);
	XGTable categorize(String cat);
	Set<String> getCategories();
	XGTable filter(XMLNode n);
	String getName();
	String getUnit();
	int size();

	default XGTableEntry getMinEntry()
	{	return this.getByIndex(this.getMinIndex());
	}

	default XGTableEntry getMaxEntry()
	{	return this.getByIndex(this.getMaxIndex());
	}

	@Override default String getTag()
	{	return this.getName();
	}

	default String getInfo()
	{	return this.getClass().getSimpleName() + ": " + this.getName() + "(" + this.size() + "): " + this.getMinEntry().getInfo() + "..." + this.getMaxEntry().getInfo();
	}

	default int getMinIndex()
	{	return 0;
	}

	default int getMaxIndex()
	{	return this.size() - 1;
	}
}
