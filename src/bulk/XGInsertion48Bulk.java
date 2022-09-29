package bulk;
import adress.InvalidXGAddressException;
import module.XGModule;
import msg.XGMessenger;
import msg.XGMessengerException;import msg.XGRequest;import msg.XGResponse;import table.XGTable;
import javax.sound.midi.InvalidMidiDataException;
import java.util.HashSet;
import java.util.Set;

public class XGInsertion48Bulk extends XGBulk
{
	public XGInsertion48Bulk(XGBulkType type, XGModule mod)throws InvalidMidiDataException, InvalidXGAddressException
	{	super(type, mod);
	}

	@Override public void submit(XGResponse res)
	{	if(this.isRequired()) super.submit(res);
	}

	@Override public void submit(XGRequest req)
	{	if(this.isRequired())
		{	try
			{	super.submit(req);
			}
			catch(XGMessengerException e)
			{	LOG.severe(e.getMessage());
			}
		}
	}

	private boolean isRequired()
	{	int v = this.getModule().getValues().get("ins_program").getValue();
		return XGTable.FX_MSB_PROGRAMS.contains(v);
	}
}