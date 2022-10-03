package bulk;
import adress.InvalidXGAddressException;
import application.XGStrings;import module.XGModule;
import msg.*;
import table.XGTable;import static table.XGTable.INS_MSB_PROGRAMS;
import javax.sound.midi.InvalidMidiDataException;

public class XGInsertion48Bulk extends XGBulk
{
/**
* If effect type does not require MSB, accept parameters with addresses 02 to 0B, and ignore parameters with addresses from 30 to 42.
  * If effect type requires MSB, accept parameters with addresses 30 to 42 and ignore parameters with addresses 02 to 0B.
  * Bulk transmissions that include effect-type information will always send parameters at addresses 02 to 0B, but
  * if the effect type requires the MSB, the bulk receiving side shall ignore parameters at addresses 02 to 0B.
  * At present, the folloiwng four effect types require MSBs.
  * Delay L,C,R、 Delay L,R、 Echo、 Cross Delay
  * *Data range varies according to effect-type value.
*/
	public XGInsertion48Bulk(XGBulkType type, XGModule mod)throws InvalidMidiDataException, InvalidXGAddressException
	{	super(type, mod);
	}

	@Override public void submit(XGMessageBulkDump res)throws XGMessengerException
	{	if(this.isMSBRequired()) super.submit(res);
	}

	@Override public void submit(XGMessageParameterChange res)throws XGMessengerException
	{	if(this.isMSBRequired()) super.submit(res);
	}

	@Override public void submit(XGMessageBulkRequest req)throws XGMessengerException
	{	if(this.isMSBRequired()) super.submit(req);
	}

	@Override public void submit(XGMessageParameterRequest req)throws XGMessengerException
	{	if(this.isMSBRequired()) super.submit(req);
	}

	private boolean isMSBRequired()
	{	int v = this.getModule().getValues().get("ins_program").getValue();
		boolean res = INS_MSB_PROGRAMS.contains(v);
LOG.info("ins_programs: " + INS_MSB_PROGRAMS.size() + " contains " + XGStrings.valueToString(v) + " = " + res);
		return res;
	}
}