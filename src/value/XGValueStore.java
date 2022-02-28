package value;

import javax.sound.midi.InvalidMidiDataException;
import adress.*;
import application.*;
import module.*;
import static module.XGModuleType.TYPES;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGMessengerException;
import msg.XGRequest;
import msg.XGResponse;
import parm.*;

public class XGValueStore extends XGAddressableSet<XGValue> implements XGMessenger, XGLoggable
{
	public static XGValueStore STORE = new XGValueStore();

/**
* initialisiert zu jedem Moduletype die angegebene Anzahl Instanzen (XGModule) inkl. der Bulk-Instanzen (XGAddress) und Opcode-Instanzen (XGValues inkl. Abhängigkeiten)
*/
	public static void init()
	{	for(XGModuleType mt : TYPES)
		{	for(XGModule mod : mt.getModules())
			{	for(XGBulk blk : mod.getBulks())
				for(XGOpcode opc : blk.getType().getOpcodes())
				{	try
					{	STORE.add(new XGValue(opc, blk));
					}
					catch(InvalidXGAddressException e)
					{	LOG.warning(e.getMessage());
					}
				}
			}
		}
//export opcodes
		//for(XGModuleType mt : TYPES)
		//{	System.out.println(mt.getTag());
		//	for(XGOpcode o : mt.getOpcodes()) System.out.println(o.getTag());
		//}
		for(XGValue v : STORE)
		{	try	{	v.initDepencies();}
			catch(InvalidXGAddressException e)	{	LOG.warning(e.getMessage());}
		}
		for(XGValue v : STORE) v.setDefaultValue();
		LOG.info(STORE.size() + " Values initialized");
	}

/************************************************************************************************************/

	@Override public synchronized XGValue get(XGAddress adr)
	{	if(adr.isFixed()) return super.get(adr);
		else throw new RuntimeException(adr + " is not a valid value-address!");
	}

	@Override public void submit(XGMessage message) throws InvalidXGAddressException, XGMessengerException
	{	if(!(message instanceof XGResponse)) throw new XGMessengerException(message + " is not of type XGResponse");
		XGResponse msg = (XGResponse)message;
		int end = msg.getBulkSize() + msg.getBaseOffset(),
			offset = msg.getAddress().getLo().getValue(),
			hi = msg.getAddress().getHi().getValue(),
			mid = msg.getAddress().getMid().getValue(),
			size = 1;

		for(int i = msg.getBaseOffset(); i < end;)
		{	XGAddress adr = new XGAddress(hi, mid, offset);
			XGValue v = this.get(adr);
			if(v != null)
			{	v.decodeMessage(msg);
				size = v.getSize();
			}
			else
			{	LOG.severe("value not found: " + adr);
				size = 1;
			}
			offset += size;
			i += size;
		}
	}

	@Override public String toString()
	{	return "Memory";
	}

	@Override public void request(XGRequest req) throws InvalidXGAddressException
	{	XGResponse res = req.getResponse();
		int
			hi = res.getAddress().getHi().getValue(),
			mid = res.getAddress().getMid().getValue(),
			lo = res.getAddress().getLo().getValue(),
			end = res.getBulkSize() + res.getBaseOffset(),
			size = 1;

		for(int i = res.getBaseOffset(); i < end;)
		{	XGAddress adr = new XGAddress(hi, mid, lo);
			XGValue v = this.get(adr);
			if(v != null)
			{	v.encodeMessage(res);
				size = v.getSize();
			}
			else
			{	LOG.severe("value not found:"  + adr);
				size = 1;
			}
			lo += size;
			i += size;
		}
		res.setChecksum();
		try
		{	res.checkSum();
			req.setResponsedBy(res);
		}
		catch(InvalidMidiDataException e)
		{	LOG.severe(e.getMessage());
		}
//		res.getDestination().submit(res);
	}

	@Override public void close()
	{
	}
}
