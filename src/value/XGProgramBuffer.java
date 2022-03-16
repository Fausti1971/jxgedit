package value;

import java.util.HashMap;
import java.util.Map;
import adress.InvalidXGAddressException;import module.XGDrumsetModuleType;import static module.XGDrumsetModuleType.*;import module.XGModule;import static parm.XGDefaultsTable.DEF_DRUMSETPROGRAM;import tag.XGTagableAddressableSet;import tag.XGTagableSet;import static value.XGValueType.MP_PM_VALUE_TAG;import static value.XGValueType.MP_PRG_VALUE_TAG;
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
	{	XGValue partmode = program.getBulk().getValues().get(MP_PM_VALUE_TAG);
		int pm = partmode.getValue();
		int prg = program.getValue();
		try
		{	int mp = program.getAddress().getMid().getValue();
			switch(pm)
			{	case 0:		normalPrograms.put(mp, prg); break;
				case 1:		drumkitPrograms.put(mp, prg); break;
				default:	DRUMSETS.get(pm).setProgram(prg); break;
			}
		}
		catch(InvalidXGAddressException e){	e.printStackTrace();}
	}

/**
* Restauriert den Wert des Programms für den angegebenen Partmode (partmode) aus dem internen Chache
*/
	static void changePartmode(XGValue partmode)
	{	XGValue prg = partmode.getBulk().getValues().get(MP_PRG_VALUE_TAG);
		int pm = partmode.getValue();
		try
		{	int mp = prg.getAddress().getMid().getValue();
			switch(pm)
			{	case 0:		prg.setValue(normalPrograms.getOrDefault(mp, 0), false, false); break;
				case 1:		prg.setValue(drumkitPrograms.getOrDefault(mp, DEF_DRUMSETPROGRAM), false, false); break;
				default:	prg.setValue(DRUMSETS.get(pm).getProgram(), false, false); break;
			}
		}
		catch(InvalidXGAddressException e){	e.printStackTrace();}
	}

	static void reset()
	{	normalPrograms.clear();
		drumkitPrograms.clear();
	}
}
