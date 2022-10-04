package module;

import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import bulk.XGBulk;
import static table.XGTable.INS_MSB_PROGRAMS;
import value.XGValue;
import javax.sound.midi.InvalidMidiDataException;

/**
* If effect type does not require MSB, accept parameters with addresses 02 to 0B, and ignore parameters with addresses from 30 to 42.
  * If effect type requires MSB, accept parameters with addresses 30 to 42 and ignore parameters with addresses 02 to 0B.
  * Bulk transmissions that include effect-type information will always send parameters at addresses 02 to 0B, but
  * if the effect type requires the MSB, the bulk receiving side shall ignore parameters at addresses 02 to 0B.
  * At present, the folloiwng four effect types require MSBs.
  * Delay L,C,R、 Delay L,R、 Echo、 Cross Delay
  * *Data range varies according to effect-type value.
*/
public class XGInsertionModule extends XGModule
{
	XGInsertionModule(XGModuleType mt, int id)throws InvalidMidiDataException, InvalidXGAddressException
	{	super(mt, id);
	}

	public XGValue getProgram()
	{	return this.getValues().get("ins_program");
	}

	public boolean isMSBRequired()
	{	XGValue v = this.getProgram();
		if(v == null) return true;//Krücke, damit die MSB-Parameter während der Initialisierung (XGValue.init()) nicht übergangen werden;
		return INS_MSB_PROGRAMS.contains(this.getProgram().getValue());
	}

	@Override public XGAddressableSet<XGBulk> getBulks()
	{	if(this.isMSBRequired()) return this.bulks;
		else
		{	XGAddressableSet<XGBulk> set = new XGAddressableSet<>();
			set.addAll(this.bulks);
			set.removeIf(bulk-> TAG_INS48.equals(bulk.getTag()));
			return set;
		}
	}
}
