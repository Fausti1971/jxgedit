package module;

import adress.XGInvalidAddressException;
import adress.XGIdentifiableSet;
import application.XGStrings;import device.XGDevice;
import device.XGMidi;
import msg.XGClippboard;
import msg.XGMessageConstants;import msg.XGMessageParameterChange;
import msg.XGMessengerException;
import table.*;import value.XGDrumsetProgramValue;
import value.XGValueType;
import xml.XMLNode;
import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XGDrumsetModuleType extends XGModuleType
{
	private static final byte[] RESET_MSG = {(byte)XGMessageConstants.SOX, XGMessageConstants.VENDOR, XGMessageConstants.MSG_PC, XGMessageConstants.MODEL, 0, 0, 0x7D, 0, (byte)XGMessageConstants.EOX};
	public static final Map<Integer, XGDrumsetModuleType> DRUMSETS = new HashMap<>();//partmode, Drumset
	public static final Map<Integer, XGRealTable> DRUMNAMES = new HashMap<>();//key, <drumprg, drumname>
	private static final int DEF_DRUMSETPROGRAM = 127 << 14;
	private static final int FALLBACKMASK = 127 << 14;
	private static final XGClippboard CLIPPBOARD = new XGClippboard();//ein Clippboard für alle 4 DrumsetTypen

	public static void init()
	{	try
		{	XMLNode n = XMLNode.parse(XML_DRUMS);
			for(XMLNode k : n.getChildNodes(TAG_KEY))
			{	int key = k.getValueAttribute(ATTR_VALUE, -1);
				XGRealTable t = DRUMNAMES.getOrDefault(key, new XGRealTable(Integer.toString(key), "", FALLBACKMASK, false));
				for(XMLNode i : k.getChildNodes(TAG_ITEM))
				{	t.add(new XGTableEntry(i.getValueAttribute(ATTR_SELECTORVALUE, -1), i.getStringAttribute(ATTR_NAME)));
					DRUMNAMES.putIfAbsent(key, t);
				}
				LOG.info(t + " initialized");
			}
		}
		catch(IOException e)
		{	LOG.severe(e.getMessage());
		}
	}
/****************************************************************************************************************************/

	private volatile int program = DEF_DRUMSETPROGRAM;
	private final int partmode;
	private final XGDrumsetProgramValue programListener;

	public XGDrumsetModuleType(XMLNode n, int hi) throws XGInvalidAddressException
	{	super(n, n.getStringAttributeOrDefault(ATTR_NAME, "Drumset"), hi);
		this.partmode = hi - 46;
		this.tag += this.partmode - 1;
		this.name.append(" ").append(this.partmode - 1);

		((XGRealTable)XGTable.TABLES.get(XGTableConstants.TABLE_PARTMODE)).add(new XGTableEntry(this.partmode, this.name.toString()));
		this.idTranslator = new XGVirtualTable(this.partmode, this.partmode, this.tag, this::getDrumname, (String s)->this.partmode);
		this.programListener = new XGDrumsetProgramValue(this);
		DRUMSETS.put(this.partmode, this);
	}

	@Override public XGClippboard getClippboard(){	return CLIPPBOARD;}

	public String getDrumname(int key)
	{	String keyname = XGStrings.encodeKey(key);//note
		String drumname = "No Sound";
		XGRealTable t;
		if(DRUMNAMES.containsKey(key))
		{	t = DRUMNAMES.get(key);
			if(t.containsValue(this.program)) drumname = t.getByValue(this.program).getName();
			else if(t.containsValue(this.program & DEF_DRUMSETPROGRAM)) drumname = t.getByValue(this.program & DEF_DRUMSETPROGRAM).getName();
		}
		return keyname + " (" + drumname + ")";
	}

	public int getPartmode(){ return this.partmode;}

	private XGIdentifiableSet<XGModule> getMultiparts()
	{	XGIdentifiableSet<XGModule> mp = new XGIdentifiableSet<>();
		for(XGModule mod : MODULE_TYPES.get("mp").getModules())
		{	if(mod.getValues().get(XGValueType.MP_PM_VALUE_TAG).getValue() == this.partmode) mp.add(mod);
		}
		return mp;
	}

	public XGDrumsetProgramValue getProgramListener(){	return this.programListener;}

	public int getProgram(){	return this.program;}

	public void setProgram(int prg)
	{	this.program = prg;
		for(XGModule mod : this.getMultiparts())
		{	mod.getValues().get(XGValueType.MP_PRG_VALUE_TAG).setValue(prg, false, false);
		}
		this.programListener.notifyValueListeners(this.programListener);
	}

	public void reset()
	{	try
		{	RESET_MSG[7] = (byte)(this.partmode - 2);
			XGMidi.getMidi().submit(new XGMessageParameterChange(XGDevice.DEVICE, RESET_MSG, true));
			this.resetValues();
		}
		catch( XGMessengerException | InvalidMidiDataException e)
		{	LOG.severe(e.getMessage());
		}
	}
}
