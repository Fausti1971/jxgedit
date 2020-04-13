package value;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSetListener;
import device.XGDevice;
import module.XGModule;
import msg.XGMessenger;
import msg.XGResponse;
import opcode.XGOpcode;
import opcode.XGOpcodeConstants;
import parm.XGParameter;
import tag.XGTagable;
/**
 * Quasi Inkarnation eines XGOpcodes; dazu benötigt: message (datenbasis) und opcode (interpretation);
 * @author thomas
 *
 */
public class XGValue implements XGOpcodeConstants, Comparable<XGValue>, XGAddressable, XGTagable, XGValueChangeListener, XGAddressableSetListener
{
	private static Logger log = Logger.getAnonymousLogger();

	public static void init(XGDevice dev)
	{	for(XGOpcode opc : dev.getOpcodes())
		{	for(int h : opc.getAddress().getHi())
			{	for(int m : opc.getAddress().getMid())
				{	try
					{	XGValue v = new XGValue(dev, new XGAddress(h, m, opc.getAddress().getLo().getMin()));
						dev.getValues().add(v);
					}
					catch(InvalidXGAddressException e)
					{	e.printStackTrace();
					}
				}
			}
		}
		log.info(dev.getValues().size() + " Values initalized in Device " + dev);
	}

/***********************************************************************************************/

	private int content;
	private final XGMessenger source;
	private final XGAddress address;
	private final XGOpcode opcode;
	private final XGValueDependency dependency;
	private final Set<XGValueChangeListener> listeners = new HashSet<>();

	public XGValue(XGMessenger src, XGAddress adr) throws InvalidXGAddressException//für Prototypen als Response für manuell erzeugte ParameterRequests
	{	if(!adr.isFixed()) throw new InvalidXGAddressException("Test");
		this.source = src;
		this.address = adr;
		this.opcode = src.getDevice().getOpcodes().getOrDefault(adr, new XGOpcode(src.getDevice(), adr));
		this.dependency = null;
		this.content = 0;
	}

/*	public XGValue(XGMessenger src, XGOpcode opc)//für während der Initialisierung manuell erzeugte XGValues (0)
	{	this.opcode = opc;
		this.address = this.module.getAddress().complement(opc.getAddress());
		if(!this.address.isFixed()) throw new InvalidXGAddressException("value needs an fixed address: " + this.address);
		this.message = this.module.getDevice().getData().get(this.module.getAddress().complement(this.opcode.getBulk().getAddress()));
		this.module.getDevice().getData().addListener(this);
		this.dependency = null;//TODO:
	}
*/
	public XGValue(XGOpcode opc, XGResponse msg) throws InvalidXGAddressException
	{	this.source = msg.getSource();
		this.address = new XGAddress(msg.getAddress().getHi().getValue(), msg.getAddress().getMid().getValue(), opc.getAddress().getLo().getMin());
		this.opcode = opc;
		this.content = this.decodeBytes(msg);
		this.dependency = null;
	}

	public void addListener(XGValueChangeListener l)
	{	this.listeners.add(l);
	}

	public void removeListener(XGValueChangeListener l)
	{	this.listeners.remove(l);
	}

	public synchronized void notifyListeners()
	{	for(XGValueChangeListener l : this.listeners) l.contentChanged(this);
	}

	public XGMessenger getSource()
	{	return this.source;
	}

	public XGOpcode getOpcode()
	{	return this.opcode;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public XGModule getModule()
	{	return this.opcode.getModule();
	}

	public int decodeBytes(XGResponse msg) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.opcode.getAddress().getLo().getMin() - msg.getLo();
		int size = this.opcode.getAddress().getLo().getSize();
		switch(this.opcode.getDataType())
		{	default:
			case LSB:	return msg.decodeLSB(offset, size);
			case LSN:	return msg.decodeLSN(offset, size);
		}
	}

	public void encodeBytes(XGResponse msg, Object o) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.opcode.getAddress().getLo().getValue();
		int size = this.opcode.getAddress().getLo().getSize();
		switch(this.opcode.getDataType())
		{	default:
			case LSB:	msg.encodeLSB(offset, size, (int)o); break;
			case LSN:	msg.encodeLSN(offset, size, (int)o); break;
		}
	}

	protected int validate(int i)
	{	XGParameter p = this.opcode.getParameter(this.dependency.getValue());
		i = Math.min(i, p.getMaxValue());
		i = Math.max(i, p.getMinValue());
		return i;
	}

/**
 * setzt nach Validierung den Content des XGValue auf den Wert i
 * @param i	Wert
 * @return	true, wenn sich der Inhalt änderte
 * @throws InvalidXGAddressException
 */
	public boolean setContent(int i) throws InvalidXGAddressException
	{	int old = this.getContent();
		this.content = this.validate(i);
		return this.content != old;
	}

	public int getContent()
	{	return this.content;
	}

	public boolean addContent(int i)
	{	return false;//TODO
	}

	public String getInfo()
	{	return this.opcode.getParameter(this.dependency.getValue()).getShortName() + " = " + this.toString();
	}

	@Override public String toString()
	{	return "tag: " + this.getTag();
	}

	@Override public int compareTo(XGValue o)
	{	return this.address.compareTo(o.address);
	}

	@Override public void contentChanged(XGValue v)
	{	this.notifyListeners();
	}

	@Override public String getTag()
	{	return this.opcode.getTag();
	}

	@Override public void setChanged(XGAddressable t)
	{	this.notifyListeners();
	}
}
