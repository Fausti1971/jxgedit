package value;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressable;
import adress.XGBulkDump;
import device.XGDevice;
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
public class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAddressable, XGValueChangeListener, ChangeableContent<Integer>
{
	private static Logger log = Logger.getAnonymousLogger();

/***********************************************************************************************/

	private Integer index;
	private final XGMessenger source;
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

	public int decodeMessage(XGResponse msg) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.opcode.getAddress().getLo().getMin() - msg.getLo();
		int size = this.getSize();
		switch(this.opcode.getDataType())
		{	default:
			case LSB:	return msg.decodeLSB(offset, size);
			case LSN:	return msg.decodeLSN(offset, size);
		}
	}

	public void encodeMessage(XGResponse msg, int o) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.address.getLo().getValue() - msg.getLo();
		int size = this.getSize();
		switch(this.opcode.getDataType())
		{	default:
			case LSB:	msg.encodeLSB(offset, size, o); break;
			case LSN:	msg.encodeLSN(offset, size, o); break;
		}
	}

	@Override public Integer getContent()
	{	return this.index;
	}

/**
 * setzt nach Validierung den Content des XGValue auf den Wert i
 * @param i	Wert
 * @return	true, wenn sich der Inhalt änderte
 */
	@Override public boolean setContent(Integer i)
	{	int old = this.index;
		XGParameter p = this.getParameter();
		if(p != null) this.index = p.validate(i);
		boolean changed = this.index != old;
		if(changed) this.notifyListeners();
		return changed;
	}

	public XGTableEntry getEntry()
	{	XGParameter p = this.getParameter();
		if(p == null) return null;
		return p.getTranslationTable().getByIndex(this.getContent());
	}

	public boolean setEntry(XGTableEntry e)
	{	return this.setValue(e.getValue());
	}

	public int getValue()
	{	return this.getEntry().getValue();
	}

	public boolean setValue(int v)
	{	XGParameter p = this.getParameter();
		if(p == null) return true;
		return this.setContent(p.getTranslationTable().getIndex(v));
	}

	public void transmit()
	{	XGDevice dev = this.getSource().getDevice();
		try
		{	new XGMessageParameterChange(dev, dev.getMidi(), this).transmit();
		}
		catch(InvalidXGAddressException | InvalidMidiDataException e1)
		{	e1.printStackTrace();
		}
	}

	public String getInfo()
	{	XGParameter p = this.getParameter();
		if(p != null) return p.getShortName() + ": " + this;
		else return "no parameter info";
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
