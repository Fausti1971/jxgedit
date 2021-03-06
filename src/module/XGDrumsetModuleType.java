package module;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import adress.XGMemberNotFoundException;
import device.XGDevice;
import value.*;
import xml.XMLNode;


/*
-registriere jedes drumset permanent als changelistener an jedem partmode

-wenn partmode zu this geändert wird, frage multipartprogramm ab
-und setze es in Map<Integer, Integer> oldMultipartPrograms(mp-id, prog)
-und setze multipartprogram zu this.program(default=StandardKit)
-und registriere this als changelistener an multipartprogram

-wenn multipartprogram geändert wird setze prog an this.program (und an allen in oldMultipartPrograms registrierten multiparts)

-wenn partmode von this.partmode zu 0 oder 1 geändert wird, setze multipartprogram zu oldMultipartPrograms.get(mp-id);
! bei Änderung von this.partmode zu partmode > 1 muss oldMultipartProgram.get(mp-id) irgendwie an das neue Drumset gereicht werden, damit dieses evtl. das originalprogram zurücksetzen kann
! vielleicht sollte es eine device-globale oldMultipartPrograms für alle Drumsets geben, die sobald der multipart die drumsets verlässt (partmode < 2) die oldMultipartPrograms setzt
-und entferne mp-id aus oldMultipartPrograms
-und deregistriere changelistener this an multipartprogram

! weiteres Problem: jeder Multipart kann als Drumkit (Partmode 1) jedes Drumkits annehmen und dieses bleibt je Multipart erhalten, wie auch die Normal-Voice je Multipart erhalten bleibt, was vermutlich ein "Zentralorgan" unvermeidlich macht!
Die Einführung dieser Blackbox führt die bisherigen Bestrebungen ad absurdum und schreit förmlich nach Wiedereinführung der "module_request_action"....
 */

public class XGDrumsetModuleType extends XGModuleType
{
	private static final XGAddress PARTMODEADDRESS = new XGAddress("8//7"), PROGRAMMADDRESS = new XGAddress("8//1");
	private static final String DEF_NAME = "Drumset";

/****************************************************************************************************************************/

	private Map<Integer, Integer> multiparts = new HashMap<>();//alle Multiparts, die diesen Partmode (und somit dieses Program) teilen und deren alte programme (für partmode 0 oder 1)
	private int program;
	private final int partmode, hi, id;

	public XGDrumsetModuleType(XMLNode n, XGAddress adr)
	{	super(n, adr, n.getStringAttributeOrDefault(ATTR_NAME, DEF_NAME));
		int i = 48;
		try
		{	i = this.address.getHi().getValue();
		}
		catch(InvalidXGAddressException e)
		{	LOG.severe(e.getMessage());
		}
		this.hi = i;
		this.partmode = i - 46;
		this.id = i - 47;
	}

	public void initDepencies()
	{	XGAddressableSet<XGModule> set = XGModule.INSTANCES.getAllIncluded(new XGAddress("8-10//"));
		for(XGModule mod : set)
		{	XGValue pm;
			try
			{	pm = mod.getValues().getFirstIncluded(PARTMODEADDRESS);
				XGValue prg = mod.getValues().getFirstIncluded(PROGRAMMADDRESS);
				pm.addValueListener(((XGValue val)->{this.partmodeChanged(val);}));
				if(pm.getValue() == this.partmode)
				{	prg.addValueListener(((XGValue val)->{this.programChanged(val);}));
				}
			}
			catch(XGMemberNotFoundException e)
			{	LOG.severe(e.getMessage());
			}
		}
	}

	private XGValue getProgramForPartmode(XGValue pm)
	{	try
		{	return XGValueStore.STORE.getFirstIncluded(new XGAddress(PROGRAMMADDRESS.getHi().getValue(), this.getID(pm), PROGRAMMADDRESS.getLo().getValue()));
		}
		catch(XGMemberNotFoundException | InvalidXGAddressException e)
		{	LOG.severe(e.getMessage());
			return null;
		}
	}

	private int getID(XGValue v)
	{	try
		{	return v.getAddress().getMid().getValue();
		}
		catch(InvalidXGAddressException e)
		{	LOG.severe(e.getMessage());
			return 0;
		}
	}

	void partmodeChanged(XGValue v)
	{	int mid = this.getID(v);
		if(v.getValue() == this.partmode)
		{	this.multiparts.put(mid, 0);
			
//			this.repaintNode();
System.out.println("partmode changed " + v.getInfo());
		}
		else
		{	this.multiparts.remove(mid);
			
		}
	}

	void programChanged(XGValue v)
	{	
		int mid = this.getID(v);
		
System.out.println("program changed " + v.getInfo());
	}
}
