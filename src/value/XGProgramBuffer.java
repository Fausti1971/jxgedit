package value;

import java.util.HashMap;
import java.util.Map;
import module.XGDrumsetModuleType;import table.XGDefaultsTable;
/**
* Puffert je Multipart ein normal- und ein drumkit-Program und je Partmode ein drumset-Program und synchronisiert diese bei Änderung
*/
public interface XGProgramBuffer
{
	Map<Integer, Integer> normalPrograms = new HashMap<>();//mp-id (partmode = 0), prog
	Map<Integer, Integer> drumkitPrograms = new HashMap<>();//mp-id (partmode = 1), prog

/**
* Puffert den Wert des Programms (prg.getValue()) in den internen Cache
*/
	static void bufferProgram(XGValue program)
	{	XGValue partmode = program.getModule().getValues().get(XGValueType.MP_PM_VALUE_TAG);

		int pm = partmode.getValue();
		int prg = program.getValue();
		int mp = program.getID();
		switch(pm)
		{	case 0:		normalPrograms.put(mp, prg); break;
			case 1:		drumkitPrograms.put(mp, prg); break;
			default:	XGDrumsetModuleType.DRUMSETS.get(pm).setProgram(prg); break;
		}
	}

/**
* Restauriert den Wert des Programms für den angegebenen Partmode (partmode) aus dem internen Chache
*/
	static void restoreProgram(XGValue partmode)
	{	XGValue prg = partmode.getBulk().getValues().get(XGValueType.MP_PRG_VALUE_TAG);
		int pm = partmode.getValue();
		int mp = prg.getID();
			switch(pm)
			{	case 0:		prg.setValue(normalPrograms.getOrDefault(mp, 0), false, false); break;
				case 1:		prg.setValue(drumkitPrograms.getOrDefault(mp, XGDefaultsTable.DEF_DRUMSETPROGRAM), false, false); break;
				default:	prg.setValue(XGDrumsetModuleType.DRUMSETS.get(pm).getProgram(), false, false); break;
			}
		}

	static void reset()
	{	normalPrograms.clear();
		drumkitPrograms.clear();
	}
}
