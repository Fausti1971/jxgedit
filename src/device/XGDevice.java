package device;

import java.io.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressConstants;
import adress.XGAddressableSet;
import application.JXG;
import config.Configurable;import file.XGSysexFile;
import file.XGSysexFileConstants;
import gui.XGFileSelector;
import gui.XGWindow;
import module.XGModule;
import module.XGModuleType;
import static module.XGModuleType.TYPES;
import msg.*;
import value.XGProgramBuffer;
import static value.XGValueStore.STORE;
import xml.XGProperty;import xml.XMLNode;

public class XGDevice implements XGDeviceConstants, XGBulkDumper, Configurable
{
	public static XGDevice device = null;
//	public static XMLNode config = null;
	private String WARNSTRING = "This will reset all parameters!";

	public static void init()
	{	device = new XGDevice(JXG.config.getChildNodeOrNew(TAG_DEVICE));
	}

/***************************************************************************************************************************/

//	private final StringBuffer name;
	private final XMLNode config;
	private int info1, info2;
	private XGSysexFile defaultFile;
	private int sysexID = 0;
	private XGWindow childWindow;

	public XGDevice(XMLNode cfg)
	{	
//		this.name = cfg.getStringBufferAttributeOrNew(ATTR_NAME, DEF_DEVNAME);
//		this.defaultFileName = XGSysexFile.config.getStringBufferAttributeOrNew(ATTR_DEFAULTDUMPFILE, Paths.get(APPPATH).resolve(DEF_SYXFILENAME).toString());
		
		this.sysexID = cfg.getIntegerAttribute(ATTR_SYSEXID, DEF_SYSEXID);
		this.config = cfg;

//		try
//		{	this.defaultFile = new XGSysexFile(this, this.defaultFileName.toString());
//			this.defaultFile.parse();
//			this.transmitAll(this.defaultFile, this.values);
//		}
//		catch(IOException e)
//		{	LOG.severe(e.getMessage());
//			for(XGModuleType mt : this.getModuleTypes()) mt.resetValues();
//		}
		LOG.info("device initialized: " + this);
	}

	public void exit()
	{	XGMidi.getMidi().close();
	}

	public int getSysexID()
	{	return this.sysexID;
	}

	public void setSysexID(int id)
	{	this.sysexID = id & 0xF;
		config.setIntegerAttribute(ATTR_SYSEXID, this.sysexID);
	}

	public void requestInfo()	//SystemInfo ignoriert parameterrequest?!;
	{	XGRequest m;
		try
		{	m = new XGMessageBulkRequest(STORE, XGMidi.getMidi(), XGAddressConstants.XGMODELNAMEADRESS);
			try
			{	m.request();
				if(m.isResponsed())
				{	XGResponse r = m.getResponse();
					int offs = r.getBaseOffset();
					String s = r.getString(offs, offs + 14);
					this.config.setStringAttribute(ATTR_NAME, s.trim());
					this.info1 = r.decodeLSB(offs + 14);
					this.info2 = r.decodeLSB(offs + 15);
				}
				else JOptionPane.showMessageDialog(null, "no response for " + m);
			}
			catch(InvalidXGAddressException | XGMessengerException e)
			{	LOG.severe(e.getMessage());
			}
		}
		catch(InvalidXGAddressException | InvalidMidiDataException e)
		{	LOG.severe(e.getMessage());
		}
	}

	public void resetXG(boolean send, boolean ask)
	{	int answer = javax.swing.JOptionPane.CANCEL_OPTION;
		if(ask) answer = JOptionPane.showConfirmDialog(gui.XGMainWindow.window, WARNSTRING);
		if(answer == javax.swing.JOptionPane.CANCEL_OPTION || answer == javax.swing.JOptionPane.NO_OPTION) return;
		try
		{	if(send)new XGMessageParameterChange(STORE, XGMidi.getMidi(), new byte[]{0,0,0,0,0,0,0x7E,0,0}, true).transmit();
			for(XGModuleType mt : TYPES) mt.resetValues();
		}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
		XGProgramBuffer.reset();
	}

	public void resetGM(boolean send, boolean ask)
	{	int answer = javax.swing.JOptionPane.CANCEL_OPTION;
		if(ask) answer = JOptionPane.showConfirmDialog(gui.XGMainWindow.window, WARNSTRING);
		if(answer == javax.swing.JOptionPane.CANCEL_OPTION || answer == javax.swing.JOptionPane.NO_OPTION) return;
		try
		{	if(send) XGMidi.getMidi().transmit(new SysexMessage(new byte[]{(byte)0xF0,0x7E,0x7F,0x09,0x01,(byte)0xF7}, 6));
			for(XGModuleType mt : TYPES) mt.resetValues();
		}
		catch(InvalidMidiDataException e)
		{	e.printStackTrace();
		}
		XGProgramBuffer.reset();
	}

	public void resetAll(boolean send, boolean ask)
	{	int answer = javax.swing.JOptionPane.CANCEL_OPTION;
		if(ask) answer = JOptionPane.showConfirmDialog(gui.XGMainWindow.window, WARNSTRING);
		if(answer == javax.swing.JOptionPane.CANCEL_OPTION || answer == javax.swing.JOptionPane.NO_OPTION) return;
		try
		{	if(send) new XGMessageParameterChange(STORE, XGMidi.getMidi(), new byte[]{0,0,0,0,0,0,0x7F,0,0}, true).transmit();
			for(XGModuleType mt : TYPES) mt.resetValues();
		}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
		XGProgramBuffer.reset();
	}

	public void load()
	{	XMLNode last = XGSysexFile.config.getLastChildOrNew(TAG_ITEM);
		XGFileSelector fs = new XGFileSelector(last.getTextContent(), "open sysex file...", "open", XGSysexFileConstants.SYX_FILEFILTER);
		switch(fs.select(this.childWindow))
		{	case JFileChooser.APPROVE_OPTION:
			{	XGSysexFile f;
				try
				{	f = new XGSysexFile(last.getTextContent().toString());
					f.parse();
					this.transmitAll(f, STORE);
					f.close();
				}
				catch(IOException e)
				{	LOG.severe(e.getMessage());
				}
				break;
			}
			case JFileChooser.CANCEL_OPTION:
			{	last.removeNode();
				LOG.severe("fileselection aborted");
				break;
			}
		}
	}

	public void save()
	{	XMLNode last = XGSysexFile.config.getLastChildOrNew(TAG_ITEM);
		XGFileSelector fs = new XGFileSelector(last.getTextContent(), "save sysex file...", "save", XGSysexFileConstants.SYX_FILEFILTER);
		switch(fs.select(this.childWindow))
		{	case JFileChooser.APPROVE_OPTION:
			{	XGSysexFile f;
				try
				{	f = new XGSysexFile(last.getTextContent().toString());
					f.parse();// damit die in einer Datei enthaltenen (aber vom ValueStore nicht unterstützen) Messages erhalten bleiben
					this.transmitAll(STORE, f);
					f.save();
					f.close();
				}
				catch(IOException e)
				{	LOG.severe(e.getMessage());
				}
				break;
			}
			case JFileChooser.CANCEL_OPTION:
			{	last.removeNode();
				LOG.warning("fileselection aborted");
				break;
			}
		}
	}

	@Override public String toString()
	{	return this.config.getStringAttribute(ATTR_NAME);
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	XGAddressableSet<XGAddress> set = new XGAddressableSet<>();
		for(XGModuleType mt : TYPES)
			for(XGModule mi : mt.getModules())
				set.addAll(mi.getBulks());
		return set;
	}

	public XMLNode getConfig()
	{	return this.config;
	}

	public void propertyChanged(XGProperty attr)
	{
	}
}