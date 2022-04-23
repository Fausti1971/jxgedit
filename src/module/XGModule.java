package module;

import java.util.LinkedHashSet;
import java.util.Set;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressField;
import adress.XGAddressable;
import adress.XGAddressableSet;
import application.*;
import bulk.XGBulk;import bulk.XGBulkDumper;import bulk.XGBulkType;import static module.XGModuleType.TYPES;
import table.XGRealTable;import static table.XGTable.TABLES;import static table.XGTableConstants.TABLE_FX_PARTS;import table.XGTableEntry;import tag.*;
import value.*;import javax.sound.midi.InvalidMidiDataException;

public class XGModule implements XGAddressable, Comparable<XGModule>, XGModuleConstants, XGLoggable, XGBulkDumper
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

	public static void init()
	{	for(XGModuleType mt : TYPES)
		{	for(int id : mt.getAddress().getMid())
			{	try
				{	mt.getModules().add(new XGModule(mt, id));
				}
				catch(InvalidXGAddressException | InvalidMidiDataException e)
				{	LOG.warning(e.getMessage());
				}
			}
			LOG.info(mt.getModules().size() + " " + mt + "-Modules initialized");
		}
	}

/***************************************************************************************************************/

	private final XGAddress address;
	private final XGModuleType type;
	private final XGAddressableSet<XGBulk> bulks = new XGAddressableSet<>();
//	private final XGTagableAddressableSet<XGValue> values = new XGTagableAddressableSet<>();

	public XGModule(XGModuleType mt, int id) throws InvalidXGAddressException, InvalidMidiDataException
	{	this.type = mt;
		this.address = new XGAddress(mt.getAddress().getHi(), new XGAddressField(id), mt.getAddress().getLo());
		for(XGBulkType bt : mt.getBulkTypes()){ this.bulks.add(XGBulk.newBulk(bt, this));}

		XGRealTable tab = (XGRealTable)TABLES.get(TABLE_FX_PARTS);
		String tag = mt.getTag();
		if("mp".equals(tag)) tab.add(new XGTableEntry(id, this.toString()));
		if("ad".equals(tag)) tab.add(new XGTableEntry(id + 64, this.toString()));
	}

	public XGModuleType getType(){ return this.type;}

	public XGTagableAddressableSet<XGValue> getValues()
	{	XGTagableAddressableSet<XGValue> set = new XGTagableAddressableSet<>();
		for(XGBulk blk : this.bulks) set.addAll(blk.getValues());
		return set;
	}

	public void resetValues(){ for(XGValue v : this.getValues()) v.setDefaultValue();}

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
	{	int id;
		String text = this.type.getName();
		try
		{	id = this.address.getMid().getValue();
		}
		catch(InvalidXGAddressException e)
		{	id = this.address.getMid().getMin();
		}
		if(this.type instanceof XGDrumsetModuleType) return ((XGDrumsetModuleType)this.type).getDrumname(id);
		if(this.type.getAddress().getMid().isRange()) return text + " " + (id + 1);
		else return text;
	}

	@Override public XGAddressableSet<XGBulk> getBulks(){ return this.bulks;}

	@Override public XGAddress getAddress(){ return this.address;}

	public int compareTo(XGModule module){ return this.address.compareTo(module.address);}
}
