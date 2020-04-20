package value;
import java.util.HashSet;
import java.util.Map;
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
import parm.XGOpcode;
import parm.XGParameter;
import parm.XGParameterConstants;
/**
 * 
 * @author thomas
 *
 */
public class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAddressable, XGValueChangeListener, XGAddressableSetListener
{
	private static Logger log = Logger.getAnonymousLogger();

	public static void init(XGDevice dev)
	{	for(XGOpcode opc : dev.getOpcodes())
		{	for(int h : opc.getAddress().getHi())
			{	for(int m : opc.getAddress().getMid())
				{	try
					{	XGValue v = new XGValue(dev, opc, new XGAddress(h, m, opc.getAddress().getLo().getMin()));
						dev.getValues().add(v);
					}
					catch(InvalidXGAddressException e)
					{	e.printStackTrace();
					}
				}
			}
		}
	}

/***********************************************************************************************/

	private int content;
	private final XGMessenger source;
	private final XGAddress address;
	private final XGOpcode opcode;
	private final XGParameter parameter;
	private final XGValue parameterMaster;
	private final Set<XGValueChangeListener> listeners = new HashSet<>();

	public XGValue(String name, int v)
	{	this.source = null;
		this.address = XGALLADDRESS;
		this.parameter = new XGParameter(name,  v);
		this.content = v;
		this.parameterMaster = null;
		this.opcode = null;
		log.info("dummy value initialized: " + name);
	}

	public XGValue(XGMessenger src, XGOpcode opc, XGAddress adr) throws InvalidXGAddressException
	{	if(!adr.isFixed()) throw new InvalidXGAddressException("no valid value-address: " + adr);
		this.source = src;
		this.address = adr;
		this.opcode = opc;
		this.parameter = src.getDevice().getParameters().getOrDefault(opc.getParameterID(), XGParameterConstants.DUMMY_PARAMETER);
		if(this.parameter.isMutable())
		{	this.parameterMaster = src.getDevice().getValues().get(this.parameter.getMasterAddress().complement(this.address));
			this.parameterMaster.addListener(this);
		}
		else this.parameterMaster = null;
		this.content = 0;
		log.info("value initialized: " + this.getInfo());
	}

/*	public XGValue(XGFixedParameter prm, XGResponse msg) throws InvalidXGAddressException
	{	this.source = msg.getSource();
		this.address = new XGAddress(msg.getAddress().getHi().getValue(), msg.getAddress().getMid().getValue(), prm.getAddress().getLo().getMin());
		this.parameter = prm;
		this.content = this.decodeBytes(msg);
	}
*/
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

	public XGParameter getParameter()
	{	if(this.parameter.isMutable())
		{	int progNr = this.parameterMaster.getContent(),
				index = this.parameter.getIndex();
			Map<Integer, XGParameter> map = this.source.getDevice().getParameterSets().get(progNr);
			if(map != null && map.containsKey(index)) return map.get(index);
			else return DUMMY_PARAMETER;
		}
		else return this.parameter;
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

	public void encodeBytes(XGResponse msg, int o) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.address.getLo().getValue();
		int size = this.opcode.getAddress().getLo().getSize();
		switch(this.opcode.getDataType())
		{	default:
			case LSB:	msg.encodeLSB(offset, size, o); break;
			case LSN:	msg.encodeLSN(offset, size, o); break;
		}
	}

	protected int validate(int i)
	{	i = Math.min(i, this.getParameter().getMaxValue());
		i = Math.max(i, this.getParameter().getMinValue());
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
	{	XGParameter p = this.getParameter();
		return p.getLongName() + " = " + p.getValueTranslator().translate(this);
	}

	@Override public String toString()
	{	return this.getParameter().getValueTranslator().translate(this);
	}

	@Override public int compareTo(XGValue o)
	{	return this.address.compareTo(o.address);
	}

	@Override public void contentChanged(XGValue v)
	{	this.notifyListeners();
	}

	@Override public void setChanged(XGAddressable t)
	{	this.notifyListeners();
	}
}
