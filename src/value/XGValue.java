package value;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import application.XGLoggable;
import device.*;
import module.XGDrumsetModuleType;import module.XGModule;
import module.XGModuleType;
import msg.*;
import parm.XGDefaultsTable;
import static parm.XGDefaultsTable.DEFAULTSTABLE;
import parm.XGOpcode;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;
import static parm.XGParameterConstants.XACTION.change_partmode;
import static parm.XGParameterConstants.XACTION.change_program;
import parm.XGParameterTable;
import static parm.XGParameterTable.PARAMETERTABLES;
import parm.XGTable;
import parm.XGTableEntry;
import tag.*;
import static value.XGValueStore.STORE;

/**
 * 
 * @author thomas
 *
 */
public class XGValue implements XGParameterConstants, XGAddressable, Comparable<XGValue>, XGValueChangeListener, XGLoggable, XGCategorizeable, XGTagable
{
	private static final XGValue DEF_DEFAULTSELECTOR = new XGFixedValue("defaultSelector", DEF_SELECTORVALUE);
	private static final int count = 0;

/***********************************************************************************************/

	private Integer index = 0, oldIndex = 0;//Index des XGTableEntry in der (im XGParameter anhängenden) XGTable
	private final XGAddress address;
	private final XGOpcode opcode;
	private final XGModule module;
	private XGValue parameterSelector = null, defaultSelector = null;
	private final XGParameterTable parameters;
	private final XGDefaultsTable defaults;
	private final Set<XGValueChangeListener> valueListeners = new LinkedHashSet<>();
	private final Set<XGParameterChangeListener> parameterListeners = new LinkedHashSet<>();

	XGValue(String name, int v)
	{	this.address = XGALLADDRESS;
		this.index = v;
		this.parameterSelector = DEF_DEFAULTSELECTOR;
		this.defaultSelector = DEF_DEFAULTSELECTOR;
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
		{	this.parameters = new XGParameterTable(this.getTag());
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

	public void initDepencies() throws InvalidXGAddressException
	{
		if("mp_program".equals(this.opcode.getTag())) this.opcode.getActions().add(change_program);
		if("mp_partmode".equals(this.opcode.getTag())) this.opcode.getActions().add(change_partmode);
//TODO: Designfehler (beim sequentiellen Laden oder Empfangen eines Dumps wird zuerst (1) das Program am falschen ProgramBuffer gesetzt, da der Partmode erst danach (7) gesetzt wird);
//Lösung: Umbau auf XGBulkdump als Datenhalter
		if(this.opcode.isMutable())
		{	XGValue psv = this.module.getValues().get(this.opcode.getParameterSelectorTag());
			if(psv == null) throw new RuntimeException(ATTR_PARAMETERSELECTOR + " " + this.opcode.getParameterSelectorTag() + " not found for value " + this.getTag());
			this.parameterSelector = psv;
			this.parameterSelector.valueListeners.add((XGValue val)->{this.notifyParameterListeners();});
		}
		else this.parameterSelector = DEF_DEFAULTSELECTOR;

		if(this.opcode.hasMutableDefaults())
		{	String dst = this.opcode.getDefaultSelectorTag();
			XGValue dsv = this.module.getValues().get(dst);
			if(dsv != null)
			{	this.defaultSelector = dsv;
				this.defaultSelector.valueListeners.add((XGValue)->{this.setDefaultValue();});
			}
			else if("ds_program".equals(dst))
			{	XGModuleType t = this.getModule().getType();
				if(t instanceof XGDrumsetModuleType)
				{	this.defaultSelector = ((XGDrumsetModuleType)t).getProgramListener();
					this.defaultSelector.valueListeners.add((XGValue)->{this.setDefaultValue();});
				}
			}
			else
			{	LOG.warning(ATTR_DEFAULTSELECTOR + " " + dst + " not found for value " + this.getTag());
				this.defaultSelector = DEF_DEFAULTSELECTOR;
			}
		}
		else this.defaultSelector = DEF_DEFAULTSELECTOR;
	}

	public Set<XGValueChangeListener> getValueListeners()
	{	return this.valueListeners;
	}

	public Set<XGParameterChangeListener> getParameterListeners()
	{	return this.parameterListeners;
	}

	public void notifyValueListeners(XGValue v)
	{	for(XGValueChangeListener l : this.valueListeners) l.contentChanged(v);
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
	{	try
		{	return this.parameters.getOrDefault(this.parameterSelector.getValue(), NO_PARAMETER);
		}
		catch(NullPointerException e)
		{	System.out.println(this.getTag());
			return NO_PARAMETER;
		}
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
		if(p != null) this.index = p.getLimitizedIndex(i);
		if(this.hasChanged()) this.notifyValueListeners(this);
	}

/**
 * @return returniert den momentan ausgewählten XGTable-Eintrag
 */
	public XGTableEntry getEntry()
	{	XGParameter p = this.getParameter();
		if(p == null) return null;
		return p.getTranslationTable().getByIndex(this.index);
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
* returniert den Value des zuvor gewählten XGTable-Eintrags
*/
	public int getOldValue()
	{	return this.getParameter().getTranslationTable().getByIndex(this.oldIndex).getValue();
	}

/**
 * setzt den Inhalt des XGValue auf den Index des zum übergebenen value passenden XGTable-Eintrags ohne Action
 * @param v Value
 */
	public void setValue(int v)
	{	XGParameter p = this.getParameter();
		if(p == null) return;
		XGTable t = p.getTranslationTable();
		this.setIndex(t.getIndex(v, t.getMinIndex()));
	}

/**
 * setzt den übergebenen Entry im Value und durchläuft die Action-Kette
 * @param e Entry
 */
	public void editEntry(XGTableEntry e)
	{	this.setEntry(e);
		if(this.hasChanged()) this.actions();
	}

/**
 * setzt den übergebenen Index im Value und startet die Action-Kette
 * @param i index
 */
	public void editIndex(int i)
	{	this.setIndex(i);
		if(this.hasChanged()) this.actions();
	}

/**
 * addiert den übergebenen Wert zum Index und durchläuft die Action-Kette
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

/**
* erfragt den Value mittels XGMessageParameterRequest von MIDI
*/
	public void requestAction()
	{	try
		{	XGMessageParameterRequest req = new XGMessageParameterRequest(STORE, XGMidi.getMidi(), this);
			req.getDestination().request(req);
			if(req.isResponsed()) STORE.submit(req.getResponse());
		}
		catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e)
		{	e.printStackTrace();
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
				case change_program:	XGProgramBuffer.changeProgram(this); break;
				case change_partmode:	XGProgramBuffer.changePartmode(this); break;
				case none:				break;
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
	{	return this.index.compareTo(o.index);
	}

	@Override public void contentChanged(XGValue v)
	{	if(v.hasChanged()) this.notifyValueListeners(v);
	}

	@Override public String getCategory()
	{	return this.opcode.getCategory();
	}

	public String getTag()
	{	return this.opcode.getTag();
	}

	//@Override public int compare(XGValue value,XGValue t1)
	//{	return value.compareTo(t1);
	//}
}
