package value;

import java.util.HashMap;
import java.util.Map;
import adress.InvalidXGAddressException;import module.XGDrumsetModuleType;import module.XGModule;import static parm.XGDefaultsTable.DEF_DRUMSETPROGRAM;import tag.XGTagableAddressableSet;import tag.XGTagableSet;
/**
* Puffert je Multipart ein normal- und ein drumkit-Program und je Partmode ein drumsetProgram und synchronisiert diese bei Änderung
*/

public interface XGProgramBuffer
{
	Map<Integer, Integer> normalPrograms = new HashMap<>();//mp-id (partmode = 0), prog
	Map<Integer, Integer> drumkitPrograms = new HashMap<>();//mp-id (partmode = 1), prog
//	Map<Integer, Integer> drumsetPrograms = new HashMap<>();//partmode (> 1), prog
//	Map<Integer, XGDrumsetProgramListener> drumsetListeners = new HashMap<>();//partmode, listener

/**
* Puffert den Wert des Programms (prg.getValue()) in den internen Cache
*/
	static void changeProgram(XGValue program)
	{	XGValue partmode = program.getModule().getValues().get("mp_partmode");
		int pm = partmode.getValue();
		int prg = program.getValue();
		try
		{	int mp = program.getAddress().getMid().getValue();
			switch(pm)
			{	case 0:		normalPrograms.put(mp, prg); break;
				case 1:		drumkitPrograms.put(mp, prg); break;
				default:	XGDrumsetModuleType.DRUMSETS.get(pm).setProgram(prg); break;
			}
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
		}
//	throw new RuntimeException(program.toString());
	}

/**
* Restauriert den Wert des Programms für den angegebenen Partmode (v) aus dem internen Chache
*/
	static void changePartmode(XGValue partmode)
	{	XGValue prg = partmode.getModule().getValues().get("mp_program");
		int pm = partmode.getValue();
		try
		{	int mp = prg.getAddress().getMid().getValue();
			switch(pm)
			{	case 0:		prg.setValue(normalPrograms.getOrDefault(mp, 0)); break;
				case 1:		prg.setValue(drumkitPrograms.getOrDefault(mp, DEF_DRUMSETPROGRAM)); break;
				default:	prg.setValue(XGDrumsetModuleType.DRUMSETS.get(pm).getProgram()); break;
			}
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
		}
	}

	static void reset()
	{	normalPrograms.clear();
		drumkitPrograms.clear();
//		XGDrumsetModuleType.getM;
	}
}
