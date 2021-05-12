package value;

import javax.sound.midi.InvalidMidiDataException;
import adress.*;
import application.*;
import static application.ConfigurationConstants.APPNAME;
import static application.ConfigurationConstants.XMLPATH;import module.*;
import static module.XGModuleType.TYPES;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGMessengerException;
import msg.XGRequest;
import msg.XGResponse;
import parm.*;
import xml.*;
import static xml.XMLNodeConstants.*;
import java.io.*;

public class XGValueStore extends XGAddressableSet<XGValue> implements XGMessenger, XGLoggable
{
	public static XGValueStore STORE = null;

/**
* initialisiert zu jedem Moduletype die angegebene Anzahl Instanzen (XGModule) inkl. der Bulk-Instanzen (XGAddress) und Opcode-Instanzen (XGValues inkl. Abhängigkeiten)
*/
	public static void init()
	{	STORE = new XGValueStore();
		for(XGModuleType mt : TYPES)
		{	for(int id : mt.getAddress().getMid())
			{	try
				{	XGModule mod = new XGModule(mt, id);
					mt.getModules().add(mod);
					for(XGOpcode opc : mt.getOpcodes())
					{	if(opc.getModuleType().equals(mt)) STORE.add(new XGValue(opc, mod));
					}
					for(XGValue val : mod.getValues()) val.initValueDepencies();
				}
				catch(InvalidXGAddressException e)
				{	LOG.warning(e.getMessage());
				}
			}
			LOG.info(mt.getModules().size() + " " + mt + "-Modules initialized");
		}
//export opcodes
		//for(XGModuleType mt : TYPES)
		//{	System.out.println(mt.getTag());
		//	for(XGOpcode o : mt.getOpcodes()) System.out.println(o.getTag());
		//}
		for(XGValue v : STORE) v.setDefaultValue();
		LOG.info(STORE.size() + " Values initialized");
	}

/************************************************************************************************************/
	@Override public synchronized XGValue get(XGAddress adr)
	{	if(adr.isFixed()) return super.get(adr);
		else throw new RuntimeException(adr + " is not a valid value-address!");
	}

	@Override public String getMessengerName()
	{	return APPNAME + " (Memory)";
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
		}
		catch(InvalidMidiDataException e)
		{	LOG.severe(e.getMessage());
			req.setResponsed(false);
		}
		req.setResponsed(true);
//		res.getDestination().submit(res);
	}

	@Override public void close()
	{
	}
}
