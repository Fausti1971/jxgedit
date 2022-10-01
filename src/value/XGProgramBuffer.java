package value;

import java.util.HashMap;
import java.util.Map;
import adress.InvalidXGAddressException;import static module.XGDrumsetModuleType.*;import static table.XGDefaultsTable.DEF_DRUMSETPROGRAM;import static value.XGValueType.MP_PM_VALUE_TAG;import static value.XGValueType.MP_PRG_VALUE_TAG;
/**
* Puffert je Multipart ein normal- und ein drumkit-Program und je Partmode ein drumsetProgram und synchronisiert diese bei Änderung
*/

public interface XGProgramBuffer
{
	Map<Integer, Integer> normalPrograms = new HashMap<>();//mp-id (partmode = 0), prog
	Map<Integer, Integer> drumkitPrograms = new HashMap<>();//mp-id (partmode = 1), prog

/**
* Puffert den Wert des Programms (prg.getValue()) in den internen Cache
*/
	static void changeProgram(XGValue program)
	{	XGValue partmode = program.getModule().getValues().get(MP_PM_VALUE_TAG);

		int pm = partmode.getValue();
		int prg = program.getValue();
		int mp = program.getAddress().getID();
		switch(pm)
		{	case 0:		normalPrograms.put(mp, prg); break;
			case 1:		drumkitPrograms.put(mp, prg); break;
			default:	DRUMSETS.get(pm).setProgram(prg); break;
		}
//TODO: 

	}

/**
* Restauriert den Wert des Programms für den angegebenen Partmode (partmode) aus dem internen Chache
*/
	static void changePartmode(XGValue partmode)
	{	XGValue prg = partmode.getBulk().getValues().get(MP_PRG_VALUE_TAG);
		int pm = partmode.getValue();
		int mp = prg.getAddress().getMidValue();
			switch(pm)
			{	case 0:		prg.setValue(normalPrograms.getOrDefault(mp, 0), false, false); break;
				case 1:		prg.setValue(drumkitPrograms.getOrDefault(mp, DEF_DRUMSETPROGRAM), false, false); break;
				default:	prg.setValue(DRUMSETS.get(pm).getProgram(), false, false); break;
			}
//TODO: beim sequentiellen Abarbeiten eines Bulks wird erst das Program (Offset 1...3) und danach der Partmode (Offset 7) gesetzt
// und damit das Program zurückgesetzt auf den veralteten Inhalt des Puffers
		}

	static void reset()
	{	normalPrograms.clear();
		drumkitPrograms.clear();
	}
}
