package file;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.sound.midi.InvalidMidiDataException;import javax.swing.*;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import application.*;
import gui.*;import module.XGBulkDumper;import msg.*;
import xml.*;import static xml.XMLNodeConstants.TAG_ITEM;

public class XGSysexFile extends File implements XGSysexFileConstants,  XGMessenger, XGLoggable
{	private static final long serialVersionUID=870648549558099401L;

	public static XMLNode config;

	public static void init()
	{	config = JXG.config.getChildNodeOrNew(XMLNodeConstants.TAG_FILES);

		for(XMLNode x : config.getChildNodes(TAG_ITEM))
		{	try
			{	File f =  new File(String.valueOf(x.getTextContent()));
			}
			catch(NullPointerException e)
			{	LOG.info(x + " doesn't exisit; removing from files...");
				config.removeChildNode(x);
			}
		}
	}

	private static String appendSuffix(String s)
	{	if(SYX_SUFFIX.equalsIgnoreCase(s.substring(s.length() - 4))) return s;
		else return s.concat(SYX_SUFFIX);
	}

	public static void load(XGBulkDumper dumper)
	{	XMLNode last = config.getLastChildOrNew(TAG_ITEM);
		XGFileSelector fs = new XGFileSelector(last.getTextContent(), "load sysex file...", "load", XGSysexFileConstants.SYX_FILEFILTER);
		switch(fs.select(XGMainWindow.window))
		{	case JFileChooser.APPROVE_OPTION:
			{	XGSysexFile f;
				try
				{	f = new XGSysexFile(last.getTextContent().toString());
					dumper.requestAll(f);
					f.close();
				}
				catch(IOException e)
				{	LOG.severe(e.getMessage());
				}
				break;
			}
			case JFileChooser.CANCEL_OPTION:
			{	last.removeNode();
				LOG.info("fileselection aborted");
				break;
			}
		}
	}

	public static void save(XGBulkDumper dumper)
	{	XMLNode last = XGSysexFile.config.getLastChildOrNew(TAG_ITEM);
		XGFileSelector fs = new XGFileSelector(last.getTextContent(), "save sysex file...", "save", XGSysexFileConstants.SYX_FILEFILTER);
		switch(fs.select(XGMainWindow.window))
		{	case JFileChooser.APPROVE_OPTION:
			{	try
				{	XGSysexFile f = new XGSysexFile(last.getTextContent().toString());
					if(f.exists())
					{	int res = JOptionPane.showConfirmDialog(XGMainWindow.window, " Overwrite " + f + "?");
						if(res == JOptionPane.CANCEL_OPTION || res == JOptionPane.NO_OPTION) return;
					}
					else f.createNewFile();
					dumper.transmitAll(f);
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
				LOG.info("fileselection aborted");
				break;
			}
		}
	}


/******************************************************************************************************************************************/

	private final XGAddressableSet<XGResponse> buffer = new XGAddressableSet<>();
	private boolean changed = false;

	private XGSysexFile(final String path)throws IOException
	{	super(XGSysexFile.appendSuffix(path));
		this.parse();
	}

	public void parse() throws IOException
	{	LOG.info("start parsing: " + this.getAbsolutePath());

		FileInputStream fis = new FileInputStream(this);
		byte[] tmp = new byte[fis.available()];
		boolean start = false;
		int first = 0, i = 0;
		while(fis.available() != 0)
		{	tmp[i] = (byte) fis.read();
			if(tmp[i] == (byte)XGMessage.SOX)
			{	start = true;
				first = i;
			}

			if(start && tmp[i] == (byte)XGMessage.EOX)
			{	try
				{	XGMessage m = XGMessage.newMessage(this, Arrays.copyOfRange(tmp, first, i + 1), false);
					if(m instanceof XGResponse) this.buffer.add((XGResponse)m);
				}
				catch (InvalidMidiDataException | InvalidXGAddressException e)
				{	LOG.severe(e.getMessage());
				}
				start = false;
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

	@Override public void submit(XGResponse msg)throws XGMessengerException, InvalidXGAddressException
	{	if(msg instanceof  XGMessageBulkDump)
		{	this.buffer.add(msg);
			this.changed = true;
		}
	}

	@Override public void submit(XGRequest req)throws XGMessengerException, InvalidXGAddressException
	{	if(req instanceof XGMessageBulkRequest)
		{	XGResponse response = this.buffer.get(req.getAddress());
			if(req.setResponsedBy(response)) req.getSource().submit(response);
		}
	}

	@Override public void close()
	{	int choose = JOptionPane.NO_OPTION;
//		if(this.changed) choose = JOptionPane.showConfirmDialog(XGMainWindow.window, this.getMessengerName() + " has unsaved edits! Save before close?", "Close...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
		if(choose == JOptionPane.YES_OPTION) this.save();
		LOG.info(this.getMessengerName() + " closed");
	}
}

