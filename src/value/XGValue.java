package value;
import java.util.HashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import application.XGLoggable;
import msg.XGMessageBulkDump;
import msg.XGMessageParameterChange;
import msg.XGMessenger;
import msg.XGMessengerException;
import msg.XGResponse;
import parm.XGOpcode;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;
import parm.XGTable;
import parm.XGTable.Preference;
import parm.XGTableEntry;
/**
 * 
 * @author thomas
 *
 */
public class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAddressable, XGValueChangeListener, XGLoggable
{

/***********************************************************************************************/

/**
 * Index des XGTableEntry in der (im XGParameter anhängenden) XGTable
 */
	private Integer index;
	private XGMessenger source;//TODO: überdenken; ist hier tatsächlich der XGMessenger erforderlich oder reicht auch lediglich das XGDevice
	private final XGAddress address;
	private final XGOpcode opcode;
	private XGParameter parameter;
	private final XGValue parameterSelector, defaultSelector;
	private final Set<XGValueChangeListener> valueListeners = new HashSet<>();
	private final Set<XGParameterChangeListener> parameterListeners = new HashSet<>();

	public XGValue(String name, int v)
	{	this.source = null;
		this.address = XGALLADDRESS;
		this.parameter = null;
		this.index = v;
		this.parameterSelector = null;
		this.defaultSelector = null;
		this.opcode = null;
	}

	public XGValue(XGMessenger src, XGAddress adr) throws InvalidXGAddressException
	{	this.source = src;

		if(!adr.isFixed()) throw new InvalidXGAddressException("no valid value-address: " + adr);
		this.address = adr;

		Set<XGOpcode> set = src.getDevice().getOpcodes().getAllIncluding(this.address);
		if(set.size() != 1) throw new RuntimeException("found " + set.size() + " matches for address " + this.address);
		this.opcode = set.iterator().next();

		if(this.opcode.isMutable())
		{	XGAddress a = this.opcode.getParameterSelectorAddress().complement(this.address);
			this.parameterSelector = src.getDevice().getValues().get(a);
			this.parameterSelector.addValueListener((XGValue v)->{this.assignParameter();});
		}
		else this.parameterSelector = null;
		this.assignParameter();

		if(this.opcode.hasMutableDefaults())
		{	XGAddress a = this.opcode.getDefaultSelectorAddress().complement(this.address);
			this.defaultSelector = src.getDevice().getValues().get(a);
			this.defaultSelector.addValueListener((XGValue v)->{this.setDefaultValue(v);});
		}
		else this.defaultSelector = null;
		this.index = 0;
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
	{	for(XGParameterChangeListener l : this.parameterListeners) l.parameterChanged(this.parameter);
	}

	public XGMessenger getSource()
	{	return this.source;
	}

	public void setSource(XGMessenger src)
	{	this.source = src;
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

	private void assignParameter()
	{	if(this.opcode.isMutable())
		{	int progNr = this.parameterSelector.getValue();
			this.parameter = this.opcode.getParameters().get(progNr);
			this.notifyParameterListeners();
		}
		else this.parameter = this.opcode.getParameters().get(DEF_SELECTORVALUE);
	}

	private void setDefaultValue(XGValue v)
	{	int prog = v.getValue();
		int value = this.opcode.getDefaults().get(prog);
		this.setValue(value);
	}

	public XGParameter getParameter()
	{	return this.parameter;
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
 * @param setzt den Inhalt des XGValue auf den Index des übergebenen XGTable-Eintrags
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
 * setzt den Inhalt des XGValue auf den Index des zum übergebenen value passenden XGTable-Eintrags
 * @param v Value
 * @return true, wenn sich der Inhalt änderte
 */
	public boolean setValue(int v)
	{	XGParameter p = this.getParameter();
		if(p == null) return true;
		return this.setIndex(p.getTranslationTable().getIndex(v, Preference.FALLBACK));
	}

	public void editEntry(XGTableEntry e)
	{	this.actions(XACTION_BEFORE_EDIT);
		if(setEntry(e))
		{	this.actions(XACTION_AFTER_EDIT);
		}
	}

	public boolean editIndex(int i)
	{	this.actions(XACTION_BEFORE_EDIT);
		boolean changed = setIndex(i);
		if(changed)
		{	this.actions(XACTION_AFTER_EDIT);
		}
		return changed;
	}

	public boolean addIndex(int diff)
	{	return this.editIndex(this.index + diff);
	}

	public void sendAction()
	{	this.actions(XACTION_BEFORE_SEND);
		try
		{	new XGMessageParameterChange(this.source, this.source.getDevice().getMidi(), this).transmit();
		}
		catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
		this.actions(XACTION_AFTER_SEND);
	}

	public void bulkAction()
	{	this.actions(XACTION_BEFORE_SEND);
		{	XGAddress adr = this.getAddress();
			try
			{	XGMessageBulkDump msg = new XGMessageBulkDump(this.getSource(), this.getSource().getDevice().getMidi(), new XGAddress(adr.getHi(), adr.getMid(), this.getOpcode().getAddress().getLo()));
				msg.encodeLSB(msg.getBaseOffset(), msg.getBulkSize(), this.getValue());
				msg.setChecksum();
				msg.transmit();
			}
			catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
			{	LOG.severe(e1.getMessage());
			}
		}
		this.actions(XACTION_AFTER_SEND);
	}

	public String getInfo()
	{	XGParameter p = this.getParameter();
		if(p != null) return p.getShortName() + "=" + this;
		else return "no parameter info";
	}

	private void actions(String type)
	{	Set<String> set = this.opcode.getActions().get(type);
		if(set != null)
		{	for(String s : set)
			{	switch(s)
				{	case "module_default":	break;	//TODO: lade alle defaults des gesamten modules
					case "mutable_default":	break;	//TODO: lade nur defaults der mutable-default-values
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
		if(p.getUnit().isBlank()) return s + " " + t.getUnit();
		else return s + " " + p.getUnit();
	}

	@Override public int compareTo(XGValue o)
	{	return this.address.compareTo(o.address);
	}

	@Override public void contentChanged(XGValue v)
	{	this.notifyValueListeners();
	}
}
