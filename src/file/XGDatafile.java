package file;
import java.io.File;
import java.io.IOException;import java.util.Collection;import java.util.LinkedHashSet;import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import adress.XGAddressableSet;
import application.*;
import gui.*;
import bulk.XGBulkDumper;
import msg.*;
import xml.*;

public class XGDatafile extends File implements XGMessenger, XGLoggable
{	//TODO: beschissenes Konzept überdenken: bislang requestet der XGDumper seine konfigurierten Bulks aus dem Datafile, was nicht zielführend ist, wenn auch/nur ParameterChangeMessages drin sind; Es müsste also das XGDatafile an einen (als Destination) übergebenen XGMessenger gepushed werden 
	public static XMLNode CONFIG; 

	public static void init()
	{	CONFIG = JXG.config.getChildNodeOrNew(XMLNodeConstants.TAG_FILES);
		File f;
		for(XMLNode x : CONFIG.getChildNodes(XMLNodeConstants.TAG_ITEM))
		{	String s = x.getTextContent().toString();
			f =  new File(s);
			if(f.exists()) continue;
			XGLoggable.LOG.info(s + " doesn't exist (removing from history)");
			x.removeNode();
		}
		new XGSysexFileFilter();
		new XGMidiFileFilter();
	}

	public static void load(XGBulkDumper dumper)
	{	String last = CONFIG.getLastChildOrNew(XMLNodeConstants.TAG_ITEM).getTextContent().toString();
		XGFileSelector fs = new XGFileSelector(last, "load data file...", "load", XGDatafileFilter.getFilters());

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
						JXG.CURRENT_CONTENT.setValue(f.getAbsolutePath());
						addToRecentFiles(f.getAbsolutePath());
						break;
					}
					catch(XGDatafileFilterException e)
					{	XGLoggable.LOG.info(e.getMessage());
					}
					catch(IOException e)
					{	XGLoggable.LOG.severe(e.getMessage());
						return;
					}
				}
				else
				{	String s = file + " does not exist!";
					XGLoggable.LOG.info(s);
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
				JXG.CURRENT_CONTENT.setValue(f.getAbsolutePath());
				addToRecentFiles(f.getAbsolutePath());
			}
			catch(XGDatafileFilterException e)
			{	XGLoggable.LOG.info(e.getMessage());
			}
			catch(IOException e)
			{	XGLoggable.LOG.severe(e.getMessage());
			}
		}
	}

	public static void addToRecentFiles(String s)
	{	CONFIG.removeChildNodesWithTextContent(XMLNodeConstants.TAG_ITEM, s);
		CONFIG.addChildNode(new XMLNode(XMLNodeConstants.TAG_ITEM, null, s));
	}

	public static Collection<String> getRecentFilenames()
	{	Set<String> set = new LinkedHashSet<>();
		for(XMLNode n : CONFIG.getChildNodes(XMLNodeConstants.TAG_ITEM)) set.add(n.getTextContent().toString());
		return set;
	}

	public static void save(XGBulkDumper dumper)
	{	String last = CONFIG.getLastChildOrNew(XMLNodeConstants.TAG_ITEM).getTextContent().toString();
		XGFileSelector fs = new XGFileSelector(last, "save data file...", "save", XGDatafileFilter.getFilters());
		int fs_result ;
		File file;
		XGDatafileFilter filter ;
		while(true)
		{	fs_result = fs.select(XGMainWindow.MAINWINDOW);
			if(fs_result == XGFileSelector.CANCEL_OPTION)
			{	XGLoggable.LOG.info("fileselection aborted");
				return;
			}
			if(fs_result == XGFileSelector.APPROVE_OPTION)
			{	file = fs.getSelectedFile();
				try
				{	filter = XGDatafileFilter.getFilter(file);
					if(file.exists())
					{	int res = JOptionPane.showConfirmDialog(XGMainWindow.MAINWINDOW, " Overwrite " + file + " ?");
						if(res == JOptionPane.YES_OPTION)
						{	XGLoggable.LOG.info("yes is choosen: "+ file);
							break;
						}
						if(res == JOptionPane.NO_OPTION) continue;
						if(res == JOptionPane.CANCEL_OPTION) return;
					}
					else if(file.createNewFile())
					{	XGLoggable.LOG.info("new file created: " + file);
						break;
					}
				}
				catch(XGDatafileFilterException e)
				{	XGLoggable.LOG.info(e.getMessage());
					continue;
				}
				catch(IOException exception)
				{	XGLoggable.LOG.severe("I/O-Exception: " + exception.getMessage());
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
			JXG.CURRENT_CONTENT.setValue(df.getAbsolutePath());
			addToRecentFiles(df.getAbsolutePath());
		}
		catch(IOException e)
		{	XGLoggable.LOG.severe(e.getMessage());
		}
	}

/******************************************************************************************************************************************/

	final XGAddressableSet<XGResponse> buffer = new XGAddressableSet<>();
	final XGDatafileFilter filter;

	private XGDatafile(File f, XGDatafileFilter filter)throws IOException
	{	super(f.getAbsolutePath());
		this.filter = filter;
		this.filter.read(this);//damit auch beim Schreiben vorhandene (nicht unterstütze) Bulks erhalten bleiben
	}

	@Override public String toString(){ return this.getMessengerName();}

	@Override public String getMessengerName(){	return "File (" + this.getAbsolutePath() +")";}

	@Override public void submit(XGResponse res)
	{	this.buffer.add(res);
	}

	@Override public void request(XGRequest req)throws XGMessengerException
	{	XGResponse response = this.buffer.get(req.getAddress());
		if(req.setResponsedBy(response))
		{	req.getSource().submit(response);
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

