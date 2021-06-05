package file;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.sound.midi.InvalidMidiDataException;import javax.swing.*;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import application.*;
import gui.*;import msg.XGMessage;
import msg.XGMessenger;
import msg.XGRequest;
import msg.XGResponse;import xml.*;import static xml.XMLNodeConstants.TAG_ITEM;

public class XGSysexFile extends File implements XGSysexFileConstants,  XGMessenger, XGLoggable
{	private static final long serialVersionUID=870648549558099401L;

	public static XMLNode config;
	private static XGSysexFile currentFile = null;

	public static void init()
	{	config = JXG.config.getChildNodeOrNew(XMLNodeConstants.TAG_FILES);

		for(XMLNode x : config.getChildNodes(TAG_ITEM))
		{	try
			{	File f =  new File(String.valueOf(x.getTextContent()));
			}
			catch( NullPointerException e)
			{	LOG.info(x + " doesn't exisit; removing from files...");
				config.removeChildNode(x);
			}
		}
	}
/******************************************************************************************************************************************/

//	private final XGDevice device;
//	private XGMessageBuffer buffer = new XGMessageBuffer(this);
	private final XGAddressableSet<XGMessage> buffer = new XGAddressableSet<>();
	private boolean changed = false;

	public XGSysexFile(final String path) throws IOException
	{	super(path);
//		if(!this.canRead()) this.createNewFile();
//		this.device = dev;
//		this.parse();
	}

/**
 * lädt und parst ein SysexFile in den internen Puffer
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
			{	for(XGMessage r : this.buffer)
				{	fos.write(r.getByteArray());
					count++;
//					LOG.info("written: " + ++count + "/" + this.buffer.size());
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

	@Override public String getMessengerName()
	{	return "File (" + this.getAbsolutePath() +")";
	}

	@Override public void submit(XGMessage msg)
	{	this.buffer.add(msg);
		this.changed = true;
	}

	@Override public void request(XGRequest req) throws InvalidXGAddressException
	{	XGMessage response = this.buffer.get(req.getAddress());
		req.setResponsed((XGResponse)response);
	}

	@Override public void close()
	{	int choose = JOptionPane.NO_OPTION;
		if(this.changed) choose = JOptionPane.showConfirmDialog(XGMainWindow.window, this.getMessengerName() + " has unsaved edits! Save before close?", "Close...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
		if(choose == JOptionPane.YES_OPTION) this.save();
		LOG.info(this.getMessengerName() + " closed");
	}
}

