package file;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import application.ConfigurationConstants;
import application.XGLoggable;
import device.XGDevice;
import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;

public class XGSysexFile extends File implements XGSysexFileConstants, ConfigurationConstants, XGMessenger, XGLoggable
{	private static final long serialVersionUID=870648549558099401L;

//	public static Path selectFile(String s, String title, String button, boolean ask) throws FileNotFoundException
//	{	if(s == null) s = JXG.HOMEPATH.toString();
//		JFileChooser fc = new JFileChooser(s);
//		fc.setDialogTitle(title);
//		fc.setAcceptAllFileFilterUsed(false);
//		fc.setFileFilter(SYX_FILEFILTER);
//		int res = fc.showDialog(XGWindow.getRootWindow(), button);
//		if(res == JFileChooser.APPROVE_OPTION) return fc.getSelectedFile().toPath();
//		throw new FileNotFoundException("fileselection aborted");
//	}

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

	public XGSysexFile(XGDevice dev, final String path) throws IOException, FileNotFoundException
	{	super(path);
		if(!this.canRead()) this.createNewFile();
		this.device = dev;
//		this.parse();
	}

/**
 * lädt und parst ein SysexFile in den internen Puffer
 * @param dest
 * @throws IOException 
 */
	public void parse() throws IOException, FileNotFoundException
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

	public void save()
	{	int count = 0;
		if(this.changed && this.canWrite())
		{	try(FileOutputStream fos = new FileOutputStream(this))
			{	for(XGResponse r : this.buffer)
				{	fos.write(r.getByteArray());
					LOG.info("written: " + ++count + "/" + this.buffer.size());
				}
				fos.close();
				this.changed = false;
				LOG.info(count + " messages saved to " + this);
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

	@Override public void submit(XGResponse msg)
	{	this.buffer.add(msg);
		this.changed = true;
	}

	@Override public void request(XGRequest req) throws InvalidXGAddressException
	{	XGResponse response = this.buffer.get(req.getAddress());
		req.setResponsed(response);
	}

	@Override public void close()
	{
	}
}

