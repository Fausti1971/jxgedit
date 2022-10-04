package module;

import java.util.LinkedHashSet;
import java.util.Set;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import adress.XGIdentifiable;import application.*;
import bulk.XGBulk;import bulk.XGBulkDumper;import bulk.XGBulkType;import static module.XGModuleType.MODULE_TYPES;
import table.XGRealTable;import static table.XGTable.TABLES;import static table.XGTableConstants.TABLE_FX_PARTS;import table.XGTableEntry;import tag.*;
import value.*;import javax.sound.midi.InvalidMidiDataException;

public class XGModule implements Comparable<XGModule>, XGModuleConstants, XGLoggable, XGBulkDumper, XGIdentifiable, XGTagable
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
	{	for(XGModuleType mt : MODULE_TYPES)
		{	for(int id : mt.getAddressRange().getMid())
			{	try
				{	mt.getModules().add(newModule(mt, id));
				}
				catch( InvalidMidiDataException | InvalidXGAddressException e)
				{	LOG.warning(e.getMessage());
				}
			}
			LOG.info(mt.getModules().size() + " " + mt + "-Modules initialized");
		}
	}

	public static XGModule newModule(XGModuleType mt, int id)throws InvalidMidiDataException, InvalidXGAddressException
	{	if("ins".equals(mt.getTag())) return new XGInsertionModule(mt, id);
		return new XGModule(mt, id);
	}

/***************************************************************************************************************/

	private final int id;
	private final XGModuleType type;
	final XGAddressableSet<XGBulk> bulks = new XGAddressableSet<>();

	XGModule(XGModuleType mt, int id) throws InvalidMidiDataException, InvalidXGAddressException
	{	this.id = id;
		this.type = mt;
		for(XGBulkType bt : mt.getBulkTypes())
		{	this.bulks.add(new XGBulk(bt, this));
		}

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
	{	if(this.type instanceof XGDrumsetModuleType) return ((XGDrumsetModuleType)this.type).getDrumname(this.id);
		else return this.type.getName() + " " + this.type.idTranslator.getByValue(this.getID());
	}

	@Override public XGAddressableSet<XGBulk> getBulks(){ return this.bulks;}

	public int compareTo(XGModule module){ return Integer.compare(this.id, module.id);}

	public int getID(){	return this.id;}

	public String getTag(){	return this.type.getTag();}
}
