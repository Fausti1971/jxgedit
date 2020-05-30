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
import parm.XGParameterChangeListener;
import parm.XGParameterConstants;
import parm.XGTable;
/**
 * 
 * @author thomas
 *
 */
public class XGValue implements XGParameterConstants, Comparable<XGValue>, XGAddressable, XGValueChangeListener, XGAddressableSetListener, ChangeableContent<Integer>
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
					{	log.info(e.getMessage());
					}
				}
			}
		}
	}

/***********************************************************************************************/

	private Integer content;
	private final XGMessenger source;
	private final XGAddress address;
	private final XGOpcode opcode;
	private final XGParameter parameter;
	private XGParameter mutableParameter;
	private final XGValue parameterMaster;
	private final Set<XGValueChangeListener> valueListeners = new HashSet<>();
	private final Set<XGParameterChangeListener> parameterListeners = new HashSet<>();

	public XGValue(String name, int v)
	{	this.source = null;
		this.address = XGALLADDRESS;
		this.parameter = DUMMY_PARAMETER;
		this.content = v;
		this.parameterMaster = null;
		this.opcode = null;
//		log.info("dummy value initialized: " + name);
	}

	public XGValue(XGMessenger src, XGOpcode opc, XGAddress adr) throws InvalidXGAddressException
	{	if(!adr.isFixed()) throw new InvalidXGAddressException("no valid value-address: " + adr);
		this.source = src;
		this.address = adr;
		this.opcode = opc;
		this.parameter = src.getDevice().getParameters().getOrDefault(opc.getParameterID(), DUMMY_PARAMETER);
		if(this.parameter.isMutable())
		{	XGAddress a = this.parameter.getMasterAddress().complement(this.address);
			this.parameterMaster = src.getDevice().getValues().get(a);
			if(this.parameterMaster != null)
			{	this.parameterMaster.addValueListener(this);
				this.assignMutableParameter();
			}
			else log.info("master not found: " + a);
		}
		else this.parameterMaster = null;
		this.content = 0;
//		log.info("value initialized: " + this.getInfo());
	}

/*	public XGValue(XGFixedParameter prm, XGResponse msg) throws InvalidXGAddressException
	{	this.source = msg.getSource();
		this.address = new XGAddress(msg.getAddress().getHi().getValue(), msg.getAddress().getMid().getValue(), prm.getAddress().getLo().getMin());
		this.parameter = prm;
		this.content = this.decodeBytes(msg);
	}
*/
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

	private void assignMutableParameter()
	{	if(this.parameter.isMutable())
		{	int progNr = this.parameterMaster.getContent(),
				index = this.parameter.getIndex();
			Map<Integer, XGParameter> map = this.source.getDevice().getParameterSets().get(progNr);
			if(map != null)
			{	this.mutableParameter = map.get(index);
			}
			else this.mutableParameter = null;
			for(XGParameterChangeListener l : this.parameterListeners) l.parameterChanged(this.mutableParameter);
		}
	}

	public XGParameter getParameter()
	{	if(this.parameter.isMutable()) return this.mutableParameter;
		else return this.parameter;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	public XGModule getModule()
	{	return this.opcode.getModule();
	}

	public int decodeMessage(XGResponse msg) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.opcode.getAddress().getLo().getMin() - msg.getLo();
		int size = this.opcode.getDataSize();
		switch(this.opcode.getDataType())
		{	default:
			case LSB:	return msg.decodeLSB(offset, size);
			case LSN:	return msg.decodeLSN(offset, size);
		}
	}

	public void encodeMessage(XGResponse msg, int o) throws InvalidXGAddressException
	{	int offset = msg.getBaseOffset() + this.address.getLo().getValue() - msg.getLo();
		int size = this.opcode.getDataSize();
		switch(this.opcode.getDataType())
		{	default:
			case LSB:	msg.encodeLSB(offset, size, o); break;
			case LSN:	msg.encodeLSN(offset, size, o); break;
		}
	}

	@Override public Integer getContent()
	{	return this.content;
	}

/**
 * setzt nach Validierung den Content des XGValue auf den Wert i
 * @param i	Wert
 * @return	true, wenn sich der Inhalt änderte
 */
	@Override public boolean setContent(Integer i)
	{	int old = this.content;
		XGParameter p = this.getParameter();
		if(p != null) this.content = p.validate(i);
		boolean changed = this.content != old;
		if(changed) this.notifyListeners();
		return changed;
	}

	public boolean addContent(Integer i)
	{	int old = this.content;
		XGTable t = this.getParameter().getTranslationTable();
		if(i == 1) return this.setContent(t.nextKey(old));
		if(i == -1) return this.setContent(t.prevKey(old));
		log.info("cannot add content " + i);
		return false;
	}

	public String getInfo()
	{	XGParameter p = this.getParameter();
		if(p != null) return p.getLongName() + " = " + p.getTranslationTable().get(this.getContent());
		else return "no parameter info";
	}

	@Override public String toString()
	{	XGParameter p = this.getParameter();
		if(p.getUnit().isEmpty()) return p.getTranslationTable().get(this.getContent()).getName();
		else return p.getTranslationTable().get(this.getContent()).getName() + " " + p.getUnit();
	}

	@Override public int compareTo(XGValue o)
	{	return this.address.compareTo(o.address);
	}

	@Override public void contentChanged(XGValue v)
	{	if(v.equals(this.parameterMaster)) this.assignMutableParameter();
		else this.notifyListeners();
	}

	@Override public void setChanged(XGAddressable t)
	{	this.notifyListeners();
	}
}
