package value;
import java.util.HashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import application.XGLoggable;
import device.*;
import msg.XGMessageBulkDump;
import msg.XGMessageParameterChange;
import msg.XGMessengerException;
import msg.XGResponse;
import parm.XGDefaultsTable;
import static parm.XGDefaultsTable.DEFAULTSTABLE;import parm.XGOpcode;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;
import parm.XGParameterTable;
import static parm.XGParameterTable.PARAMETERTABLES;import parm.XGTable;
import parm.XGTable.Preference;
import parm.XGTableEntry;
import tag.*;import static value.XGValueStore.STORE;
/**
 * 
 * @author thomas
 *
 */
public class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAddressable, XGValueChangeListener, XGLoggable, XGCategorizeable, XGTagable
{
	private static final XGValue DEFAULTSELECTOR = new XGFixedValue("defaultSelector", DEF_SELECTORVALUE);

/***********************************************************************************************/

/**
 * Index des XGTableEntry in der (im XGParameter anhängenden) XGTable
 */
	private Integer index = 0;
	private final XGAddress address;
	private final XGOpcode opcode;
	private XGValue parameterSelector = null, defaultSelector = null;
	private final XGParameterTable parameters;
	private final XGDefaultsTable defaults;
	private final Set<XGValueChangeListener> valueListeners = new HashSet<>();
	private final Set<XGParameterChangeListener> parameterListeners = new HashSet<>();

	XGValue(String name, int v)
	{	this.address = XGALLADDRESS;
		this.index = v;
		this.parameterSelector = DEFAULTSELECTOR;
		this.defaultSelector = DEFAULTSELECTOR;
		this.parameters = null;
		this.defaults = null;
		this.opcode = null;
	}

	public XGValue(XGOpcode opc, int id) throws InvalidXGAddressException
	{	this.opcode = opc;
		this.address = new XGAddress(opc.getAddress().getHi().getValue(), id, opc.getAddress().getLo().getMin());
		if(!this.address.isFixed()) throw new InvalidXGAddressException("no valid value-address: " + this.address);
		if(opc.isMutable())
		{	this.parameters = PARAMETERTABLES.get(opc.getParameterTableName());
			if(this.parameters == null) throw new RuntimeException(ATTR_PARAMETERS + " " + opc.getParameterTableName() + " not found!");
		}
		else
		{	this.parameters = new XGParameterTable();
			this.parameters.put(DEF_SELECTORVALUE, new XGParameter(opc.getConfig()));
		}

//TODO: prüfe, ob die DefaultsTable wieder komplett nach XGOpcode umziehen kann! Vielleicht auch statisch: DEFAULTSTABLE.getOrNew(String tag, int id, int sel, int def)
		if(opc.hasMutableDefaults())
		{	this.defaults = DEFAULTSTABLE.get(opc.getDefaultsTableName());
			if(this.defaults == null) throw new RuntimeException(ATTR_DEFAULTS + " " + opc.getDefaultsTableName() + " not found!");
		}
		else
		{	this.defaults = new XGDefaultsTable(opc.getTag());
			this.defaults.put(XGDefaultsTable.NO_ID, DEF_SELECTORVALUE, opc.getConfig().getValueAttribute(ATTR_DEFAULT, 0));
		}
	}

	public void initValueDepencies() throws InvalidXGAddressException
	{	if(this.opcode.isMutable())
		{	XGAddress psa = this.opcode.getParameterSelectorAddress().complement(this.address);
			XGValue psv = STORE.get(psa);
			if(psv == null) throw new RuntimeException("parameterSelector " + psa + " not found for value " + this.address);
			this.parameterSelector = psv;
			this.parameterSelector.addValueListener((XGValue val)->{this.notifyParameterListeners();});
		}
		else this.parameterSelector = DEFAULTSELECTOR;

		if(this.opcode.hasMutableDefaults())
		{	XGAddress dsa = this.opcode.getDefaultSelectorAddress().complement(this.address);
			XGValue dsv = STORE.get(dsa);
			if(dsv == null) throw new RuntimeException("defaultsSelector " + dsa + " not found for value " + this.address);//TODO: drumset hat zwar mutableDefaults aber kann keinen festen defaultSelector (multipartProgram) haben
			this.defaultSelector = dsv;
			this.defaultSelector.addValueListener((XGValue)->{this.setDefaultValue();});
		}
		else this.defaultSelector = DEFAULTSELECTOR;
	}

	public void addValueListener(XGValueChangeListener l)
	{	this.valueListeners.add(l);
	}

	public void removeValueListener(XGValueChangeListener l)
	{	this.valueListeners.remove(l);
	}

	public void addParameterListener(XGParameterChangeListener l)
	{	this.parameterListeners.add(l);
	}

	public void removeParameterListener(XGParameterChangeListener l)
	{	this.parameterListeners.remove(l);
	}

	public void notifyValueListeners()
	{	for(XGValueChangeListener l : this.valueListeners) l.contentChanged(this);
	}

	public void notifyParameterListeners()
	{	for(XGParameterChangeListener l : this.parameterListeners) l.parameterChanged(this.getParameter());
	}

	public XGOpcode getOpcode()
	{	return this.opcode;
	}

	public int getSize()
	{	return this.opcode.getAddress().getLo().getSize();
	}

	public XGValue getParameterSelector()
	{	return this.parameterSelector;
	}

	public void setDefaultValue()
	{	int id = DEF_SELECTORVALUE;
		try
		{	id = this.address.getMid().getValue();
		}
		catch(InvalidXGAddressException e)
		{	LOG.info(e.getMessage());
		}
		this.setValue(this.defaults.get(id, this.defaultSelector.getValue()));
	}

	public XGParameter getParameter()
	{	return this.parameters.getOrDefault(this.parameterSelector.getValue(), NO_PARAMETER);
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

/**
 * decodiert und setzt aus dem Bytearray der übergebenen Message den Wert am zum XGValue gehörenden offset und size
 * @param msg
 * @throws InvalidXGAddressException
 */
	public void decodeMessage(XGResponse msg) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.address.getLo().getValue() - msg.getLo();
		switch(this.opcode.getDataType())
		{	default:
			case LSB:	this.setValue(msg.decodeLSB(offset, this.getSize())); break;
			case LSN:	this.setValue(msg.decodeLSN(offset, this.getSize())); break;
		}
	}

	public void encodeMessage(XGResponse msg) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.address.getLo().getValue() - msg.getLo();
		switch(this.opcode.getDataType())
		{	default:
			case LSB:	msg.encodeLSB(offset, this.getSize(), this.getValue()); break;
			case LSN:	msg.encodeLSN(offset, this.getSize(), this.getValue()); break;
		}
	}

/**
 * returniert des Inhalts Index in der aktuellen XGTable des momentan zuständigen XGParameter
 */
	public Integer getIndex()
	{	return this.index;
	}

/**
 * setzt nach Validierung den XGTable-Index des XGValue auf den Wert i ohne die XAction-Kette auszulösen
 * @param i	Index
 * @return	true, wenn sich der Inhalt änderte
 */
	public boolean setIndex(Integer i)
	{	int old = this.index;
		XGParameter p = this.getParameter();
		if(p != null) this.index = p.validate(i);
		boolean changed = this.index != old;
		if(changed) this.notifyValueListeners();
		return changed;
	}

/**
 * @return returniert den momentan ausgewählten XGTable-Eintrag
 */
	public XGTableEntry getEntry()
	{	XGParameter p = this.getParameter();
		if(p == null) return null;
		return p.getTranslationTable().getByIndex(this.getIndex());
	}

/**
 * 
 * @param e setzt den Inhalt des XGValue auf den Index des übergebenen XGTable-Eintrags
 * @return true, wenn sich der Inhalt änderte
 */
	public boolean setEntry(XGTableEntry e)
	{	return this.setValue(e.getValue());
	}

/**
 * @return Value des aktuell gewählten XGTable-Eintrags
 */
	public int getValue()
	{	if(this.getEntry() == null) return 0;
		return this.getEntry().getValue();
	}

/**
 * setzt den Inhalt des XGValue auf den Index des zum übergebenen value passenden XGTable-Eintrags ohne Action
 * @param v Value
 * @return true, wenn sich der Inhalt änderte
 */
	public boolean setValue(int v)
	{	XGParameter p = this.getParameter();
		if(p == null) return true;
		return this.setIndex(p.getTranslationTable().getIndex(v, Preference.FALLBACK));
	}

/**
 * setzt den übergebenen Entry im Value und durchläuft die Action-Kette
 * @param e
 * @return true, wenn sich Inhalt änderte
 */
	public boolean editEntry(XGTableEntry e)
	{	this.actions(XACTION_BEFORE_EDIT);
		boolean changed = setEntry(e);
		if(changed)
		{	this.actions(XACTION_AFTER_EDIT);
		}
		return changed;
	}

/**
 * setzt den übergebenen Index im Value und durchläuft die Action-Kette
 * @param i
 * @return true, wenn sich Inhalt änderte
 */
	public boolean editIndex(int i)
	{	this.actions(XACTION_BEFORE_EDIT);
		boolean changed = setIndex(i);
		if(changed)
		{	this.actions(XACTION_AFTER_EDIT);
		}
		return changed;
	}

/**
 * addiert nach Validierung/Limitierung den übergebenen Wert zum Index und durchläuft die Action-Kette
 * @param diff
 * @return true, wenn sich Inhalt änderte
 */
	public boolean addIndex(int diff)
	{	return this.editIndex(this.index + diff);
	}

/**
 * schaltet um zwischen Min- und MaxIndex und durchläuft die Action-Kette
 * @return true, wenn sich Inhalt änderte
 */
	public boolean toggleIndex()
	{	XGParameter p = this.getParameter();
		if(this.index == p.getMinIndex()) return this.editIndex(p.getMaxIndex());
		else return this.editIndex(p.getMinIndex());
	}
/**
 * sendet den Value mittels XGMessageParameterChange
 */
	public void sendAction()
	{	this.actions(XACTION_BEFORE_SEND);
		try
		{	new XGMessageParameterChange(STORE, XGMidi.getMidi(), this).transmit();
		}
		catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
		this.actions(XACTION_AFTER_SEND);
	}

/**
 * sendet den Value mittels XGMessageBulkDump
 */
	public void bulkAction()
	{	this.actions(XACTION_BEFORE_SEND);
		XGAddress adr = this.getAddress();
		try
		{	XGMessageBulkDump msg = new XGMessageBulkDump(STORE, XGMidi.getMidi(), new XGAddress(adr.getHi(), adr.getMid(), this.getOpcode().getAddress().getLo()));
			msg.encodeLSB(msg.getBaseOffset(), msg.getBulkSize(), this.getValue());
			msg.setChecksum();
			msg.transmit();
		}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
		this.actions(XACTION_AFTER_SEND);
	}

	public String getInfo()
	{	XGParameter p = this.getParameter();
		if(p != null) return p.getName() + "=" + this;
		else return "no parameter info";
	}

	private void actions(String type)
	{	Set<String> set = this.opcode.getActions().get(type);
		if(set != null)
		{	for(String s : set)
			{	switch(s)
				{	case "module_default":	break;	//TODO: lade alle defaults des gesamten moduls
					case "send": 			this.sendAction(); break;	//send via XGMessageParameterChange (normal)
					case "bulk":			this.bulkAction(); break;	//bulk via XGMessageBulkDump (voice-programs)
					default:				LOG.info("unknown action: " + s + " for value: " + this.getInfo());
				}
			}
		}
	}

	@Override public String toString()
	{	XGParameter p = this.getParameter();
		XGTable t = p.getTranslationTable();
		String s = t.getByIndex(this.index).getName();
		if(p.getUnit().isEmpty()) return s + " " + t.getUnit();
		else return s + " " + p.getUnit();
	}

	@Override public int compareTo(XGValue o)
	{	return this.address.compareTo(o.address);
	}

	@Override public void contentChanged(XGValue v)
	{	this.notifyValueListeners();
	}

	@Override public String getCategory()
	{	return this.opcode.getCategory();
	}

	public String getTag()
	{	return this.opcode.getTag();
	}
}
