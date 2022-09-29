package file;

import gui.XGMainWindow;import msg.XGMessage;import msg.XGMessageBulkDump;import javax.sound.midi.InvalidMidiDataException;import javax.swing.*;import javax.swing.filechooser.FileFilter;import java.io.File;import java.io.FileInputStream;import java.io.FileOutputStream;import java.io.IOException;import java.util.Arrays;

public class XGSysexFileFilter extends XGDatafileFilter
{	private static final String SYX_SUFFIX = ".syx";
	private static final String SYX_DESCRIPTION = "Raw SystemExlusive File";

	@Override public String getSuffix(){	return SYX_SUFFIX;}

	@Override void read(XGDatafile f) throws IOException
	{	LOG.info("start parsing: " + f.getAbsolutePath());

		FileInputStream fis = new FileInputStream(f);
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
				{	XGMessage m = XGMessage.newMessage(f, Arrays.copyOfRange(tmp, first, i + 1), false);
					if(m instanceof XGMessageBulkDump) f.buffer.add((XGMessageBulkDump)m);
				}
				catch ( InvalidMidiDataException e)
				{	LOG.severe(e.getMessage());
					JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, e.getMessage());
				}
				start = false;
			}
			i++;
		}
		LOG.info(f.buffer.size() + " messages parsed from " + f);
		fis.close();
	}

	@Override void write(XGDatafile f)
	{	int count = 0;
		if(f.canWrite())
		{	try(FileOutputStream fos = new FileOutputStream(f))
			{	for(XGMessage r : f.buffer)
				{	fos.write(r.getByteArray());
					count++;
				}
				fos.close();
				LOG.info(count + " messages saved to " + f);
			}
			catch(IOException e)
			{	LOG.severe(e.getMessage());
				JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, e.getMessage());
			}
		}
		else
		{	LOG.info("cannot write: " + f);
			JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, "cannot write: " + f);
		}
	}

	@Override public String getDescription(){	return SYX_DESCRIPTION;}
}
