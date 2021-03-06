package device;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.*;
import java.nio.file.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressConstants;
import adress.XGAddressField;
import adress.XGAddressableSet;
import application.Configurable;
import static application.ConfigurationConstants.APPPATH;import application.JXG;
import file.XGSysexFile;
import file.XGSysexFileConstants;
import gui.XGFileSelector;
import gui.XGTemplate;
import gui.XGWindow;
import module.XGDrumsetModuleType;
import module.XGModule;
import module.XGModuleType;
import msg.*;
import parm.XGDefaultsTable;
import parm.XGOpcode;
import parm.XGParameterTable;
import parm.XGTable;
import tag.XGTagableSet;
import value.XGValue;
import value.XGValueStore;
import static value.XGValueStore.STORE;import xml.XMLNode;

public class XGDevice implements XGDeviceConstants, XGBulkDumper
{	private static XGDevice device = null;
	public static XMLNode config = null;

	public static XGDevice getDevice()
	{	if(device == null) XGDevice.init();
		return device;
	}

	public static void init()
	{	config = JXG.config.getChildNodeOrNew(TAG_DEVICE);
		device = new XGDevice(config);
	}

/***************************************************************************************************************************/

	private final StringBuffer name;
	private boolean isSelected = false;

	private int info1, info2;
	private XGSysexFile defaultFile;
	private int sysexID = 0;
	private XGWindow childWindow;

	public XGDevice(XMLNode cfg)
	{	this.name = cfg.getStringBufferAttributeOrNew(ATTR_NAME, DEF_DEVNAME);
//		this.defaultFileName = XGSysexFile.config.getStringBufferAttributeOrNew(ATTR_DEFAULTDUMPFILE, Paths.get(APPPATH).resolve(DEF_SYXFILENAME).toString());
		this.sysexID = cfg.getIntegerAttribute(ATTR_SYSEXID, DEF_SYSEXID);

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
		{	m = new XGMessageBulkRequest(XGMidi.getMidi(), XGMidi.getMidi(), XGAddressConstants.XGMODELNAMEADRESS);// TODO: Krücke, für dest existiert hier noch kein XGMessenger, der allerdings für getDevice() erforderlich ist
			try
			{	m.request();
				if(m.isResponsed())
				{	XGResponse r = m.getResponse();
					int offs = r.getBaseOffset();
					String s = r.getString(offs, offs + 14);
					this.name.replace(0, this.name.length(), s.trim());
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

	private void resetXG()
	{	try
		{	new XGMessageParameterChange(STORE, XGMidi.getMidi(), new byte[]{0,0,0,0,0,0,0x7E,0,0}, true).transmit();
			for(XGModuleType mt : XGModuleType.TYPES) mt.resetValues();
		}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
	}

	private void resetGM()
	{	try
		{	XGMidi.getMidi().transmit(new SysexMessage(new byte[]{(byte)0xF0,0x7E,0x7F,0x09,0x01,(byte)0xF7}, 6));
			for(XGModuleType mt : XGModuleType.TYPES) mt.resetValues();
		}
		catch(InvalidMidiDataException e)
		{	e.printStackTrace();
		}
	}

	private void resetAll()
	{	try
		{	new XGMessageParameterChange(STORE, XGMidi.getMidi(), new byte[]{0,0,0,0,0,0,0x7F,0,0}, true).transmit();
			for(XGModuleType mt : XGModuleType.TYPES) mt.resetValues();
		}
		catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
		{	LOG.severe(e1.getMessage());
		}
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
	{	return this.name.toString();
	}

	public JComponent getConfigComponent()
	{	GridBagConstraints gbc = new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, 2, 2, 0.5, 0.5, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);
		JPanel root = new JPanel();
		root.setLayout(new GridBagLayout());

		//JComponent c = this.midi.getConfigComponent();
		//root.add(c, gbc);

		//c = new XGSpinner("sysexID", this.sysex, 0, 15, 1);
		//gbc.gridx = 0;
		//gbc.gridheight = 1;
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		//gbc.weightx = 0;
		//gbc.weighty = 0;
		//root.add(c, gbc);

		//c = new XGDeviceDetector("device name", this.name, this);
		//root.add(c, gbc);
		//
		//c = new XGFileSelector(this.defaultFileName, "default dump file", "select", XGSysexFileConstants.SYX_FILEFILTER).small();
		//root.add(c, gbc);

		return root;
	}

	@Override public XGAddressableSet<XGAddress> getBulks()
	{	XGAddressableSet<XGAddress> set = new XGAddressableSet<>();
		for(XGModule mi : XGModule.INSTANCES) set.addAll(mi.getBulks());
		return set;
	}
}