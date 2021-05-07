package value;

import java.util.HashMap;
import java.util.Map;import java.util.Set;
import adress.InvalidXGAddressException;import static application.XGLoggable.LOG;import device.XGDevice;import module.XGDrumsetModuleType;import module.XGModule;import static parm.XGDefaultsTable.DEF_DRUMSETPROGRAM;

public interface XGProgramBuffer
{
	final Map<Integer, Integer> normalPrograms = new HashMap<>();//mp-id (partmode = 0), prog
	final Map<Integer, Integer> drumKitPrograms = new HashMap<>();//mp-id (partmode = 1), prog
	final Map<Integer, Integer> drumsets = new HashMap<>();//partmode (> 1), prog

/**
* Puffert den Wert des Programms (v) in den internen Cache
*/
	static void bufferProgram(XGValue prg)
	{	int pm = prg.getModule().getValues().get("mp_partmode").getValue();
		int prgV = prg.getValue();
		try
		{	int mp = prg.getAddress().getMid().getValue();
			switch(pm)
			{	case 0:		normalPrograms.put(mp, prgV); break;
				case 1:		drumKitPrograms.put(mp, prgV); break;
				default:	drumsets.put(pm, prgV);
							synchronizeDrumset(pm, prg); break;
			}
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
		}
	}

/**
* Restauriert den Wert des Programms für den angegebenen Partmode (v) aus dem internen Chache
*/
	static void restoreProgram(XGValue pm)
	{	XGValue prg = pm.getModule().getValues().get("mp_program");
		int pmV = pm.getValue();
		try
		{	int mp = prg.getAddress().getMid().getValue();
			switch(pmV)
			{	case 0:		prg.setValue(normalPrograms.getOrDefault(mp, 0)); break;
				case 1:		prg.setValue(drumKitPrograms.getOrDefault(mp, DEF_DRUMSETPROGRAM)); break;
				default:	prg.setValue(drumsets.getOrDefault(pmV, DEF_DRUMSETPROGRAM)); break;
			}
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
		}
	}

	static void reset()
	{	normalPrograms.clear();
		drumKitPrograms.clear();
		drumsets.clear();
	}

	static void synchronizeDrumset(int pm, XGValue prg)//TODO: hier könnten auch gleich die Drumset-Defaults für einen Programmwechsel des Drumsets ausgelöst werden...
	{	for(XGModule mod : prg.getModule().getType().getModules())
			if(mod.getValues().get("mp_partmode").getValue() == pm) mod.getValues().get("mp_program").setValue(drumsets.getOrDefault(pm, DEF_DRUMSETPROGRAM));
	}
}
