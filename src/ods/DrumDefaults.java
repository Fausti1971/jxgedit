package ods;

import com.github.miachm.sods.Sheet;
import table.XGDefaultsTable;
import xml.XMLNode;import xml.XMLNodeConstants;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DrumDefaults
{
	Map<String, XGDefaultsTable> TAB = new HashMap<>();
	int P1_COL = 2, P1_NAME_ROW = 1;

	static void parse(List<Sheet> sheets)
	{	for(Sheet s : sheets)
		{	Object o = s.getDataRange().getCell(0,0).getValue();
			if(o instanceof String)
			{	String prgName = (String)o;
				if(!DrumNames.PRGNAMES.containsKey(prgName))
				{	System.out.println(prgName + " not found! " + DrumNames.PRGNAMES);
					continue;
				}
				int prgNr = DrumNames.PRGNAMES.get(prgName);
				for(int col = P1_COL; col < s.getLastColumn(); col++)
				{	for(int row = 2; row < s.getLastRow(); row++)
					{	o = s.getDataRange().getCell(P1_NAME_ROW, col).getValue();
						Object v = s.getDataRange().getCell(row, col).getValue();
						if(o instanceof String && v instanceof Double)
						{	String parName = ((String)o).trim().replaceAll("\\s+", "");
							int value = ((Double)v).intValue();
							int drumNr = row + 11;
							XGDefaultsTable t = TAB.getOrDefault(parName, new XGDefaultsTable(parName));
							t.put(drumNr,prgNr,value);
							TAB.putIfAbsent(parName, t);
						}
					}
				}
			}
		}
		XMLNode root = new XMLNode(XMLNodeConstants.TAG_DEFAULTSTABLE);
		for(XGDefaultsTable t : TAB.values()) root.addChildNode(t.toXMLNode());
		try
		{	root.save(new File("/home/thomas/Dokumente/Computer/XG/XML/drums.xml"));
		}
		catch(IOException | XMLStreamException e)
		{	e.printStackTrace();
		}
	}
}
