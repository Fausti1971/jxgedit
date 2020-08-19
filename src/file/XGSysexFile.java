package file;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.sound.midi.InvalidMidiDataException;
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

	public static Path selectFile(String s, String title, String button, boolean ask) throws FileNotFoundException
	{	if(s == null) s = JXG.HOMEPATH.toString();
		JFileChooser fc = new JFileChooser(s);
		fc.setDialogTitle(title);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(SYX_FILEFILTER);
		int res = fc.showDialog(XGWindow.getRootWindow(), button);
		if(res == JFileChooser.APPROVE_OPTION) return fc.getSelectedFile().toPath();
		throw new FileNotFoundException("fileselection aborted");
	}

//	public static Path selectPath(String s, String title, String button, boolean ask)
//	{	if(s == null) s = JXG.HOMEPATH.toString();
//		JFileChooser fc = new JFileChooser(s);
//		fc.setDialogTitle("select folder...");
//		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		fc.setAcceptAllFileFilterUsed(false);
//		int res = fc.showDialog(XGWindow.getRootWindow(), "select");
//		if(res == JFileChooser.APPROVE_OPTION) return fc.getCurrentDirectory().toPath();
//		else return Paths.get(s);
//	}



/******************************************************************************************************************************************/

	private final XGDevice device;
//	private XGMessageBuffer buffer = new XGMessageBuffer(this);
	private XGAddressableSet<XGResponse> buffer = new XGAddressableSet<>();
	private boolean changed = false;
	private Timer timer;

	public XGSysexFile(XGDevice dev, final String path) throws IOException, FileNotFoundException
	{	super(path);
		if(!this.canRead()) this.createNewFile();
		this.device = dev;
		this.parse();
		this.timer = new Timer(1000, new ActionListener()
		{	@Override public void actionPerformed(ActionEvent e)
			{	save();
			}
		});
		this.timer.setRepeats(false);
	}

/**
 * lädt und parst ein SysexFile
 * @param dest
 * @throws IOException 
 */
	private void parse() throws IOException, FileNotFoundException
	{	LOG.info("start parsing: " + this.getAbsolutePath());

		FileInputStream fis = new FileInputStream(this);
		byte[] tmp = new byte[fis.available()];
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
				{	XGResponse m = (XGResponse)XGMessage.newMessage(this, null, Arrays.copyOfRange(tmp, first, i + 1), false);
					this.buffer.add(m);
				}
				catch (InvalidMidiDataException | InvalidXGAddressException e)
				{	LOG.severe(e.getMessage());
				}
				start = false;
				end = false;
			}
			i++;
		}
		LOG.info("parsing finished: " + this.buffer.size() + " messages parsed from " + this);
		fis.close();
	}

	private void save()
	{	if(this.changed)
		{	try(FileOutputStream fos = new FileOutputStream(this))
			{	for(XGResponse r : this.buffer)
				{	fos.write(r.getByteArray());
				}
				fos.close();
//				this.timer.stop();
				this.changed = false;
				LOG.info(this.buffer.size() + "messages saved");
			}
			catch(IOException e)
			{	LOG.severe(e.getMessage());
			}
		}
		else LOG.info("file is unchanged: " + this);
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
	{	XGResponse response = this.buffer.get(req.getAddress());
		if(req.setResponsed(response)) response.getDestination().submit(response);
	}

	@Override public void close()
	{	this.timer.stop();
	}
}

