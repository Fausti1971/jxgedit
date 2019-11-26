package file;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.SysexMessage;
import javax.swing.JFileChooser;
import adress.InvalidXGAdressException;
import adress.XGAdressableSet;
import application.ConfigurationConstants;
import application.JXG;
//const mit file für default, const mit path mit filechooser
import device.XGDevice;
import gui.XGWindow;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public class XGSysexFile extends File implements XGSysexFileConstants, ConfigurationConstants, XGMessenger
{	private static final long serialVersionUID=870648549558099401L;
	private static final Logger log = Logger.getAnonymousLogger();

/******************************************************************************************************************************************/

	private final XGMessenger source;
	private XGAdressableSet<XGMessage> buffer = new XGAdressableSet<XGMessage>();

	public XGSysexFile(XGMessenger src, String path)
	{	super(path);
		this.source = src;
	}

	public void load(XGMessenger dest)
	{	log.info("parsing started: " + this.getAbsolutePath());
		for(SysexMessage s : parse())
		{	try
			{	XGMessage m = XGMessage.newMessage(this, s);
				m.setDestination(dest);
				if(dest == null) buffer.add(m);
				else dest.transmit(m);
			}
			catch (InvalidMidiDataException | InvalidXGAdressException | MidiUnavailableException e)
			{	log.severe(e.getMessage());
			}
		}
		log.info(buffer.size() + " Messages parsed from " + this.getAbsolutePath());
	}

	public Path selectFile(String s)
	{	if(s == null) s = JXG.HOMEPATH.toString();
		JFileChooser fc = new JFileChooser(s);
		fc.setDialogTitle("open sysex-file...");
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(SYX_FILEFILTER);
		int res = fc.showDialog(XGWindow.getRootWindow(), "open");
		if(res == JFileChooser.APPROVE_OPTION) return fc.getSelectedFile().toPath();
		return null;
	}

	public Path selectPath(String s)
	{	if(s == null) s = JXG.HOMEPATH.toString();
		JFileChooser fc = new JFileChooser(s);
		fc.setDialogTitle("select folder...");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
//		fc.setFileFilter(SYX_FILEFILTER);
		int res = fc.showDialog(XGWindow.getRootWindow(), "select");
		if(res == JFileChooser.APPROVE_OPTION) return fc.getCurrentDirectory().toPath();
		else return Paths.get(s);
	}

	private List<SysexMessage> parse()
	{	ArrayList<SysexMessage> array = new ArrayList<>();
		try(FileInputStream fis = new FileInputStream(this))
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

	public void transmit(XGMessage m)//zum SysexFile ("datei.syx")
	{
	}

	public XGResponse request(XGRequest msg)//von SysexFile ("datei.syx")
	{	return null;
	}

	public int getSysexID()
	{	return this.getDevice().getSysexID();
	}

	public XGDevice getDevice()
	{	return this.source.getDevice();
	}

	public String getMessengerName()
	{	return this.getAbsolutePath();
	}
}

