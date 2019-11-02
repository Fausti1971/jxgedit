package file;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import adress.InvalidXGAdressException;
import adress.XGAdressableSet;
import application.ConfigurationConstants;
//const mit file für default, const mit path mit filechooser
import device.XGDevice;
import gui.XGMainFrame;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public class XGSysexFile implements ConfigurationConstants, XGMessenger
{	private static final Logger log = Logger.getAnonymousLogger();
	private static XGSysexFile defaultDump;

/******************************************************************************************************************************************/

	private File file;
	private final XGDevice device;
	private XGAdressableSet<XGMessage> buffer = new XGAdressableSet<XGMessage>();

	private XGSysexFile(XGDevice dev, File f)
	{	this.device = dev;
		this.file = f;
		this.load();
	}

	public XGSysexFile(XGDevice dev)
	{	this.device = dev;
		this.selectFile();
	}

	public void load()
	{	if(file != null)
		{	log.info("parsing started: " + file.getAbsolutePath());
			for(SysexMessage s : parse())
			{	try
				{	XGMessage m = XGMessage.newMessage(this, s);
					XGMessenger dest = m.getDestination();
					if(dest == null) buffer.add(m);
					else dest.take(m);
				}
				catch (InvalidMidiDataException | InvalidXGAdressException e)
				{	log.severe(e.getMessage());
				}
			}
			log.info(buffer.size() + " Messages parsed from " + file.getAbsolutePath());
		}
	}

	private void selectFile()
	{	JFileChooser fc = new JFileChooser(this.device.getConfig().getChildNode(TAG_LASTDUMPFILE).getTextContent());
		fc.setDialogTitle("Open A Sysex-File...");
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileFilter()
		{	public String getDescription()
			{	return "Raw SystemExlusive File";
			}

			public boolean accept(File f)
			{	return f.exists() && f.canRead() && f.isFile() && f.getName().endsWith(".syx");
			}
		});
		int res = fc.showDialog(XGMainFrame.getMainFrame(), "Open");
		if(res == JFileChooser.APPROVE_OPTION) this.file = fc.getSelectedFile();
		else this.file = null;
		return;
	}

	private ArrayList<SysexMessage> parse()
	{	ArrayList<SysexMessage> array = new ArrayList<>();
		try(FileInputStream fis = new FileInputStream(file))
		{	byte[] tmp = new byte[fis.available()];
			boolean start = false, end = false;
			int first = 0, i = 0;
			while(fis.available() != 0)
			{	tmp[i] = (byte) fis.read();
				if(tmp[i] == (byte)SysexMessage.SYSTEM_EXCLUSIVE)
				{	start = true;
					first = i;
				}
				if(tmp[i] == (byte)SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE) end = true;
				if(start && end)
				{	try
					{	array.add(new SysexMessage(Arrays.copyOfRange(tmp, first, i + 1), (i + 1) - first));
					}
					catch (InvalidMidiDataException e)
					{	log.info(e.getMessage());
					}
					start = false;
					end = false;
				}
				i++;
			}
		}
		catch (IOException e)
		{	log.warning(e.getMessage());
			return null;
		}
		return array;
	}

	public XGDevice getDevice()
	{	return this.device;
	}

	public XGMessengerType getMessengerType()
	{	return XGMessengerType.File;
	}

	public String getMessengerName()
	{	return this.file.getAbsolutePath();
	}

	public void take(XGMessage m)//zum SysexFile ("datei.syx")
	{
	}

	public XGResponse pull(XGRequest msg)//von SysexFile ("datei.syx")
	{	return null;
	}

	public int getSysexID()
	{	return this.device.getSysexID();
	}
}

