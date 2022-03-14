package value;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.XGLoggable;
import device.*;
import module.*;
import static module.XGModuleType.TYPES;
import msg.*;
import parm.XGDefaultsTable;
import static parm.XGDefaultsTable.DEFAULTSTABLE;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;
import parm.XGParameterTable;
import static parm.XGParameterTable.PARAMETERTABLES;
import parm.XGTable;
import parm.XGTableEntry;
import tag.*;

/**
 * 
 * @author thomas
 *
 */
public class XGValue implements XGParameterConstants, XGAddressable, Comparable<XGValue>, XGValueChangeListener, XGLoggable, XGTagable, XGMessenger
{
//	private static final XGPartmodeListener XGPARTMODELISTENER = new XGPartmodeListener();
	private static final XGValue DEF_DEFAULTSELECTOR = new XGFixedValue("defaultSelector", DEF_SELECTORVALUE);
	private static final int count = 0;

/**
* initialisiert zu jedem Moduletype die angegebene Anzahl Instanzen (XGModule) inkl. der Bulk-Instanzen (XGAddress) und Opcode-Instanzen (XGValues inkl. Abhängigkeiten)
*/
	public static void init()
	{	XGAddressableSet<XGValue> pool = new XGAddressableSet<>();
	
		for(XGModuleType mt : TYPES)
		{	for(XGModule mod : mt.getModules())
			{	for(XGBulk blk : mod.getBulks())
				{	for(XGValueType opc : blk.getType().getOpcodes())
					{	try
						{	XGValue v = new XGValue(opc, blk);
							blk.getValues().add(v);
							pool.add(v);
						}
						catch(InvalidXGAddressException e)
						{	LOG.warning(e.getMessage());
						}
					}
				}
			}
		}
		for(XGValue v : pool)
		{	try	{	v.initDepencies();}
			catch(InvalidXGAddressException e)	{	LOG.warning(e.getMessage());}
		}
		for(XGValue v : pool) v.setDefaultValue();
		LOG.info(pool.size() + " values initialized");
	}

/***********************************************************************************************/

	private Integer oldValue = 0;
	private final XGAddress address;
	private final XGValueType type;
	private volatile XGBulk bulk;
	private final XGMessageCodec codec;
	private XGValue parameterSelector = null, defaultSelector = null;
	private final XGParameterTable parameters;
	private final XGDefaultsTable defaults;
	private final Set<XGValueChangeListener> valueListeners = new LinkedHashSet<>();
	private final Set<XGParameterChangeListener> parameterListeners = new LinkedHashSet<>();

	XGValue(String name, int v)
	{	this.address = XGALLADDRESS;
		this.parameterSelector = DEF_DEFAULTSELECTOR;
		this.defaultSelector = DEF_DEFAULTSELECTOR;
		this.parameters = null;
		this.defaults = null;
		this.type = null;
		this.codec = XGMessageCodec.LSB_CODEC;
	}

	public XGValue(XGValueType opc, XGBulk blk) throws InvalidXGAddressException
	{	this.type = opc;
		this.bulk = blk;
		this.codec = XGMessageCodec.getCodec(opc.getDataType());
		this.address = new XGAddress(opc.getAddress().getHi().getValue(), blk.getAddress().getMid().getValue(), opc.getAddress().getLo().getMin());
		if(!this.address.isFixed()) throw new InvalidXGAddressException("no valid value-address: " + this.address);

		if(opc.hasMutableParameters()){	this.parameters = PARAMETERTABLES.get(opc.getParameterTableName());}
		else
		{	this.parameters = new XGParameterTable(this.getTag());
			this.parameters.put(DEF_SELECTORVALUE, new XGParameter(opc.getConfig()));
		}

		if(opc.hasMutableDefaults()){	this.defaults = DEFAULTSTABLE.get(opc.getDefaultsTableName());}
		else
		{	this.defaults = new XGDefaultsTable(opc.getTag());
			this.defaults.put(XGDefaultsTable.NO_ID, DEF_SELECTORVALUE, opc.getConfig().getValueAttribute(ATTR_DEFAULT, 0));
		}
//		blk.getValues().add(this);
	}

	public void initDepencies() throws InvalidXGAddressException
	{
		if("mp_program".equals(this.getTag())) this.valueListeners.add(XGProgramBuffer::changeProgram);
		if("mp_partmode".equals(this.getTag())) this.valueListeners.add(XGProgramBuffer::changePartmode);

		if(this.type.hasMutableParameters())
		{	XGValue psv = this.bulk.getModule().getValues().get(this.type.getParameterSelectorTag());
			if(psv == null) throw new RuntimeException(ATTR_PARAMETERSELECTOR + " " + this.type.getParameterSelectorTag() + " not found for value " + this.getTag());
			this.parameterSelector = psv;
			this.parameterSelector.valueListeners.add((XGValue val)->{this.notifyParameterListeners();});
		}
		else this.parameterSelector = DEF_DEFAULTSELECTOR;

		if(this.type.hasMutableDefaults())
		{	String dst = this.type.getDefaultSelectorTag();
			XGValue dsv = this.bulk.getModule().getValues().get(dst);
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

	public Set<XGValueChangeListener> getValueListeners(){	return this.valueListeners;}

	public Set<XGParameterChangeListener> getParameterListeners(){	return this.parameterListeners;}

	public void notifyValueListeners(XGValue v){	for(XGValueChangeListener l : this.valueListeners) l.contentChanged(v);}

	public void notifyParameterListeners(){	for(XGParameterChangeListener l : this.parameterListeners) l.parameterChanged(this.getParameter());}

	public XGMessageCodec getCodec(){	return codec;}

	public XGValueType getType(){	return this.type;}

	public XGBulk getBulk(){	return this.bulk;}

	public int getSize(){	return this.type.getAddress().getLo().getSize();}

	public XGModule getModule(){	return this.bulk.getModule();}

	public void setDefaultValue()
	{	int id;
		try
		{	id = this.address.getMid().getValue();
		}
		catch(InvalidXGAddressException e)
		{	LOG.info(e.getMessage());
			id = DEF_SELECTORVALUE;
		}
		this.setValue(this.defaults.get(id, this.defaultSelector.getValue()), false, false);
	}

	public XGParameter getParameter(){	return this.parameters.getOrDefault(this.parameterSelector.getValue(), NO_PARAMETER);}

	@Override public XGAddress getAddress(){	return this.address;}

/**
 * returniert des Inhalts Index in der aktuellen XGTable des momentan zuständigen XGParameter
 */
	public int getIndex(){	return this.getParameter().getTranslationTable().getIndex(this.getValue());}

	public int getOldValue(){	return this.oldValue;}

	public boolean hasChanged(){	return !this.getValue().equals(this.oldValue);}

/**
 * setzt nach eventueller Validierung (limitize) den XGTable-Index des XGValue auf den Wert i
 * @param i	Index
 */
	private void setIndex(Integer i, boolean limitize, boolean action)
	{	XGParameter p = this.getParameter();
		if(limitize) i = p.getLimitizedIndex(i);
		this.setValue(p.getTranslationTable().getByIndex(i).getValue(), false, action);
	}

/**
 * @return returniert den momentan ausgewählten XGTable-Eintrag
 */
	public XGTableEntry getEntry()
	{	XGParameter p = this.getParameter();
		int v = this.getValue();
		if(p == null) return new XGTableEntry(v, "**" + v + "**");
		return p.getTranslationTable().getByValue(v);
	}

/**
 * 
 * @param e setzt den Inhalt des XGValue auf den Index des übergebenen XGTable-Eintrags
 */
	public void setEntry(XGTableEntry e, boolean limitize, boolean action){	this.setValue(e.getValue(), limitize, action);}

/**
 * @return aktueller Value aus Bulk
 */
	public Integer getValue()
	{	XGMessageBulkDump msg = this.bulk.getMessage();
		try
		{	int offset = msg.getBaseOffset() + this.address.getLo().getValue() - msg.getLo();
			return this.codec.decode(msg, offset, this.getSize());
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
			return this.oldValue;
		}
	}

/**
 * setzt den Inhalt des XGValue auf den übergebenen value
 * @param v Value
 */
	public void setValue(int v, boolean limitize, boolean action)
	{	XGMessageBulkDump msg = this.bulk.getMessage();
		if(limitize) v = this.getParameter().getLimitizedValue(v);
		try
		{	int offset = msg.getBaseOffset() + this.address.getLo().getValue() - msg.getLo();
			this.oldValue = this.getValue();
			this.codec.encode(msg, offset, this.getSize(), v);
		}
		catch(InvalidXGAddressException e){	e.printStackTrace();}
		if(this.hasChanged())
		{	this.notifyValueListeners(this);
			if(action) this.actions();
		}
	}

	public void setValue(String s, boolean action)
	{	if(this.getParameter() != null) this.setEntry(this.getParameter().getTranslationTable().getByName(s), true, action);
	}

/**
 * addiert den übergebenen Wert zum Index
 * @param diff Differenz
 */
	public void addIndex(int diff, boolean action){	this.setIndex(this.getIndex() + diff, true, action);}

/**
 * schaltet um zwischen Min- und MaxIndex
 */
	public void toggleIndex(boolean action)
	{	XGParameter p = this.getParameter();
		if(this.getIndex() == p.getMinIndex()) this.setIndex(p.getMaxIndex(), false, action);
		else this.setIndex(p.getMinIndex(), false, action);
	}
/**
 * sendet den Value mittels XGMessageParameterChange
 */
	public void sendAction()
	{	try
		{	XGMidi.getMidi().submit(new XGMessageParameterChange(this, this));
		}
		catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
	}

/**
 * sendet den Value mittels XGMessageBulkDump
 */
	public void bulkAction()
	{	try{	XGMidi.getMidi().submit(new XGMessageBulkDump(this, this));}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1){	LOG.severe(e1.getMessage());}
	}

/**
* erfragt den Value mittels XGMessageParameterRequest von MIDI
*/
	public void requestAction()
	{	try{	XGMidi.getMidi().submit(new XGMessageParameterRequest(this, this));}
		catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e){	e.printStackTrace();}
	}

	public String getInfo()
	{	XGParameter p = this.getParameter();
		if(p != null) return p.getName() + "=" + this;
		else return "no parameter info";
	}

	private void actions()
	{	for(XACTION a : this.type.getActions())
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

	@Override public void submit(XGResponse res) throws InvalidXGAddressException, XGMessengerException
	{	try
		{	int offset = res.getBaseOffset() + this.address.getLo().getValue() - res.getLo();
			this.setValue(this.codec.decode(res, offset, this.getSize()), false, false);
		}
		catch(InvalidXGAddressException e){	e.printStackTrace();}
	}

	public void submit(XGRequest req) throws InvalidXGAddressException, XGMessengerException
	{	LOG.info("not implemented yet...");
	}

	@Override public String toString()
	{	XGParameter p = this.getParameter();
		return this.getEntry().getName() + p.getUnit();
	}

	@Override public void close(){}

	@Override public int compareTo(XGValue o){	return this.getValue().compareTo(o.getValue());}

	@Override public void contentChanged(XGValue v){	if(v.hasChanged()) this.notifyValueListeners(v);}

	@Override public String getTag(){	return this.type.getTag();}
}
