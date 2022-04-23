package table;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import application.*;
import parm.XGParameterConstants;
import tag.*;
import xml.XMLNode;import static xml.XMLNodeConstants.TAG_TABLE;import static xml.XMLNodeConstants.XML_TABLES;

public interface XGTable extends XGLoggable, XGTableConstants, XGTagable, Iterable<XGTableEntry>
{
	Set<Integer> FX_MSB_PROGRAMS = new HashSet<>();//wird während der XGRealTable-Initialisierung durch XGEntry (mit programmvalues, die das Attribut address="word" besitzen) befüllt;
	XGTagableSet<XGTable> TABLES = new XGTagableSet<>();
	int DEF_FALLBACKMASK = 127;

	static void init()
	{	try
		{	XMLNode xml = XMLNode.parse(JXG.getDeviceXMLResourceFile(XML_TABLES));
			for(XMLNode x : xml.getChildNodes(TAG_TABLE))
			{	TABLES.add(new XGRealTable(x));
			}
		}
		catch(IOException e)
		{	LOG.severe(e.getMessage());
		}
		XGRealTable t = new XGRealTable(TABLE_FX_PARTS);//wird zur Laufzeit um die Multipart- und ADPart-Instanzen erweitert
		t.add(new XGTableEntry(127, "Off"));
		TABLES.add(t);

		t = new XGRealTable(TABLE_PARTMODE);//wird bei XGDrumsetModuleType.init() um die Drumsets erweitert
		t.add(new XGTableEntry(0, "Normal"));
		t.add(new XGTableEntry(1, "Drum"));
		TABLES.add(t);

		XGVirtualTable.init();
		}

/*************************************************************************************************************/

	XGTableEntry getByIndex(int i);
	XGTableEntry getByValue(int v);
	XGTableEntry getByName(String name) throws NumberFormatException;
/**
* returniert den index des gesuchten values v, im Falle eines Fehlschlags noch einmal mit fallbackmask, ansonsten def
*/
	int getIndex(int value);
	int getIndex(String name) throws NumberFormatException;
	int getMinIndex();
	int getMaxIndex();
	XGTable categorize(String cat);
	Set<String> getCategories();
	XGTable filter(XMLNode n);
	String getName();
	String getUnit();
	int size();

	default XGTableEntry getMinEntry(){	return this.getByIndex(this.getMinIndex());}

	default XGTableEntry getMaxEntry(){	return this.getByIndex(this.getMaxIndex());}

	@Override default String getTag(){	return this.getName();}

	default String getInfo()
	{	return this.getClass().getSimpleName() + " (" + this.getName() + "(" + this.size() + "): " + this.getMinEntry().getInfo() + "..." + this.getMaxEntry().getInfo() + ")";
	}
}
