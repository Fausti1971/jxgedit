package file;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import application.ConfigurationConstants;
import application.JXG;
import application.XGLoggable;
//const mit file für default, const mit path mit filechooser
import device.XGDevice;
import gui.XGWindow;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public class XGSysexFile extends File implements XGSysexFileConstants, ConfigurationConstants, XGMessenger, XGLoggable
{	private static final long serialVersionUID=870648549558099401L;

/******************************************************************************************************************************************/

	private final XGDevice device;
//	private XGMessageBuffer buffer = new XGMessageBuffer(this);
	private XGAddressableSet<XGMessage> buffer = new XGAddressableSet<>();
	private boolean changed = false;
	private Timer timer;

	public XGSysexFile(XGDevice dev, String path)
	{	super(path);
		this.device = dev;
		this.parse();
		this.timer = new Timer(1000, new ActionListener()
		{	@Override public void actionPerformed(ActionEvent e)
			{	save();
			}
		});
	}

/**
 * lädt und parst ein SysexFile
 * @param dest
 */
	private void parse()
	{	LOG.info("start parsing: " + this.getAbsolutePath());

		try(FileInputStream fis = new FileInputStream(this))
		{	byte[] tmp = new byte[fis.available()];
			boolean start = false, end = false;
			int first = 0, i = 0;
			while(fis.available() != 0)
			{	tmp[i] = (byte) fis.read();
				if(tmp[i] == (byte)XGMessage.SOX)
				{	start = true;
					first = i;
				}
				if(tmp[i] == (byte)XGMessage.EOX) end = true;
				if(start && end)
				{	try
					{	XGMessage m = XGMessage.newMessage(this, null, Arrays.copyOfRange(tmp, first, i + 1), false);
						this.buffer.add(m);
					}
					catch (InvalidMidiDataException | InvalidXGAddressException e)
					{	LOG.info(e.getMessage());
					}
					start = false;
					end = false;
				}
				i++;
			}
			LOG.info("parsing finished: " + this.buffer.size() + " messages parsed from " + this);
			fis.close();
		}
		catch (IOException e)
		{	LOG.warning(e.getMessage());
		}
	}

	private void save()
	{	if(this.changed)
		{	LOG.warning("bla bla bla saved");
		}
		this.timer.stop();
		this.changed = false;
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
	{	this.buffer.add(msg);
		this.changed = true;
		this.timer.restart();
	}

	@Override public void request(XGRequest req) throws InvalidXGAddressException
	{	XGMessage response = this.buffer.get(req.getAddress());
		if(req.setResponsed((XGResponse)response)) response.getDestination().submit((XGResponse)response);
	}

	@Override public JComponent getConfigComponent()
	{	return new JFileChooser(this.getAbsolutePath());
	}

	@Override public void close()
	{	this.timer.stop();
	}
}

