package value;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import module.XGModule;
import module.XGModuleNotFoundException;
import msg.XGMessageBulkDump;
import msg.XGMessenger;
import msg.XGResponse;
import opcode.XGOpcode;
import opcode.XGOpcodeConstants;
import tag.XGTagable;
/**
 * Inkarnation eines XGOpcodes
 * @author thomas
 *
 */
public class XGValue implements XGOpcodeConstants, Comparable<XGValue>, XGAddressable, XGTagable, XGValueChangeListener
{
	private static Logger log = Logger.getAnonymousLogger();

/***********************************************************************************************/

	private final XGAddress address;
	private final XGMessageBulkDump message;
	private final XGOpcode opcode;
	private final XGModule module;
	private final XGValueDependency dependency;
	private final Set<XGValueChangeListener> listeners = new HashSet<>();

	public XGValue(XGModule mod, XGOpcode opc) throws XGModuleNotFoundException, InvalidXGAddressException
	{	this.opcode = opc;
		this.address = mod.getAddress().complement(opc.getAddress());
		if(!this.address.isValidAdress()) throw new InvalidXGAddressException("invalid adress: " + this.address);
		this.module = mod;
		this.message = this.module.getDevice().getMessages().get(this.module.getAddress().complement(this.opcode.getBulkAddress()));
		this.dependency = new XGValueDependency(this, module.getValues().get(this.opcode.getDependencyTag()), this.opcode.getDependencyType());
	}

	public XGValue(XGMessenger src, XGAddress adr) throws InvalidXGAddressException
	{	this.address = adr;
		this.opcode = src.getDevice().getOpcodes().get(adr);
		this.module = src.getDevice().getModule(adr);
		this.message = this.module.getDevice().getMessages().get(this.module.getAddress().complement(this.opcode.getBulkAddress()));
		this.dependency = new XGValueDependency(this, module.getValues().get(this.opcode.getDependencyTag()), this.opcode.getDependencyType());
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

	protected void validate(Object o) throws WrongXGValueTypeException
	{	switch(this.opcode.getValueClass())
		{	case Image:		if(o instanceof Image) return;
			case Integer:	if(o instanceof Integer) return;
			case String:	if(o instanceof String) return;
			default:		throw new WrongXGValueTypeException("unsupported valuetype: " + o.getClass().getSimpleName());
		}
	}

	public Object decodeBytes(XGResponse msg) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.address.getLo();
		switch(this.opcode.getValueClass())
			{	case String:		return msg.getString(offset, this.opcode.getByteCount());
				case Image:			return null;
				default:
				case Integer:
					switch(this.opcode.getDataType())
					{	default:
						case LSB:	return msg.decodeMidiBytesToInteger(offset, this.opcode.getByteCount());
						case LSN:	return msg.decodeLowerNibbles(offset, this.opcode.getByteCount());
					}
			}
	}

	public void encodeBytes(XGResponse msg, Object o) throws InvalidXGAddressException
	{	int offset = this.getAddress().getLo();
		switch(this.opcode.getValueClass())
			{	case String:	msg.setString(offset, this.opcode.getByteCount(), this.toString());
				case Image:		return;
				default:
				case Integer:
					switch(this.opcode.getDataType())
					{	default:
						case LSB:	msg.encodeMidiBytesFromInteger(offset, this.opcode.getByteCount(), (int)o); break;
						case LSN:	msg.encodeLowerNibblesFromInteger(offset, this.opcode.getByteCount(), (int)o); break;
					}
			}
	}

	public boolean setContent(Object o) throws WrongXGValueTypeException, InvalidXGAddressException
	{	Object old = this.getContent();
		this.validate(o);
		o = this.limitize(o);
		this.encodeBytes(this.getMessage(), o);
		return o.equals(old);
	}

	public Object getContent() throws InvalidXGAddressException
	{	return this.decodeBytes(this.getMessage());
	}

	public boolean addContent(Object v) throws WrongXGValueTypeException
	{	return false;//TODO
	}

	protected Object limitize(Object v)
	{	return v; //TODO
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
}
