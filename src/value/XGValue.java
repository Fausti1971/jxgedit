package value;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import adress.*;
import application.XGLoggable;
import bulk.XGBulk;import device.*;
import module.*;
import msg.*;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;
import table.XGTableEntry;
import tag.*;

/**
 * 
 * @author thomas
 *
 */
public abstract class XGValue implements XGParameterConstants, XGAddressable, Comparable<XGValue>, XGValueChangeListener, XGLoggable, XGTagable, XGMessenger, XGIdentifiable
{
/**
* initialisiert zu jedem Moduletype die angegebene Anzahl Instanzen (XGModule) inkl. der Bulk-Instanzen (XGAddress) und Opcode-Instanzen (XGValues inkl. Abhängigkeiten)
*/
	public static void init()
	{	XGAddressableSet<XGValue> pool = new XGAddressableSet<>();
	
		for(XGModuleType mt : XGModuleType.MODULE_TYPES)
		{	for(XGModule mod : mt.getModules())
			{	for(XGBulk blk : mod.getBulks())
				{	for(XGValueType opc : blk.getType().getValueTypes())
					{	XGValue v = newValue(opc, blk);
						blk.getValues().add(v);
						pool.add(v);
					}
				}
			}
		}
		for(XGValue v : pool) v.initDepencies();
		for(XGValue v : pool) v.setDefaultValue();
		LOG.info(pool.size() + " values initialized");
	}

	private static XGValue newValue(XGValueType vt, XGBulk blk)
	{	if(XGValueType.MP_PRG_VALUE_TAG.equals(vt.tag)) return new XGMultipartProgramValue(vt, blk);
		if(XGValueType.MP_PM_VALUE_TAG.equals(vt.tag)) return new XGMultipartModeValue(vt, blk);
		if(vt.hasMutableParameters() && vt.hasMutableDefaults()) return new XGMutableValue(vt, blk);
		if(vt.hasMutableParameters()) return new XGMutableParametersValue(vt, blk);
		if(vt.hasMutableDefaults()) return new XGMutableDefaultsValue(vt, blk);
		return new XGImmutableValue(vt, blk);
	}

/***********************************************************************************************/

	int value, oldValue = 0;
	final XGAddress address;
	final XGValueType type;
	final XGBulk bulk;
	final Set<XGValueChangeListener> valueListeners = new LinkedHashSet<>();
	final Set<XGParameterChangeListener> parameterListeners = new LinkedHashSet<>();

	XGValue(int v)
	{	this.address = XGALLADDRESS;
		this.type = null;
		this.bulk = null;
		this.value = v;
	}

	public XGValue(XGValueType type, XGBulk blk)
	{	this.type = type;
		this.bulk = blk;
		this.address = new XGAddress(blk.getAddress().getHiValue(), blk.getID(), type.lo);
	}

	public abstract void initDepencies();

	public Set<XGValueChangeListener> getValueListeners(){	return this.valueListeners;}

	public Set<XGParameterChangeListener> getParameterListeners(){	return this.parameterListeners;}

	public void notifyValueListeners(XGValue v){	for(XGValueChangeListener l : this.valueListeners) l.contentChanged(v);}

	public void notifyParameterListeners(){	for(XGParameterChangeListener l : this.parameterListeners) l.parameterChanged(this.getParameter());}

	public XGMessageCodec getCodec(){	return this.type.codec;}

	public XGValueType getType(){	return this.type;}

	public XGBulk getBulk(){	return this.bulk;}

	public int getSize(){	return this.type.getSize();}

	public XGModule getModule(){	return this.bulk.getModule();}

	public void setDefaultValue(){	this.setValue(this.getDefaultValue(), false, false);}

	public abstract int getDefaultValue();

	public abstract XGParameter getParameter();

	@Override public XGAddress getAddress(){	return this.address;}

/**
 * returniert des Inhalts Index in der aktuellen XGTable des momentan zuständigen XGParameter
 */
	public int getIndex(){	return this.getParameter().getTranslationTable().getIndex(this.getValue());}

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
	public Integer getValue(){	return this.value;}

/**
 * setzt den Inhalt des XGValue in der Message des Bulks auf den übergebenen value
 * @param v Value
 */
	public void setValue(int v, boolean limitize, boolean action)
	{	if(limitize) v = this.getParameter().getLimitizedValue(v);
		this.oldValue = this.getValue();
		this.value = v;
		if(this.hasChanged())
		{	this.notifyValueListeners(this);
			if(action) this.type.action.accept(this);
		}
	}

	public void setValue(String s, boolean action) throws NumberFormatException
	{	if(this.getParameter() != null && s != null)
			this.setEntry(this.getParameter().getTranslationTable().getByName(s), true, action);
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
		catch(XGMessengerException | InvalidMidiDataException e)
		{	e.printStackTrace();
		}
	}

	public void noneAction()
	{
	}

	public String getInfo()
	{	XGParameter p = this.getParameter();
		if(p != null) return p.getName() + "=" + this;
		else return this.getTag();
	}

	@Override public void submit(XGMessageBulkDump res) throws XGMessengerException
	{	int offset = res.getBaseOffset() + (this.address.getLoValue() - res.getAddress().getLoValue());
		this.setValue(this.type.codec.decode(res, offset, this.getSize()), false, false);
	}

	@Override public void submit(XGMessageParameterChange res)
	{	this.setValue(this.type.codec.decode(res, res.getBaseOffset(), this.getSize()), false, false);
	}

	public void submit(XGMessageParameterRequest req)throws XGMessengerException
	{	try
		{	XGMessageParameterChange res = new XGMessageParameterChange(this, this);
			if(req.setResponsedBy(res)) req.getSource().submit(res);
		}
		catch(InvalidMidiDataException e)
		{	throw new XGMessengerException(e.getMessage());
		}
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
