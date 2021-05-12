package module;

import java.util.LinkedHashSet;
import java.util.Set;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.*;
import msg.XGBulkDumper;
import static parm.XGParameterConstants.TABLE_FX_PARTS;
import static parm.XGTable.TABLES;
import parm.XGTableEntry;
import parm.XGRealTable;
import tag.*;
import value.*;
import static value.XGValueStore.STORE;

public class XGModule implements XGAddressable, XGModuleConstants, XGLoggable, XGBulkDumper
{
	static final Set<String> ACTIONS = new LinkedHashSet<>();

	static
	{	ACTIONS.add(ACTION_EDIT);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
		ACTIONS.add(ACTION_RESET);
	}

/***************************************************************************************************************/

	private final XGAddress address;
	private final XGModuleType type;
	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<>();

	public XGModule(XGModuleType mt, int id) throws InvalidXGAddressException
	{	this.type = mt;
		this.address = new XGAddress(mt.getAddress().getHi(), new XGAddressField(id), mt.getAddress().getLo());
		this.values.addAll(STORE.getAllIncluded(this.address));
		XGRealTable tab = (XGRealTable)TABLES.get(TABLE_FX_PARTS);
		String tag = mt.getTag();
		if("mp".equals(tag)) tab.add(new XGTableEntry(id, this.toString()));
		if("ad".equals(tag)) tab.add(new XGTableEntry(id + 64, this.toString()));
	}

	public XGModuleType getType()
	{	return this.type;
	}

	public XGTagableAddressableSet<XGValue> getValues()
	{	return this.values;
	}

	public String getTranslatedID()
	{	try
		{	return this.type.idTranslator.getByIndex(this.address.getMid().getValue()).getName();
		}
		catch(InvalidXGAddressException e)
		{	return this.address.getMid().toString();
		}
	}

	public void resetValues()
	{	for(XGValue v : this.getValues()) v.setDefaultValue();
	}

	//@Override public void actionPerformed(ActionEvent e)
	//{	XGDevice dev = this.type.getDevice();
	//	switch(e.getActionCommand())
	//	{	case ACTION_EDIT:		this.editWindow(); break;
	//		case ACTION_REQUEST:	new Thread(() -> {this.transmitAll(dev.getMidi(), dev.getValues());}).start(); break;
	//		case ACTION_TRANSMIT:	new Thread(() -> {this.transmitAll(dev.getValues(), dev.getMidi());}).start(); break;
	//		case ACTION_RESET:		if(JOptionPane.showConfirmDialog(XGWindow.getRootWindow(), "Do you really want to reset " + this, "Reset Module?", JOptionPane.CANCEL_OPTION) == JOptionPane.OK_OPTION) this.resetValues(); break;
	//		default:				JOptionPane.showMessageDialog(XGWindow.getRootWindow(), "action not implemented: " + e.getActionCommand());
	//	}
	//}

	@Override public String toString()
	{	return this.type.getName() + " " + this.getTranslatedID();
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	XGAddressableSet<XGAddress> set = new XGAddressableSet<>();
		for(XGAddress bd : this.type.getBulkAdresses())
		{	try
			{	set.add(bd.getAddress().complement(this.address));
			}
			catch(InvalidXGAddressException e)
			{	LOG.warning(e.getMessage());
			}
		}
		return set;
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}
}
