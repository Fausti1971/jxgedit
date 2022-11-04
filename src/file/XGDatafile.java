package file;
import java.awt.*;import java.awt.event.ActionEvent;import java.io.File;
import java.io.IOException;import java.util.Collection;import java.util.HashSet;import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;import javax.swing.event.PopupMenuEvent;import javax.swing.event.PopupMenuListener;
import adress.XGAddressableSet;
import application.*;
import device.XGDevice;import static file.XGDatafileFilter.*;
import gui.*;
import bulk.XGBulkDumper;
import msg.*;
import xml.*;
import static xml.XMLNodeConstants.TAG_ITEM;

public class XGDatafile extends File implements XGMessenger, XGLoggable
{
	public static XMLNode CONFIG; 

	public static void init()
	{	CONFIG = JXG.config.getChildNodeOrNew(XMLNodeConstants.TAG_FILES);
		File f;
		for(XMLNode x : CONFIG.getChildNodes(TAG_ITEM))
		{	String s = x.getTextContent().toString();
			f =  new File(s);
			if(f.exists()) continue;
			LOG.info(s + " doesn't exist (removing from history)");
			x.removeNode();
		}
	}

	public static void load(XGBulkDumper dumper)
	{	String last = CONFIG.getLastChildOrNew(TAG_ITEM).getTextContent().toString();
		XGFileSelector fs = new XGFileSelector(last, "load data file...", "load", SUPPORTED_FILEFILTER, SYX_FILEFILTER, MID_FILEFILTER);

		while(true)
		{	int res = fs.select(XGMainWindow.MAINWINDOW);
			if(res == JFileChooser.CANCEL_OPTION) return;
			if(res == JFileChooser.APPROVE_OPTION)
			{	File file = fs.getSelectedFile();
				if(file.exists())
				{	try
					{	XGDatafileFilter filter = XGDatafileFilter.getFilter(file);
						XGDatafile f = new XGDatafile(file, filter);
						dumper.requestAll(f);
						f.close();
						JXG.CURRENT_CONTENT.setValue(f.getName());
						CONFIG.removeChildNodesWithTextContent(TAG_ITEM, f.getAbsolutePath());
						CONFIG.addChildNode(new XMLNode(TAG_ITEM, null, f.getAbsolutePath()));
						break;
					}
					catch(XGDatafileFilterException e)
					{	LOG.info(e.getMessage());
					}
					catch(IOException e)
					{	LOG.severe(e.getMessage());
						return;
					}
				}
				else
				{	String s = file + " does not exist!";
					LOG.info(s);
					JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, s);
				}
			}
		}
	}

	public static void load(XGBulkDumper dmp, String path)
	{	File file = new File(path);
		if(file.exists())
		{	try
			{	XGDatafileFilter filter = XGDatafileFilter.getFilter(file);
				XGDatafile f = new XGDatafile(file, filter);
				dmp.requestAll(f);
				f.close();
				JXG.CURRENT_CONTENT.setValue(f.getName());
				CONFIG.removeChildNodesWithTextContent(TAG_ITEM, f.getAbsolutePath());
				CONFIG.addChildNode(new XMLNode(TAG_ITEM, null, f.getAbsolutePath()));
			}
			catch(XGDatafileFilterException e)
			{	LOG.info(e.getMessage());
			}
			catch(IOException e)
			{	LOG.severe(e.getMessage());
			}
		}
	}

	public static Collection<String> getRecentFilenames()
	{	Set<String> set = new HashSet<>();
		for(XMLNode n : CONFIG.getChildNodes(TAG_ITEM)) set.add(n.getTextContent().toString());
		return set;
	}

	//public static void recent(XGBulkDumper dmp, ActionEvent evnt)
	//{	if(evnt.getSource() instanceof JComponent)
	//	{	Point p = ((JComponent)evnt.getSource()).getLocationOnScreen();
	//		JPopupMenu m = new JPopupMenu();
	////		m.setSelectionModel(new DefaultSingleSelectionModel());
	//		m.setLocation(p);
	//		for(XMLNode n : XGDatafile.CONFIG.getChildNodes(TAG_ITEM))
	//		{	JMenuItem i = new JMenuItem(String.valueOf(n.getTextContent()));
	//			i.addActionListener((ActionEvent e)->{	XGDatafile.load(dmp, i.getText()); m.setVisible(false);});
	//			m.add(i);
	//		}
	//		m.setVisible(true);
	//	}
	//}


	public static void save(XGBulkDumper dumper)
	{	String last = CONFIG.getLastChildOrNew(TAG_ITEM).getTextContent().toString();
		XGFileSelector fs = new XGFileSelector(last, "save data file...", "save", SUPPORTED_FILEFILTER, MID_FILEFILTER, SYX_FILEFILTER);
		int fs_result ;
		File file;
		XGDatafileFilter filter ;
		while(true)
		{	fs_result = fs.select(XGMainWindow.MAINWINDOW);
			if(fs_result == XGFileSelector.CANCEL_OPTION)
			{	LOG.info("fileselection aborted");
				return;
			}
			if(fs_result == XGFileSelector.APPROVE_OPTION)
			{	file = fs.getSelectedFile();
				try
				{	filter = XGDatafileFilter.getFilter(file);
					if(file.exists())
					{	int res = JOptionPane.showConfirmDialog(XGMainWindow.MAINWINDOW, " Overwrite " + file + " ?");
						if(res == JOptionPane.YES_OPTION)
						{	LOG.info("yes is choosen: "+ file);
							break;
						}
						if(res == JOptionPane.NO_OPTION) continue;
						if(res == JOptionPane.CANCEL_OPTION) return;
					}
					else if(file.createNewFile())
					{	LOG.info("new file created: " + file);
						break;
					}
				}
				catch(XGDatafileFilterException e)
				{	LOG.info(e.getMessage());
					continue;
				}
				catch(IOException exception)
				{	LOG.severe("I/O-Exception: " + exception.getMessage());
					continue;
				}
			}
			return;
		}
		try
		{	XGDatafile df = new XGDatafile(file, filter);
			dumper.transmitAll(df);
			df.filter.write(df);
			df.close();
			JXG.CURRENT_CONTENT.setValue(df.getName());
			CONFIG.removeChildNodesWithTextContent(TAG_ITEM, df.getAbsolutePath());
			CONFIG.addChildNode(new XMLNode(TAG_ITEM, null, df.getAbsolutePath()));
		}
		catch(IOException | InvalidMidiDataException e)
		{	LOG.severe(e.getMessage());
		}
	}

/******************************************************************************************************************************************/

	final XGAddressableSet<XGMessageBulkDump> buffer = new XGAddressableSet<>();
	final XGDatafileFilter filter;

	private XGDatafile(File f, XGDatafileFilter filter)throws IOException
	{	super(f.getAbsolutePath());
		this.filter = filter;
		this.filter.read(this);//damit auch beim Schreiben vorhandene (nicht unterstütze) Bulks erhalten bleiben
	}

	@Override public String toString(){ return this.getMessengerName();}

	@Override public String getMessengerName(){	return "File (" + this.getAbsolutePath() +")";}

	@Override public void submit(XGMessageBulkDump msg)throws XGMessengerException
	{	this.buffer.add(msg);
	}

	@Override public void submit(XGMessageParameterChange msg)throws XGMessengerException
	{	throw new XGMessengerException(this, msg);
	}

	@Override public void submit(XGMessageBulkRequest req)throws XGMessengerException
	{	XGMessageBulkDump response = this.buffer.get(req.getAddress());
		if(req.setResponsedBy(response))
		{	req.getSource().submit(response);
		}
	}

	@Override public void submit(XGMessageParameterRequest msg)throws XGMessengerException
	{	throw new XGMessengerException(this, msg);
	}

	@Override public void close()
	{
//		int choose = JOptionPane.NO_OPTION;
//		if(this.changed) choose = JOptionPane.showConfirmDialog(XGMainWindow.window, this.getMessengerName() + " has unsaved edits! Save before close?", "Close...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
//		if(choose == JOptionPane.YES_OPTION) save();
//		LOG.info(this.getMessengerName() + " closed");
	}
}

