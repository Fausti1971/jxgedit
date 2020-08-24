package value;
import java.util.HashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressable;
import device.XGDevice;
import msg.XGBulkDump;
import msg.XGMessageParameterChange;
import msg.XGMessenger;
import msg.XGResponse;
import parm.XGOpcode;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;
import parm.XGTable;
import parm.XGTableEntry;
/**
 * 
 * @author thomas
 *
 */
public class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAddressable, XGValueChangeListener
{

/***********************************************************************************************/

/**
 * Index in der im Parameter angehängten XGTable
 */
	private Integer index;
	private XGMessenger source;
	private final XGAddress address;
	private final XGOpcode opcode;
	private final XGBulkDump bulk;
	private XGParameter parameter;
	private final XGValue parameterSelector;
	private final Set<XGValueChangeListener> valueListeners = new HashSet<>();
	private final Set<XGParameterChangeListener> parameterListeners = new HashSet<>();

	public XGValue(String name, int v)
	{	this.source = null;
		this.address = XGALLADDRESS;
		this.parameter = null;
		this.index = v;
		this.parameterSelector = null;
		this.opcode = null;
		this.bulk = null;
	}

	public XGValue(XGMessenger src, XGOpcode opc, XGBulkDump blk) throws InvalidXGAddressException
	{	this.source = src;
		this.bulk = blk;
		this.opcode = opc;
		this.address = new XGAddress(blk.getAddress().getHi(), blk.getAddress().getMid(), new XGAddressField(opc.getAddress().getLo().getMin()));
		if(!this.address.isFixed()) throw new InvalidXGAddressException("no valid value-address: " + this.address);
		if(this.opcode.getParameterSelectorAddress() != null)
		{	XGAddress a = this.opcode.getParameterSelectorAddress().complement(this.address);
			this.parameterSelector = src.getDevice().getValues().get(a);
			this.parameterSelector.addValueListener(this);
		}
		else this.parameterSelector = null;
		this.assignParameter();
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

	public synchronized void notifyListeners()
	{	for(XGValueChangeListener l : this.valueListeners) l.contentChanged(this);
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

	public XGBulkDump getBulk()
	{	return bulk;
	}

	public int getSize()
	{	return this.opcode.getAddress().getLo().getSize();
	}

	private void assignParameter()
	{	if(this.parameterSelector != null)
		{	int progNr = this.parameterSelector.getValue();
			this.parameter = this.opcode.getParameters().get(progNr);
			for(XGParameterChangeListener l : this.parameterListeners) l.parameterChanged(this.parameter);
		}
		else this.parameter = this.opcode.getParameters().get(DEF_SELECTORVALUE);
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
 * setzt nach Validierung den XGTable-Index des XGValue auf den Wert i
 * @param i	Index
 * @return	true, wenn sich der Inhalt änderte
 */
	public boolean setIndex(Integer i)
	{	this.actions(XACTION_BEFORE_EDIT);
		int old = this.index;
		XGParameter p = this.getParameter();
		if(p != null) this.index = p.validate(i);
		boolean changed = this.index != old;
		if(changed)
		{	this.notifyListeners();
			this.actions(XACTION_AFTER_EDIT);
		}
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
		return this.setIndex(p.getTranslationTable().getIndex(v));
	}

	public void transmit()
	{	this.actions(XACTION_BEFORE_SEND);
		XGDevice dev = this.getSource().getDevice();
		try
		{	new XGMessageParameterChange(this.source, dev.getMidi(), this).transmit();
		}
		catch(InvalidXGAddressException | InvalidMidiDataException e1)
		{	e1.printStackTrace();
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
			for(String s : set)
			{	switch(s)
				{	case("module_request"): this.getBulk().getModule().transmitAll(this.source.getDevice().getMidi(), this.source.getDevice().getValues());
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
	{	if(v.equals(this.parameterSelector)) this.assignParameter();
		else this.notifyListeners();
	}
}
