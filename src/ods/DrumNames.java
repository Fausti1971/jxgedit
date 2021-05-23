package ods;

import application.XGStrings;import com.github.miachm.sods.Sheet;import parm.XGDrumNames;import parm.XGRealTable;
import java.util.*;

public interface DrumNames
{	Map<String, Integer> PRGNAMES = new HashMap<>();

	XGDrumNames NAMES = new XGDrumNames();
	int KEY_COL = 0;
	int MSB_ROW = 0;
	int LSB_ROW = 1;
	int PRG_ROW = 2;
	int PRGNAME_ROW = 3;
	Set<Integer> DONE = new HashSet<>();

	static void parse(List<Sheet> sheets)
	{	for(Sheet s : sheets)
		{	for(int col = 0; col < s.getLastColumn(); col++)
			{	Object m = s.getRange(MSB_ROW, col).getValue();
				Object l = s.getRange(LSB_ROW, col).getValue();
				Object p = s.getRange(PRG_ROW, col).getValue();
				Object n = s.getRange(PRGNAME_ROW, col).getValue();
				if(m instanceof Double && l instanceof Double && p instanceof Double && n instanceof String)
				{	int msb = ((Double)m).intValue();
					int lsb = ((Double)l).intValue();
					int prg = ((Double)p).intValue() - 1;
					int sum = (msb << 14) + (lsb <<7) + prg;
					String prgName = (String)n;
					PRGNAMES.put(prgName, sum);
					if(DONE.contains(sum)) continue;
					for(int row = 0; row < s.getLastRow(); row++)
					{	Object k = s.getRange(row, KEY_COL).getCell(0,0).getValue();
						if(k instanceof Double)
						{	int key = ((Double)k).intValue();
							n = s.getRange(row, col).getCell(0,0).getValue();
//							System.out.println("s=" + s.getName() + ", k="+ key + " " + row + "/" + col + " (" + n + ")");
							if(n instanceof String)
							{	NAMES.put(key, sum, (String)n);
								DONE.add(sum);
							}
						}
					}
				}
			}
		}
//		NAMES.print();
		System.out.println("ProgramCount=" + PRGNAMES.size());
	}
}
