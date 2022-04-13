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

public class XGSysexFile extends File implements XGSysexFileConstants, XGMessenger, XGLoggable
{	private static final long serialVersionUID=870648549558099401L;

	public static XMLNode config;

	public static void init()
	{	config = JXG.config.getChildNodeOrNew(XMLNodeConstants.TAG_FILES);
		File f;
		for(XMLNode x : config.getChildNodes(TAG_ITEM))
		{	String s = x.getTextContent().toString();
			f =  new File(s);
			if(f.exists()) continue;
			LOG.info(s + " doesn't exist (removing from history)");
			x.removeNode();
		}
	}

	private static String appendSuffix(String s)
	{	if(SYX_SUFFIX.equalsIgnoreCase(s.substring(s.length() - 4))) return s;
		else return s.concat(SYX_SUFFIX);
	}

	public static void load(XGBulkDumper dumper)
	{	String last = config.getLastChildOrNew(TAG_ITEM).getTextContent().toString();
		XGFileSelector fs = new XGFileSelector(last, "load sysex file...", "load", XGSysexFileConstants.SYX_FILEFILTER);
		switch(fs.select(XGMainWindow.MAINWINDOW))
		{	case JFileChooser.APPROVE_OPTION:
			{	try
				{	XGSysexFile f = new XGSysexFile(fs.getPath());
					if(f.exists())
					{	dumper.requestAll(f);
						f.close();
						config.removeChildNodesWithTextContent(TAG_ITEM, f.getAbsolutePath());
						config.addChildNode(new XMLNode(TAG_ITEM, null, f.getAbsolutePath()));
					}
					else JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, f + " does not exist!");
				}
				catch(IOException e)
				{	LOG.severe(e.getMessage());
					JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, e.getMessage());
					return;
				}
				break;
			}
			case JFileChooser.CANCEL_OPTION:
			{	LOG.info("fileselection aborted");
				break;
			}
		}
	}

	public static void save(XGBulkDumper dumper)
	{	String last = config.getLastChildOrNew(TAG_ITEM).getTextContent().toString();
		XGFileSelector fs = new XGFileSelector(last, "save sysex file...", "save", XGSysexFileConstants.SYX_FILEFILTER);
		switch(fs.select(XGMainWindow.MAINWINDOW))
		{	case JFileChooser.APPROVE_OPTION:
			{	try
				{	XGSysexFile f = new XGSysexFile(fs.getPath());
					if(f.exists())
					{	int res = JOptionPane.showConfirmDialog(XGMainWindow.MAINWINDOW, " Overwrite " + f + "?");
						if(res == JOptionPane.CANCEL_OPTION || res == JOptionPane.NO_OPTION) return;
					}
					else if(!(f.createNewFile()))
					{	LOG.severe("filecreation failed: " + f.getPath());
						return;
					}
					dumper.transmitAll(f);
					f.save();
					f.close();
					config.removeChildNodesWithTextContent(TAG_ITEM, f.getAbsolutePath());
					config.addChildNode(new XMLNode(TAG_ITEM, null, f.getAbsolutePath()));
				}
				catch(IOException e)
				{	LOG.severe(e.getMessage());
					JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, e.getMessage());
					return;
				}
				break;
			}
			case JFileChooser.CANCEL_OPTION:
			{	LOG.info("fileselection aborted");
				break;
			}
		}
	}

/******************************************************************************************************************************************/

	private final XGAddressableSet<XGMessageBulkDump> buffer = new XGAddressableSet<>();

	private XGSysexFile(final String path)throws IOException
	{	super(XGSysexFile.appendSuffix(path));
		if(this.exists()) this.parse();
	}

	private void parse() throws IOException
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
					if(m instanceof XGMessageBulkDump) this.buffer.add((XGMessageBulkDump)m);
				}
				catch (InvalidMidiDataException | InvalidXGAddressException e)
				{	LOG.severe(e.getMessage());
					JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, e.getMessage());
				}
				start = false;
			}
			i++;
		}
		LOG.info(this.buffer.size() + " messages parsed from " + this);
		fis.close();
	}

	private void save()
	{	int count = 0;
		if(this.canWrite())
		{	try(FileOutputStream fos = new FileOutputStream(this))
			{	for(XGMessage r : this.buffer)
				{	fos.write(r.getByteArray());
					count++;
				}
				fos.close();
				LOG.info(count + " messages saved to " + this);
			}
			catch(IOException e)
			{	LOG.severe(e.getMessage());
				JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, e.getMessage());
			}
		}
		else
		{	LOG.info("cannot write: " + this);
			JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, "cannot write: " + this);
		}
	}

	@Override public String getMessengerName(){	return "File (" + this.getAbsolutePath() +")";}

	@Override public void submit(XGResponse msg)throws XGMessengerException, InvalidXGAddressException
	{	if(msg instanceof XGMessageBulkDump) this.buffer.add((XGMessageBulkDump)msg);
	}

	@Override public void submit(XGRequest req)throws XGMessengerException, InvalidXGAddressException
	{	if(req instanceof XGMessageBulkRequest)
		{	XGResponse response = this.buffer.get(req.getAddress());
			if(req.setResponsedBy(response)) req.getSource().submit(response);
		}
	}

	@Override public void close()
	{
//		int choose = JOptionPane.NO_OPTION;
//		if(this.changed) choose = JOptionPane.showConfirmDialog(XGMainWindow.window, this.getMessengerName() + " has unsaved edits! Save before close?", "Close...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
//		if(choose == JOptionPane.YES_OPTION) save();
//		LOG.info(this.getMessengerName() + " closed");
	}
}

