package device;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.*;
import adress.InvalidXGAddressException;
import adress.XGAddressConstants;
import adress.XGAddressableSet;
import application.JXG;
import config.XGConfigurable;import file.XGSysexFile;
import module.XGBulk;import module.XGBulkDumper;import module.XGModule;
import module.XGModuleType;
import static module.XGModuleType.TYPES;
import msg.*;
import value.XGProgramBuffer;
import xml.XGProperty;import xml.XMLNode;

public class XGDevice implements XGDeviceConstants, XGBulkDumper, XGConfigurable, XGMessenger
{
	public static XGDevice device = null;
//	public static XMLNode config = null;
	private final String WARNSTRING = "This will reset all parameters!";

	public static void init()
	{	device = new XGDevice(JXG.config.getChildNodeOrNew(TAG_DEVICE));
	}

/***************************************************************************************************************************/

//	private final StringBuffer name;
	private final XMLNode config;
	private int info1, info2;
	private XGSysexFile defaultFile;
	private int sysexID = 0;
//	private XGWindow childWindow;

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

	public XGProperty getName(){	return this.config.getAttributes().getOrNew(ATTR_NAME, new XGProperty(ATTR_NAME, DEF_DEVNAME));}

	public int getSysexID(){	return this.sysexID;}

	public void setSysexID(int id)
	{	this.sysexID = id & 0xF;
		config.setIntegerAttribute(ATTR_SYSEXID, this.sysexID);
	}

	public void requestInfo()	//SystemInfo ignoriert parameterrequest?!;
	{	XGRequest m;
		try
		{	m = new XGMessageBulkRequest(this, XGAddressConstants.XGMODELNAMEADRESS);
			XGMidi.getMidi().submit(m);
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
		catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e)
		{	LOG.severe(e.getMessage());
		}
	}

	public void resetXG(boolean send, boolean ask)
	{	int answer = javax.swing.JOptionPane.CANCEL_OPTION;
		if(ask) answer = JOptionPane.showConfirmDialog(gui.XGMainWindow.MAINWINDOW, WARNSTRING);
		if(answer == javax.swing.JOptionPane.CANCEL_OPTION || answer == javax.swing.JOptionPane.NO_OPTION) return;
		try
		{	if(send) XGMidi.getMidi().submit(new XGMessageParameterChange(this, new byte[]{0,0,0,0,0,0,0x7E,0,0}, true));
			for(XGModuleType mt : TYPES) mt.resetValues();
		}
		catch(InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
		XGProgramBuffer.reset();
	}

	public void resetGM(boolean send, boolean ask)
	{	int answer = javax.swing.JOptionPane.CANCEL_OPTION;
		if(ask) answer = JOptionPane.showConfirmDialog(gui.XGMainWindow.MAINWINDOW, WARNSTRING);
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
		if(ask) answer = JOptionPane.showConfirmDialog(gui.XGMainWindow.MAINWINDOW, WARNSTRING);
		if(answer == javax.swing.JOptionPane.CANCEL_OPTION || answer == javax.swing.JOptionPane.NO_OPTION) return;
		try
		{	if(send) XGMidi.getMidi().submit(new XGMessageParameterChange(this, new byte[]{0,0,0,0,0,0,0x7F,0,0}, true));
			for(XGModuleType mt : TYPES) mt.resetValues();
		}
		catch(InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
		XGProgramBuffer.reset();
	}

	@Override public void submit(XGResponse msg) throws InvalidXGAddressException, XGMessengerException
	{	LOG.info("not implemented yet...");//TODO: finde bulk oder value anhand der adresse und übergebe msg
	}

	public void submit(XGRequest req) throws InvalidXGAddressException, XGMessengerException
	{	LOG.info("not implemented yet...");//TODO: finde bulk oder value anhand der adresse, erfrage und beantworte req
	}

	@Override public String toString(){	return this.getName().getValue().toString();}

	@Override public void close(){	XGMidi.getMidi().close();}

	@Override public XGAddressableSet<XGBulk> getBulks()
	{	XGAddressableSet<XGBulk> set = new XGAddressableSet<>();
		for(XGModuleType mt : TYPES)
			for(XGModule mi : mt.getModules())
				set.addAll(mi.getBulks());
		return set;
	}

	@Override public XMLNode getConfig(){	return this.config;}

	@Override public void propertyChanged(XGProperty attr)
	{	LOG.info(attr.toString());
	}
}