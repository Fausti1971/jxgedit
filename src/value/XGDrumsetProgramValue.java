package value;
import module.XGDrumsetModuleType;import static parm.XGDefaultsTable.DEF_DRUMSETPROGRAM;
import value.XGValue;

/**
* Dieser virtuelle XGValue dient lediglich dazu, für Drumparameter den selectorValue durch das Drumprogram des entsprechenden Drumsets/-kits zu ersetzen
* der defaultSelector muss auch den Value-Reset triggern...
*/
	public class XGDrumsetProgramValue extends XGFixedValue
	{	private final XGDrumsetModuleType type;

		public XGDrumsetProgramValue(XGDrumsetModuleType t)
		{	super("ds_program", t.getPartmode());
			this.type = t;
		}

		@Override public int getValue()
		{	return this.type.getProgram();
		}
	}