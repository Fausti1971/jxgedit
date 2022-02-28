package module;

import adress.InvalidXGAddressException;import adress.XGAddress;import adress.XGAddressable;import msg.XGBulkChangeListener;import msg.XGMessage;import msg.XGMessageBulkDump;import msg.XGMessageStore;import tag.XGTagable;import tag.XGTagableAddressableSet;import value.XGValue;import javax.sound.midi.InvalidMidiDataException;import java.util.HashMap;
import java.util.HashSet;import java.util.Map;
import java.util.Set;

public class XGBulk implements XGTagable, XGAddressable
{
	private final XGBulkType type;
	private final XGModule module;
	private volatile XGMessageBulkDump message;
	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<>();
	private final Map<Integer, Set<XGBulkChangeListener>> listeners = new HashMap<>();

	public XGBulk(XGBulkType type, XGModule mod)throws InvalidXGAddressException, InvalidMidiDataException
	{	this.type = type;
		this.module = mod;
		XGAddress adr = type.getAddress().complement(mod.getAddress());
		this.message = new XGMessageBulkDump(XGMessageStore.STORE, null, adr);
	}

	public XGBulkType getType(){	return this.type;}

	public String getTag(){	return this.type.getTag();}

	public XGTagableAddressableSet<XGValue> getValues(){	return this.values;}

	public XGModule getModule(){	return this.module;}

	public void setMessage(XGMessageBulkDump m)
	{	this.message = m;
		this.notifyAllListeners();
	}

	public XGMessageBulkDump getMessage(){	return this.message;}

	public void addListener(int offset, XGBulkChangeListener l)
	{	if(this.listeners.containsKey(offset)) this.listeners.get(offset).add(l);
		else
		{	Set<XGBulkChangeListener> set = new HashSet<>();
			set.add(l);
			this.listeners.put(offset, set);
		}
	}

	private void notifyAllListeners()
	{	for(int offset : this.listeners.keySet()) this.notifyListeners(offset);
	}

	private void notifyListeners(int offset)
	{	for(XGBulkChangeListener l : this.listeners.get(offset)) l.bulkChanged();
	}

	public XGAddress getAddress()
	{	return this.message.getAddress();
	}
}
