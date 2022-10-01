package value;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import adress.*;
import application.XGLoggable;
import bulk.XGBulk;import device.*;
import module.*;
import static module.XGModuleType.MODULE_TYPES;
import msg.*;
import table.XGDefaultsTable;
import static table.XGDefaultsTable.DEFAULTSTABLES;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;
import table.XGParameterTable;
import static table.XGParameterTable.PARAMETERTABLES;
import table.XGTableEntry;
import tag.*;

/**
 * 
 * @author thomas
 *
 */
public class XGValue implements XGParameterConstants, XGAddressable, Comparable<XGValue>, XGValueChangeListener, XGLoggable, XGTagable, XGMessenger
{
	private static final XGValue DEF_DEFAULTSELECTOR = new XGFixedValue("defaultSelector", DEF_SELECTORVALUE);

/**
* initialisiert zu jedem Moduletype die angegebene Anzahl Instanzen (XGModule) inkl. der Bulk-Instanzen (XGAddress) und Opcode-Instanzen (XGValues inkl. Abhängigkeiten)
*/
	public static void init()
	{	XGAddressableSet<XGValue> pool = new XGAddressableSet<>();
	
		for(XGModuleType mt : MODULE_TYPES)
		{	for(XGModule mod : mt.getModules())
			{	for(XGBulk blk : mod.getBulks())
				{	for(XGValueType opc : blk.getType().getValueTypes())
					{	XGValue v = new XGValue(opc, blk);
						blk.getValues().add(v);
						pool.add(v);
					}
				}
			}
		}
		for(XGValue v : pool)
		{	try
			{	v.initDepencies();}
			catch(InvalidXGAddressException e)
			{	LOG.warning(e.getMessage());}
		}
		for(XGValue v : pool) v.setDefaultValue();
		LOG.info(pool.size() + " values initialized");
	}

/***********************************************************************************************/

	private Integer oldValue = 0;
	private final XGAddress address;
	private final XGValueType type;
	private volatile XGBulk bulk;
	private XGValue parameterSelector = null, defaultSelector = null;
	private final XGParameterTable parameters;
	private final XGDefaultsTable defaults;
	private final Set<XGValueChangeListener> valueListeners = new LinkedHashSet<>();
	private final Set<XGParameterChangeListener> parameterListeners = new LinkedHashSet<>();

	XGValue(int v)
	{	this.address = XGALLADDRESS;
		this.parameterSelector = DEF_DEFAULTSELECTOR;
		this.defaultSelector = DEF_DEFAULTSELECTOR;
		this.parameters = null;
		this.defaults = null;
		this.type = null;
	}

	public XGValue(XGValueType type, XGBulk blk)
	{	this.type = type;
		this.bulk = blk;
		this.address = new XGAddress(blk.getAddress().getHiValue(), blk.getID(), type.lo);

		if(type.hasMutableParameters())
		{	this.parameters = PARAMETERTABLES.get(type.parameterTableName);
			if(this.parameters == null) throw new RuntimeException("parameter-table \"" + type.parameterTableName + "\" for " + type.getTag() + " not found");
		}
		else
		{	this.parameters = new XGParameterTable(this.getTag());
			this.parameters.put(DEF_SELECTORVALUE, new XGParameter(type.getConfig()));
		}

		if(type.hasMutableDefaults()){	this.defaults = DEFAULTSTABLES.get(type.defaultsTableName);}
		else
		{	this.defaults = new XGDefaultsTable(type.getTag());
			this.defaults.put(XGDefaultsTable.NO_ID, DEF_SELECTORVALUE, type.getConfig().getValueAttribute(ATTR_DEFAULT, 0));
		}
	}

	public void initDepencies() throws InvalidXGAddressException
	{
		if("mp_program".equals(this.getTag())) this.valueListeners.add(XGProgramBuffer::changeProgram);
		if("mp_partmode".equals(this.getTag())) this.valueListeners.add(XGProgramBuffer::changePartmode);

		if(this.type.hasMutableParameters())
		{	XGValue psv = this.bulk.getModule().getValues().get(this.type.parameterSelectorTag);
			if(psv == null) throw new RuntimeException(ATTR_PARAMETERSELECTOR + " " + this.type.parameterSelectorTag + " not found for value " + this.getTag());
			this.parameterSelector = psv;
			this.parameterSelector.valueListeners.add((XGValue val)->this.notifyParameterListeners());
		}
		else this.parameterSelector = DEF_DEFAULTSELECTOR;

		if(this.type.hasMutableDefaults())
		{	String dst = this.type.defaultSelectorTag;
			XGValue dsv = this.bulk.getModule().getValues().get(dst);
			if(dsv != null)
			{	this.defaultSelector = dsv;
				this.defaultSelector.valueListeners.add((XGValue)->this.setDefaultValue());
			}
			else if("ds_program".equals(dst))
			{	XGModuleType t = this.getModule().getType();
				if(t instanceof XGDrumsetModuleType)
				{	this.defaultSelector = ((XGDrumsetModuleType)t).getProgramListener();
					this.defaultSelector.valueListeners.add((XGValue)->this.setDefaultValue());
				}
			}
			else if("id".equals(dst))
			{	this.defaultSelector = new XGFixedValue(this.getTag(), this.getModule().getID());
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

	public XGMessageCodec getCodec(){	return this.type.codec;}

	public XGValueType getType(){	return this.type;}

	public XGBulk getBulk(){	return this.bulk;}

	public int getSize(){	return this.type.getSize();}

	public XGModule getModule(){	return this.bulk.getModule();}

	public void setDefaultValue(){	this.setValue(this.defaults.get(this.getID(), this.defaultSelector.getValue()), false, false);}

	public XGParameter getParameter(){	return this.parameters.getOrDefault(this.parameterSelector.getValue(), NO_PARAMETER);}

	@Override public XGAddress getAddress(){	return this.address;}

/**
 * returniert des Inhalts Index in der aktuellen XGTable des momentan zuständigen XGParameter
 */
	public int getIndex(){	return this.getParameter().getTranslationTable().getIndex(this.getValue());}

//TODO: untersuche: wenn der Parameter sich ändert ohne dass sich der Wert ändert
	public boolean hasChanged(){	return !this.getValue().equals(this.oldValue);}

/**
 * setzt nach eventueller Validierung (limitize) den XGTable-Index des XGValue auf den Wert i
 * @param i	Index
 */
	public void setIndex(Integer i, boolean limitize, boolean action)
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
		if(!p.isValid()) return new XGTableEntry(v, "**" + v + "**");
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
	{	XGMessageBulkDump msg = this.bulk.getUncheckedMessage();
		try
		{	int offset = msg.getBaseOffset() + this.address.getLo().getValue() - msg.getLo();
			return this.type.codec.decode(msg, offset, this.getSize());
		}
		catch(InvalidXGAddressException e)
		{	e.printStackTrace();
			return this.oldValue;
		}
	}

/**
 * setzt den Inhalt des XGValue in der Message des Bulks auf den übergebenen value
 * @param v Value
 */
	public void setValue(int v, boolean limitize, boolean action)
	{	XGMessageBulkDump msg = this.bulk.getUncheckedMessage();
		if(limitize) v = this.getParameter().getLimitizedValue(v);
		try
		{	int offset = msg.getBaseOffset() + this.address.getLo().getValue() - msg.getLo();
			this.oldValue = this.getValue();
			this.type.codec.encode(msg, offset, this.getSize(), v);
		}
		catch(InvalidXGAddressException e){	e.printStackTrace();}
		if(this.hasChanged())
		{	this.notifyValueListeners(this);
			if(action) this.type.action.accept(this);
		}
	}

	public void setValue(String s, boolean action) throws NumberFormatException
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
		catch( InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
	}

/**
 * sendet den Value mittels XGMessageBulkDump
 */
	public void dumpAction()
	{	try
		{	XGMidi.getMidi().submit(new XGMessageBulkDump(this, this));
		}
		catch(InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
	}

/**
* erfragt den Value mittels XGMessageParameterRequest von MIDI
*/
	public void requestAction()
	{	try
		{	XGMidi.getMidi().submit(new XGMessageParameterRequest(this, this));
		}
		catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e)
		{	e.printStackTrace();
		}
	}

	public void noneAction()
	{
	}

	public String getInfo()
	{	XGParameter p = this.getParameter();
		if(p != null) return p.getName() + "=" + this;
		else return "no parameter info";
	}

	@Override public void submit(XGResponse res)throws XGMessengerException
	{	if(res instanceof XGMessageParameterChange)
		{	try
			{	int offset = res.getBaseOffset() + this.address.getLo().getValue() - res.getLo();
				this.setValue(this.type.codec.decode(res, offset, this.getSize()), false, false);
			}
			catch(InvalidXGAddressException e){	e.printStackTrace();}
		}
		else throw new XGMessengerException(this, res);
	}

	public void submit(XGRequest req)
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
