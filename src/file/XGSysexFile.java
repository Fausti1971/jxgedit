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
import javax.sound.midi.SysexMessage;
import javax.swing.JFileChooser;
import adress.InvalidXGAddressException;
import application.ConfigurationConstants;
import application.JXG;
//const mit file für default, const mit path mit filechooser
import device.XGDevice;
import gui.XGWindow;
import msg.XGMessage;
import msg.XGMessageBuffer;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public class XGSysexFile extends File implements XGSysexFileConstants, ConfigurationConstants, XGMessenger
{	private static final long serialVersionUID=870648549558099401L;
	private static final Logger log = Logger.getAnonymousLogger();

/******************************************************************************************************************************************/

	private final Thread thread;
	private final XGDevice device;
	private XGMessageBuffer buffer = new XGMessageBuffer(this);

	public XGSysexFile(XGDevice dev, String path)
	{	super(path);
		this.device = dev;
		this.thread = new Thread(this);
	}

	public void load(XGMessenger dest)
	{	log.info("start parsing: " + this.getAbsolutePath());
		if(dest == null) dest = this.buffer;
		

		try(FileInputStream fis = new FileInputStream(this))
		{	byte[] tmp = new byte[fis.available()];
			boolean start = false, end = false;
			int first = 0, i = 0, messageCont = 0;
			while(fis.available() != 0)
			{	tmp[i] = (byte) fis.read();
				if(tmp[i] == (byte)SysexMessage.SYSTEM_EXCLUSIVE)
				{	start = true;
					first = i;
				}
				if(tmp[i] == (byte)SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE) end = true;
				if(start && end)
				{	try
					{	XGMessage m = XGMessage.newMessage(dest, Arrays.copyOfRange(tmp, first, i + 1), false);
						m.setDestination(dest);
						if(m instanceof XGResponse) dest.submit((XGResponse)m);
						messageCont++;
					}
					catch (InvalidMidiDataException | InvalidXGAddressException e)
					{	log.info(e.getMessage());
					}
					start = false;
					end = false;
				}
				i++;
			}
			log.info("parsing finished: " + messageCont + " messages parsed to " + dest.getMessengerName());
		}
		catch (IOException e)
		{	log.warning(e.getMessage());
		}


//		log.info( + " messages transmitted from " + this.getMessengerName() + " to " + dest.getMessengerName());
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
			return array;
		}
		return array;
	}

	public int getSysexID()
	{	return this.getDevice().getSysexID();
	}

	@Override public XGDevice getDevice()
	{	return this.device;
	}

	@Override public String getMessengerName()
	{	return this.getAbsolutePath();
	}

	@Override public void run()
	{
	}

	@Override public void submit(XGResponse msg) throws InvalidXGAddressException
	{	//TODO:
	}

	@Override public XGResponse request(XGRequest req) throws InvalidXGAddressException
	{
		// TODO Auto-generated method stub
		return null;
	}
}

