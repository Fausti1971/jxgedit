package bulk;
import adress.InvalidXGAddressException;
import application.XGStrings;import module.XGModule;
import msg.XGMessenger;
import msg.XGMessengerException;
import javax.sound.midi.InvalidMidiDataException;
import java.util.HashSet;
import java.util.Set;

public class XGInsertion48Bulk extends XGBulk
{	public static final Set<Integer> MSB_PROGRAMS = new HashSet<>();//wird während der XGRealTable-Initialisierung durch XGEntry (mit programmvalues, die das Attribut address="word" besitzen) befüllt;

	public XGInsertion48Bulk(XGBulkType type, XGModule mod)throws InvalidMidiDataException, InvalidXGAddressException
	{	super(type, mod);
	}

	@Override public void transmit(XGMessenger dest)throws XGMessengerException, InvalidXGAddressException
	{	if(this.isRequired()) super.transmit(dest);
	}

	@Override public boolean request(XGMessenger dest)throws XGMessengerException, InvalidXGAddressException, InvalidMidiDataException
	{	if(this.isRequired()) return super.request(dest);
		else return true;
	}

	private boolean isRequired()
	{	int v = this.getModule().getValues().get("ins_program").getValue();
		return MSB_PROGRAMS.contains(v);
	}
}