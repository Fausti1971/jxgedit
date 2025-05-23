package value;

import java.util.HashMap;
import java.util.Map;
import application.XGLoggable;import module.XGDrumsetModuleType;import table.XGDefaultsTable;
/**
* Puffert je Multipart ein normal- und ein drumkit-program und synchronisiert diese bei Änderung;
* drumset-programs werden im zugehörigen XGDrumsetModule gespeichert;
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
			case 1:		drumkitPrograms.put(mp, prg); break;//TODO: drumkit-multipart-programme erfordern ebenfalls eine synchronisation
			default:	XGDrumsetModuleType.DRUMSETS.get(pm).setProgram(prg); break;
		}
	}

/**
* Restauriert den Wert des Programms für den angegebenen Partmode (partmode) aus dem internen Cache
*/
	static void restoreProgram(XGValue partmode)
	{	XGLoggable.LOG.info("oldPartmode=" + partmode.oldValue + ", newPartmode=" + partmode.value);

		XGDrumsetModuleType dsmt = XGDrumsetModuleType.DRUMSETS.get(partmode.oldValue);
		if(dsmt != null) dsmt.getAssignedMultiparts().remove(partmode.getModule());

		dsmt = XGDrumsetModuleType.DRUMSETS.get(partmode.value);
		if(dsmt != null) dsmt.getAssignedMultiparts().add(partmode.getModule());

		XGValue prg = partmode.getBulk().getValues().get(XGValueType.MP_PRG_VALUE_TAG);
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
