package value;
import java.util.HashSet;
import java.util.Set;
import javax.sound.midi.MidiUnavailableException;
import adress.InvalidXGAdressException;
import adress.XGAdress;
import adress.XGAdressable;
import device.XGMidi;
import msg.XGMessageException;
import msg.XGMessageParameterChange;
import msg.XGMessenger;
import obj.XGObjectInstance;
import opcode.XGOpcode;
import parm.XGParameter;
import parm.XGParameterConstants;
import tag.XGTagable;

public abstract class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAdressable, XGTagable
{	
	public static XGValue factory(XGMessenger src, XGAdress adr) throws InvalidXGAdressException
	{	XGOpcode o = src.getDevice().getOpcodes().getOrDefault(adr, new XGOpcode(adr));
		switch(o.getValueClass())
		{	case Integer:	return new XGIntegerValue(src, adr);
			case Image:		return new XGImageValue(src, adr);
			case String:	return new XGStringValue(src, adr);
			default:		throw new RuntimeException("unknown valueclass: " + o.getValueClass());
		}
	}

/***********************************************************************************************/

	private final XGMessenger source;
	private final XGAdress adress;
	private final XGObjectInstance instance;
	private final XGOpcode opcode;
	private final Set<XGValueChangeListener> listeners = new HashSet<>();
	
	protected XGValue(XGMessenger src, XGAdress adr) throws InvalidXGAdressException
	{	if(!adr.isValidAdress()) throw new InvalidXGAdressException("not a valid adress: " + adr);
		this.source = src;
		this.adress = adr;
		this.opcode = this.source.getDevice().getOpcodes().getOrDefault(adr, new XGOpcode(adr));
		this.instance = this.source.getDevice().getType(adr).getInstance(adr);
	}

	public void addListener(XGValueChangeListener l)
	{	this.listeners.add(l);}

	public void removeListener(XGValueChangeListener l)
	{	this.listeners.remove(l);}

	public synchronized void notifyListeners()
	{	for(XGValueChangeListener l : this.listeners) l.contentChanged(this);
	}

	public XGMessenger getSource()
	{	return this.source;
	}

	public String getTag()
	{	return this.opcode.getTag();
	}

	public XGAdress getAdress()
	{	return this.adress;
	}

	public XGObjectInstance getInstance()
	{	return this.instance;
	}

	public XGParameter getParameter()
	{	return this.source.getDevice().getParameters().getOrDefault(this.getTag(), new XGParameter(this.source.getDevice(), this.getTag()));
	}

	public XGOpcode getOpcode()
	{	return this.opcode;
	}

	protected abstract void validate(Object o) throws WrongXGValueTypeException;

	public abstract boolean setContent(Object o) throws WrongXGValueTypeException;

	public abstract Object getContent();

	public abstract boolean addContent(Object v) throws WrongXGValueTypeException;

	public void transmit(XGMidi midi) throws XGMessageException, InvalidXGAdressException, MidiUnavailableException
	{	XGMessageParameterChange m = new XGMessageParameterChange(midi, this);
		m.setDestination(midi);
		m.transmit();
	}

	protected abstract Object limitize(Object v);

	public String getInfo()
	{	return this.getClass().getSimpleName() + " "
			+ this.instance.getType() + " "
			+ this.instance + ", "
			+ this.getParameter() + " = "
			+ this.toString();
}
	@Override public abstract String toString();

	public int compareTo(XGValue o)
	{	return this.adress.compareTo(o.adress);}
}
