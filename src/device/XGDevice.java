package device;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.*;
import adress.XGAddressConstants;
import adress.XGAddressableSet;
import application.JXG;
import application.XGStrings;import config.XGConfigurable;import file.XGDatafile;
import bulk.XGBulk;import bulk.XGBulkDumper;import gui.XGMainWindow;import module.XGModule;
import module.XGModuleType;
import static module.XGModuleType.MODULE_TYPES;
import msg.*;
import value.XGProgramBuffer;
import xml.XGProperty;import xml.XMLNode;import xml.XMLNodeConstants;

public class XGDevice implements  XGBulkDumper, XGConfigurable, XGMessenger, XMLNodeConstants
{
	public static XGDevice DEVICE = null;
	private static final String WARNSTRING = "This will reset all parameters!";
	static int DEF_SYSEXID = 0;
	public static String DEF_DEVNAME = "XG";

	public static void init()
	{	XMLNode xml = JXG.config.getChildNodeOrNew(TAG_DEVICE);
		DEVICE = new XGDevice(xml);
		for(XMLNode m : xml.getChildNodes(TAG_INIT_MESSAGE))
		{	try
			{	if(m.getTextContent().length() == 0) continue;
				SysexMessage msg = new SysexMessage();
				byte[] array = XGStrings.fromHexString(m.getTextContent().toString());
				msg.setMessage(array, array.length);
				XGMidi.getMidi().transmit(msg);
				LOG.info("transmitting " + TAG_INIT_MESSAGE + ": " + XGStrings.toHexString(array));
			}
			catch(NumberFormatException | InvalidMidiDataException e)
			{	LOG.severe(e.getMessage());
				JOptionPane.showMessageDialog(null, "Init Message Failure: " + e.getMessage());
			}
		}
	}

/***************************************************************************************************************************/

//	private final StringBuffer name;
	private final XMLNode config;
	private int info1, info2;
	private XGDatafile defaultFile;
	private int sysexID = 0;
//	private XGWindow childWindow;

	private XGDevice(XMLNode cfg)
	{	this.sysexID = cfg.getIntegerAttribute(ATTR_SYSEXID, DEF_SYSEXID);
		this.config = cfg;
		LOG.info("device initialized: " + this);
	}

	public XGProperty getName(){	return this.config.getAttributes().getOrNew(ATTR_NAME, new XGProperty(ATTR_NAME, DEF_DEVNAME));}

	public int getSysexID(){	return this.sysexID;}

	public void setSysexID(int id)
	{	this.sysexID = id & 0xF;
		config.setIntegerAttribute(ATTR_SYSEXID, this.sysexID);
	}

	public void requestInfo()	//SystemInfo ignoriert parameterrequest!;
	{	try
		{	//USEM Idendity Request: SOX=F0, Non-Realtime=7E, DeviceID=7F, SubID1=06, SubID2=01, EOX=F7//anders als MU500 ignoriert MU80 diesen Request
			//USEM Identity Reply: SubID2=02
			//byte[] msg = new byte[]{(byte)SOX,0x7E,0x7F,0x06,0x01,(byte)EOX};
			//m = new SysexMessage();
			//m.setMessage(msg, msg.length);
			//XGMidi.getMidi().transmit(m);
			XGMessageBulkRequest m = new XGMessageBulkRequest(this, XGAddressConstants.XGMODELNAMEADRESS);
			XGMidi.getMidi().submit(m);
			if(m.isResponsed())
			{	XGResponse r = m.getResponse();
				int offs = r.getBaseOffset();
				String s = r.getString(offs, offs + 14);
				this.config.setStringAttribute(ATTR_NAME, s.trim());
				this.info1 = r.decodeLSB(offs + 14);
				this.info2 = r.decodeLSB(offs + 15);
			}
			else JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, "no response for " + m);
		}
		catch( InvalidMidiDataException |  XGMessengerException e)
		{	LOG.severe(e.getMessage());
		}
	}

	public void resetXG(boolean send, boolean ask)
	{	int answer = javax.swing.JOptionPane.CANCEL_OPTION;
		if(ask) answer = JOptionPane.showConfirmDialog(gui.XGMainWindow.MAINWINDOW, WARNSTRING);
		if(answer == javax.swing.JOptionPane.CANCEL_OPTION || answer == javax.swing.JOptionPane.NO_OPTION) return;
		try
		{	if(send) XGMidi.getMidi().submit(new XGMessageParameterChange(this, new byte[]{0,0,0,0,0,0,0x7E,0,0}, true));
			for(XGModuleType mt : MODULE_TYPES) mt.resetValues();
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
			for(XGModuleType mt : MODULE_TYPES) mt.resetValues();
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
			for(XGModuleType mt : MODULE_TYPES) mt.resetValues();
		}
		catch(InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
		XGProgramBuffer.reset();
	}

	@Override public void submit(XGMessageBulkDump msg)throws XGMessengerException
	{	XGBulk b = this.getBulks().get(msg.getAddress());
		if(b != null) b.submit(msg);
		else throw new XGMessengerException("no matching Bulk found for " + msg);
	}

	@Override public void submit(XGMessageBulkRequest req)throws XGMessengerException
	{	XGBulk b = this.getBulks().get(req.getAddress());
		if(b != null) b.submit(req);
		else throw new XGMessengerException("no matching Bulk found for " + req);
	}

	@Override public String toString(){	return this.getName().getValue().toString();}

	@Override public void close(){	XGMidi.getMidi().close();}

	@Override public XGAddressableSet<XGBulk> getBulks()
	{	XGAddressableSet<XGBulk> set = new XGAddressableSet<>();
		for(XGModuleType mt : MODULE_TYPES)
			for(XGModule mi : mt.getModules())
				set.addAll(mi.getBulks());
		return set;
	}

	@Override public XMLNode getConfig(){	return this.config;}

	@Override public void propertyChanged(XGProperty attr){	LOG.info(attr.toString());}
}