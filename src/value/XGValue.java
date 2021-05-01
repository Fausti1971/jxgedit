package value;
import java.util.HashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import application.XGLoggable;
import device.*;
import module.XGModule;import msg.XGMessageBulkDump;
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
	private Integer index = 0, oldIndex = 0;
	private final XGAddress address;
	private final XGOpcode opcode;
	private final module.XGModule module;
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
		this.module = null;
	}

	public XGValue(XGOpcode opc, module.XGModule mod) throws InvalidXGAddressException
	{	this.opcode = opc;
		this.module = mod;
		this.address = new XGAddress(opc.getAddress().getHi().getValue(), mod.getAddress().getMid().getValue(), opc.getAddress().getLo().getMin());
		if(!this.address.isFixed()) throw new InvalidXGAddressException("no valid value-address: " + this.address);
		if(opc.isMutable())
		{	this.parameters = PARAMETERTABLES.get(opc.getParameterTableName());
			if(this.parameters == null) throw new RuntimeException(ATTR_PARAMETERS + opc.getParameterTableName() + " not found for value " + this.getTag());
		}
		else
		{	this.parameters = new XGParameterTable();
			this.parameters.put(DEF_SELECTORVALUE, new XGParameter(opc.getConfig()));
		}

		if(opc.hasMutableDefaults())
		{	this.defaults = DEFAULTSTABLE.get(opc.getDefaultsTableName());
			if(this.defaults == null) throw new RuntimeException(ATTR_DEFAULTS + opc.getDefaultsTableName() + " not found for value " + this.getTag());
		}
		else
		{	this.defaults = new XGDefaultsTable(opc.getTag());
			this.defaults.put(XGDefaultsTable.NO_ID, DEF_SELECTORVALUE, opc.getConfig().getValueAttribute(ATTR_DEFAULT, 0));
		}
		this.module.getValues().add(this);
	}

	public void initValueDepencies() throws InvalidXGAddressException
	{	if(this.opcode.isMutable())
		{	XGValue psv = this.module.getValues().get(this.opcode.getParameterSelectorTag());
			if(psv == null) throw new RuntimeException(ATTR_PARAMETERSELECTOR + this.opcode.getParameterSelectorTag() + " not found for value " + this.getTag());
			this.parameterSelector = psv;
			this.parameterSelector.addValueListener((XGValue val)->{this.notifyParameterListeners();});
		}
		else this.parameterSelector = DEFAULTSELECTOR;

		if(this.opcode.hasMutableDefaults())
		{	XGValue dsv = this.module.getValues().get(this.opcode.getDefaultSelectorTag());
			if(dsv != null)
			{	this.defaultSelector = dsv;
				this.defaultSelector.addValueListener((XGValue)->{this.setDefaultValue();});
			}
			else
			{	LOG.warning(ATTR_DEFAULTSELECTOR + " " + this.opcode.getDefaultSelectorTag() + " not found for value " + this.getTag());//TODO: drumset hat zwar mutableDefaults aber kann keinen festen defaultSelector (multipartProgram) haben
				this.defaultSelector = DEFAULTSELECTOR;
			}
		}
		else this.defaultSelector = DEFAULTSELECTOR;

		//if(this.getTag().equals("mp_program")) this.addValueListener(XGProgramBuffer::bufferProgram);
		//if(this.getTag().equals("mp_partmode")) this.addValueListener(XGProgramBuffer::restoreProgram);
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
	{	if(this.hasChanged()) for(XGValueChangeListener l : this.valueListeners) l.contentChanged(this);
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

	public XGModule getModule()
	{	return this.module;
	}

	public void setDefaultValue()
	{	int id;
		try
		{	id = this.address.getMid().getValue();
		}
		catch(InvalidXGAddressException e)
		{	LOG.info(e.getMessage());
			id = DEF_SELECTORVALUE;
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

	public boolean hasChanged()
	{	return !this.index.equals(this.oldIndex);
	}

/**
 * setzt nach Validierung den XGTable-Index des XGValue auf den Wert i ohne die XAction-Kette auszulösen
 * @param i	Index
 */
	public void setIndex(Integer i)
	{	this.oldIndex = this.index;
		XGParameter p = this.getParameter();
		if(p != null) this.index = p.validate(i);
		this.notifyValueListeners();
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
 */
	public void setEntry(XGTableEntry e)
	{	this.setValue(e.getValue());
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
 */
	public void setValue(int v)
	{	XGParameter p = this.getParameter();
		if(p == null) return;
		this.setIndex(p.getTranslationTable().getIndex(v, Preference.FALLBACK));
	}

/**
 * setzt den übergebenen Entry im Value und durchläuft die Action-Kette
 * @param e Entry
 */
	public void editEntry(XGTableEntry e)
	{	this.setEntry(e);
		this.actions();
	}

/**
 * setzt den übergebenen Index im Value und startet die Action-Kette
 * @param i index
 */
	public void editIndex(int i)
	{	this.setIndex(i);
		this.actions();
	}

/**
 * addiert nach Validierung/Limitierung den übergebenen Wert zum Index und durchläuft die Action-Kette
 * @param diff Differenz
 */
	public void addIndex(int diff)
	{	this.editIndex(this.index + diff);
	}

/**
 * schaltet um zwischen Min- und MaxIndex und durchläuft die Action-Kette
 */
	public void toggleIndex()
	{	XGParameter p = this.getParameter();
		if(this.index == p.getMinIndex()) this.editIndex(p.getMaxIndex());
		else this.editIndex(p.getMinIndex());
	}
/**
 * sendet den Value mittels XGMessageParameterChange
 */
	public void sendAction()
	{	try
		{	new XGMessageParameterChange(STORE, XGMidi.getMidi(), this).transmit();
		}
		catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
	}

/**
 * sendet den Value mittels XGMessageBulkDump
 */
	public void bulkAction()
	{	XGAddress adr = this.getAddress();
		try
		{	XGMessageBulkDump msg = new XGMessageBulkDump(STORE, XGMidi.getMidi(), new XGAddress(adr.getHi(), adr.getMid(), this.getOpcode().getAddress().getLo()));
			msg.encodeLSB(msg.getBaseOffset(), msg.getBulkSize(), this.getValue());
			msg.setChecksum();
			msg.transmit();
		}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
	}

	public String getInfo()
	{	XGParameter p = this.getParameter();
		if(p != null) return p.getName() + "=" + this;
		else return "no parameter info";
	}

	private void actions()
	{	for(XACTION a : this.opcode.getActions())
		{	switch(a)
			{	case change: 			this.sendAction(); break;	//send via XGMessageParameterChange (normal)
				case dump:				this.bulkAction(); break;	//bulk via XGMessageBulkDump (voice-programs)
				case none:				break;
				case buffer_program:	XGProgramBuffer.bufferProgram(this); break;
				case restore_program:	XGProgramBuffer.restoreProgram(this); break;
				default:				LOG.info("unknown action: " + a.name() + " for value: " + this.getInfo());
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
