package file;
//const mit file für default, const mit path mit filechooser
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import application.MU80;
import application.Setting;
import msg.XGMessage;

public class SysexFile
{	private static final Logger log = Logger.getAnonymousLogger();
	private static SysexFile defaultDump;

	public static SysexFile getDefaultDump()
	{	if(defaultDump == null) defaultDump = new SysexFile(new File("rsc/default.syx"));
		return defaultDump;
	}

/******************************************************************************************************************************************/

//	private Path path;
	private File file;

	private SysexFile(File f)
	{	this.file = f;
		load(false);
	}

	public SysexFile()
	{	//this.path = Paths.get(MU80.getSetting().get(Setting.LASTDUMPPATH));
		setFile();
	}
	
	public void load(boolean protocol)
	{	if(file != null)
		{	int count = 0;
			for(SysexMessage s : parse())
			{	try
				{	XGMessage.factory(s).processXGMessage();
					count++;
				}
				catch (InvalidMidiDataException e)
				{	log.severe(e.getMessage());
				}
			}
			log.info(count + " Messages loaded from " + file.getAbsolutePath());
			if(protocol)
			{	MU80.getSetting().put(Setting.LASTDUMPFILE, file.getAbsolutePath());
				MU80.getSetting().put(Setting.LASTDUMPPATH, file.getParent());
				MU80.getMainFrame().setTitle(file.getAbsolutePath());
				
			}
		}
	}

	private void setFile()
	{	JFileChooser fc = new JFileChooser(MU80.getSetting().get(Setting.LASTDUMPFILE));
		fc.setDialogTitle("Open A Sysex-File...");
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileFilter()
		{	public String getDescription()
			{	return "Raw SystemExlusive File";
			}

			public boolean accept(File f)
			{	return f.exists() && f.canRead() && f.isFile() && f.getName().endsWith(".syx");
			}
		});
		int res = fc.showDialog(MU80.getMainFrame(), "Open");
		if(res == JFileChooser.APPROVE_OPTION) this.file = fc.getSelectedFile();
		else this.file = null;
		return;
	}

	private ArrayList<SysexMessage> parse()
	{	ArrayList<SysexMessage> array = new ArrayList<>();
		try(FileInputStream fis = new FileInputStream(file))
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
			return null;
		}
		return array;
	}
}

