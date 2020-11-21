package module;

import java.util.HashSet;
import java.util.Set;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import adress.XGMemberNotFoundException;
import device.XGDevice;
import value.XGValue;
import xml.XMLNode;

public class XGDrumsetModuleType extends XGModuleType
{
	private static final XGAddress PARTMODEADDRESS = new XGAddress("8//7"), PROGRAMMADDRESS = new XGAddress("8//1");
	private static final String DEF_NAME = "Drumset";

/****************************************************************************************************************************/

	private Set<Integer> multiparts = new HashSet<>();//alle Multiparts, die diesen Partmode und somit dieses Program teilen
	private int program;
	private final int partmode, hi, id;

	public XGDrumsetModuleType(XGDevice dev, XMLNode n, XGAddress adr)
	{	super(dev, n, adr, n.getStringAttributeOrDefault(ATTR_NAME, DEF_NAME));
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
	{	XGAddressableSet<XGModule> set = this.getDevice().getModules().getAllIncluded(new XGAddress("8-10//"));
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
		{	return this.getDevice().getValues().getFirstIncluded(new XGAddress(PROGRAMMADDRESS.getHi().getValue(), this.getID(pm), PROGRAMMADDRESS.getLo().getValue()));
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
		{	this.multiparts.add(mid);
			
			this.repaintNode();
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

	@Override public String getNodeText()
	{	return this.name + " " + this.id + " (" + this.program + ")";
	}
}
