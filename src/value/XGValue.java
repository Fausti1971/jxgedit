package value;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import adress.XGAddressableSetListener;
import module.XGModule;
import module.XGModuleNotFoundException;
import msg.XGMessageBulkDump;
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

/***********************************************************************************************/

	private final XGAddress address;
	private XGMessageBulkDump message;
	private final XGOpcode opcode;
	private final XGModule module;
	private final XGValueDependency dependency;
	private final Set<XGValueChangeListener> listeners = new HashSet<>();

	public XGValue(XGModule mod, XGOpcode opc) throws XGModuleNotFoundException, InvalidXGAddressException
	{	this.opcode = opc;
		this.module = mod;
		this.address = mod.getAddress().complement(opc.getAddress());
		if(!this.address.isFixed()) throw new InvalidXGAddressException("value needs an fixed address: " + this.address);
		this.message = this.module.getDevice().getData().get(this.module.getAddress().complement(this.opcode.getBulk().getAddress()));
		this.module.getDevice().getData().addListener(this);
		this.dependency = null;//TODO:
	}

	public XGValue(XGMessenger src, XGAddress adr) throws InvalidXGAddressException//als Responseprototyp für manuell erzeugte XGParameterRequests
	{	this.address = adr;
		this.opcode = src.getDevice().getModules().get(adr).getBulks().get(adr).getOpcodes().get(adr);
		this.module = src.getDevice().getModules().get(adr);
		this.message = this.module.getDevice().getData().get(this.module.getAddress().complement(this.opcode.getBulk().getAddress()));
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

	public XGOpcode getOpcode()
	{	return this.opcode;
	}

	public XGMessageBulkDump getMessage()
	{	return this.message;
	}

	public XGMessenger getSource()
	{	return this.message.getSource();
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public XGModule getModule()
	{	return module;
	}

	public Object decodeBytes(XGResponse msg) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.opcode.getAddress().getLo().getValue();
		int size = this.opcode.getAddress().getLo().getSize();
		switch(this.opcode.getValueClass())
			{	case String:		return msg.getString(offset, size);
				case Image:			return null;
				default:
				case Integer:
					switch(this.opcode.getDataType())
					{	default:
						case LSB:	return msg.decodeLSB(offset, size);
						case LSN:	return msg.decodeLSN(offset, size);
					}
			}
	}

	public void encodeBytes(XGResponse msg, Object o) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.opcode.getAddress().getLo().getValue();
		int size = this.opcode.getAddress().getLo().getSize();
		switch(this.opcode.getValueClass())
			{	case String:	msg.setString(offset, size, this.toString());
				case Image:		return;
				default:
				case Integer:
					switch(this.opcode.getDataType())
					{	default:
						case LSB:	msg.encodeLSB(offset, size, (int)o); break;
						case LSN:	msg.encodeLSN(offset, size, (int)o); break;
					}
			}
	}

	protected Object validate(Object o) throws WrongXGValueTypeException
	{	switch(this.opcode.getValueClass())
		{	case Image:		if(o instanceof Image) return o;
			case Integer:	if(o instanceof Integer)
							{	int i = (int)o;
								XGParameter p = this.opcode.getParameter(this.dependency.getValue());
								i = Math.min(i, p.getMaxValue());
								i = Math.max(i, p.getMinValue());
								return i;
							}
			case String:	if(o instanceof String)
							{	String s = (String)o;
								s.substring(0, this.opcode.getAddress().getLo().getSize());
								return s;
							}
			default:		throw new WrongXGValueTypeException("unsupported value class: " + o.getClass().getSimpleName());
		}
	}


	public boolean setContent(Object o) throws WrongXGValueTypeException, InvalidXGAddressException
	{	Object old = this.getContent();
		o = this.validate(o);
		this.encodeBytes(this.getMessage(), o);
		return o.equals(old);
	}

	public Object getContent() throws InvalidXGAddressException
	{	return this.decodeBytes(this.getMessage());
	}

	public boolean addContent(Object v) throws WrongXGValueTypeException
	{	return false;//TODO
	}

	public String getInfo()
	{	return this.getClass().getSimpleName() + " "
			+ this.module + " "
			+ this.opcode.getParameter(this.dependency.getValue()) + " = "//TODO:
			+ this.toString();
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

	@Override public void setChanged(XGAddress adr)
	{	this.message = this.module.getDevice().getData().get(adr.complement(this.opcode.getBulk().getAddress()));
		this.notifyListeners();
	}
}
