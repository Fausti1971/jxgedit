package value;

import java.util.HashMap;
import java.util.Map;
import adress.InvalidXGAddressException;import module.XGDrumsetModuleType;import module.XGModule;import static parm.XGDefaultsTable.DEF_DRUMSETPROGRAM;import tag.XGTagableAddressableSet;import tag.XGTagableSet;
/**
* Puffert je Multipart ein normal- und ein drumkit-Program und je Partmode ein drumsetProgram und synchronisiert diese bei Änderung
*/
//TODO:XGProgramBuffer meldet sich (oder eine Lambda) als Listener an allen XGValues names "mp_program" und "mp_partmode" an;
// Vorsicht (!) wegen StackOverflow durch XGValueChangeListener (jedes setValue() - wie in synchronizeDrumsets() - triggert die angehängten Listeners und somit erneut XGProgramBuffer)
// das spart die "buffer_program" und "restore_buffer"-action und würde auch bei Änderungen unabhängig der GUI getriggert;
// außerdem könnte, geschickt eingesetzt, XGProgrammBuffer auch als defaultSelector namens "ds_program" fungieren;


public interface XGProgramBuffer
{
	Map<Integer, Integer> normalPrograms = new HashMap<>();//mp-id (partmode = 0), prog
	Map<Integer, Integer> drumkitPrograms = new HashMap<>();//mp-id (partmode = 1), prog
	Map<Integer, Integer> drumsetPrograms = new HashMap<>();//partmode (> 1), prog
	Map<Integer, XGDrumsetProgramListener> drumsetListeners = new HashMap<>();//partmode, listener
/**
* erzeugt, speichert und returniert einen (als defaultSelector dienenden) XGValue, der zu einem bestimmten Drumset stets das momentan eingestellte Program liefert
* muss bei !v.getTag().equals("ds_program") null zurückliefern, damit der defaultSelector im XGValue null (und somit DEF_DEFAULTSELECTOR) wird
*/
	static XGDrumsetProgramListener getDrumsetProgramListener(XGValue v)
	{	if("ds_program".equals(v.getOpcode().getDefaultSelectorTag()) && v.getModule().getType() instanceof XGDrumsetModuleType)
		{	int pm = ((XGDrumsetModuleType)v.getModule().getType()).getPartmode();
			XGDrumsetProgramListener sel = drumsetListeners.getOrDefault(pm, new XGDrumsetProgramListener(pm));
			drumsetListeners.putIfAbsent(pm, sel);
			return sel;
		}
		else return null;
	}
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
				default:	drumsetPrograms.put(pm, prg); synchronizeDrumset(partmode, program);	break;
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
				default:	prg.setValue(drumsetPrograms.getOrDefault(pm, DEF_DRUMSETPROGRAM)); break;
			}
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
		}
	}

	static void reset()
	{	normalPrograms.clear();
		drumkitPrograms.clear();
		drumsetPrograms.clear();
	}

	static void synchronizeDrumset(XGValue partmode, XGValue program)
	{	int pm = partmode.getValue();
		for(XGModule mod : partmode.getModule().getType().getModules())
		{	XGTagableAddressableSet<XGValue> vals = mod.getValues();
			if(vals.get("mp_partmode").getValue() == pm)
				vals.get("mp_program").setValue(drumsetPrograms.getOrDefault(pm, DEF_DRUMSETPROGRAM));
		}
		XGDrumsetProgramListener l = drumsetListeners.get(pm);
		if(l != null) l.contentChanged(l);
	}

/**
* Dieser virtuelle XGValue dient lediglich dazu, für Drumparameter den selectorValue durch das Drumprogram des entsprechenden Drumsets/-kits zu ersetzen
* der defaultSelector muss auch den Value-Reset triggern...
*/
	class XGDrumsetProgramListener extends XGValue
	{	private final int partmode;

		public XGDrumsetProgramListener(int pm)
		{	super("ds_program", pm);
			this.partmode = pm;
		}

		@Override public int getValue()
		{	return drumsetPrograms.getOrDefault(this.partmode, DEF_DRUMSETPROGRAM);
		}
	}
}
