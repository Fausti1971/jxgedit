package file;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFileChooser;
import adress.InvalidXGAddressException;
import application.ConfigurationConstants;
import application.JXG;
import application.XGLoggable;
//const mit file für default, const mit path mit filechooser
import device.XGDevice;
import gui.XGWindow;
import msg.XGMessage;
import msg.XGMessageBuffer;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public class XGSysexFile extends File implements XGSysexFileConstants, ConfigurationConstants, XGMessenger, XGLoggable
{	private static final long serialVersionUID=870648549558099401L;

/******************************************************************************************************************************************/

	private final XGDevice device;
	private XGMessageBuffer buffer = new XGMessageBuffer(this);

	public XGSysexFile(XGDevice dev, String path)
	{	super(path);
		this.device = dev;
	}

/**
 * lädt und parst ein SysexFile zum XGMessenger dest
 * @param dest
 */
	public void load(XGMessenger dest)
	{	LOG.info("start parsing: " + this.getAbsolutePath());
		if(dest == null) dest = this.buffer;

		try(FileInputStream fis = new FileInputStream(this))
		{	byte[] tmp = new byte[fis.available()];
			boolean start = false, end = false;
			int first = 0, i = 0, messageCount = 0;
			while(fis.available() != 0)
			{	tmp[i] = (byte) fis.read();
				if(tmp[i] == (byte)XGMessage.SOX)
				{	start = true;
					first = i;
				}
				if(tmp[i] == (byte)XGMessage.EOX) end = true;
				if(start && end)
				{	try
					{	XGMessage m = XGMessage.newMessage(this, dest, Arrays.copyOfRange(tmp, first, i + 1), false);
						if(m instanceof XGResponse) ((XGResponse)m).transmit();
						messageCount++;
					}
					catch (InvalidMidiDataException | InvalidXGAddressException e)
					{	LOG.info(e.getMessage());
					}
					start = false;
					end = false;
				}
				i++;
			}
			LOG.info("parsing finished: " + messageCount + " messages parsed from " + this + " to " + dest.getMessengerName());
		}
		catch (IOException e)
		{	LOG.warning(e.getMessage());
		}
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

	@Override public XGDevice getDevice()
	{	return this.device;
	}

	@Override public String getMessengerName()
	{	return this.getAbsolutePath();
	}

	@Override public void submit(XGResponse msg) throws InvalidXGAddressException
	{	//TODO:
	}

	@Override public XGResponse request(XGRequest req) throws InvalidXGAddressException
	{	return null;
	}
}

