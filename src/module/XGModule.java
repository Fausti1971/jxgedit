package module;

import java.awt.event.ActionEvent;import java.util.LinkedHashSet;
import java.util.Set;
import adress.XGInvalidAddressException;
import adress.XGAddressableSet;
import adress.XGIdentifiable;import application.*;
import bulk.XGBulk;import bulk.XGBulkDumper;import bulk.XGBulkType;import device.XGMidi;import gui.XGMainWindow;import static module.XGModuleType.MODULE_TYPES;
import msg.*;import table.XGRealTable;import static table.XGTable.TABLES;import static table.XGTableConstants.TABLE_FX_PARTS;import table.XGTableEntry;import tag.*;
import value.*;import javax.sound.midi.InvalidMidiDataException;import javax.swing.*;

public class XGModule implements Comparable<XGModule>, XGModuleConstants, XGLoggable, XGBulkDumper, XGIdentifiable, XGTagable, XGMessenger
{
	public static void init()
	{	for(XGModuleType mt : MODULE_TYPES)
		{	for(int id : mt.getAddressRange().getMid())
			{	try
				{	mt.getModules().add(newModule(mt, id));
				}
				catch( InvalidMidiDataException | XGInvalidAddressException e)
				{	LOG.warning(e.getMessage());
				}
			}
			LOG.info(mt.getModules().size() + " " + mt + "-Modules initialized");
		}
	}

	public static XGModule newModule(XGModuleType mt, int id)throws InvalidMidiDataException, XGInvalidAddressException
	{	if("ins".equals(mt.getTag())) return new XGInsertionModule(mt, id);
		return new XGModule(mt, id);
	}

/***************************************************************************************************************/

	private final int id;
	private final XGModuleType type;
	final XGAddressableSet<XGBulk> bulks = new XGAddressableSet<>();

	XGModule(XGModuleType mt, int id) throws InvalidMidiDataException, XGInvalidAddressException
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

	public void copy()
	{	XGClippboard c = this.type.getClippboard();
		c.clear();
		this.transmitAll(c);
	}

	public void paste()
	{	XGClippboard c = this.getType().getClippboard();
		this.requestAll(c);
	}

	//public void paste()
	//{	XGClippboard c = this.type.getClippboard();
	//	if(c.isEmpty())
	//	{	JOptionPane.showMessageDialog(null, "clippboard is empty");
	//		return;
	//	}
	//	for(XGMessageBulkDump m : c)
	//	{	try
	//		{	if(this.type instanceof XGDrumsetModuleType) m.setHi(this.type.addressRange.getHi().getValue());
	//			m.setMid(this.id);
	//			this.submit(m);
	//		}
	//		catch(XGMessengerException | XGInvalidAddressException e)
	//		{	LOG.warning(e.getMessage());
	//			return;
	//		}
	//	}
	//	LOG.info(c.size() + " messages transmitted to " + this);
	//	int res = JOptionPane.showConfirmDialog(XGMainWindow.MAINWINDOW, " Transmit " + c + " ?");
	//	if(res == JOptionPane.YES_OPTION) this.transmitAll(XGMidi.getMidi());
	//}

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


	public void submit(XGMessageBulkDump res) throws XGMessengerException
	{	this.bulks.get(res.getAddress()).submit(res);
	}

	public void submit(XGMessageBulkRequest req) throws XGMessengerException
	{	this.bulks.get(req.getAddress()).submit(req);
	}

	@Override public String toString()
	{	if(this.type instanceof XGDrumsetModuleType) return ((XGDrumsetModuleType)this.type).getDrumname(this.id);
		else return this.type.getName() + " " + this.type.idTranslator.getByValue(this.getID());
	}

	public void close(){}

	@Override public XGAddressableSet<XGBulk> getBulks(){ return this.bulks;}

	public int compareTo(XGModule module){ return Integer.compare(this.id, module.id);}

	public int getID(){	return this.id;}

	public String getTag(){	return this.type.getTag();}
}
