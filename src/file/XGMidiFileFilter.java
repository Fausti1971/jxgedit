package file;

import gui.XGMainWindow;import msg.XGMessage;import msg.XGMessageBulkDump;import javax.sound.midi.*;import javax.swing.*;import java.io.EOFException;
import java.io.IOException;import java.util.Map;

public class XGMidiFileFilter extends XGDatafileFilter
{
	private static final String MID_SUFFIX = ".mid";
	private static final String MID_DESCRIPTION = "Standard Midifile";
	private static final int DEF_MIDIFILERESOLUTION = 96;
	private static final MidiFileFormat DEF_MIDIFILEFORMAT = new MidiFileFormat(0, Sequence.PPQ, DEF_MIDIFILERESOLUTION, MidiFileFormat.UNKNOWN_LENGTH, MidiFileFormat.UNKNOWN_LENGTH);
	private static Sequence DEF_SEQUENCE  = null;
	static
	{	try
		{	DEF_SEQUENCE=new Sequence(Sequence.PPQ, DEF_MIDIFILERESOLUTION);
		}
		catch(InvalidMidiDataException e)
		{	LOG.severe(e.getMessage());
		}
	}


	@Override String getSuffix(){	return MID_SUFFIX;}

	@Override public String getDescription(){	return MID_DESCRIPTION;}

	@Override void read(XGDatafile f) throws IOException
	{	try
		{	MidiFileFormat format = MidiSystem.getMidiFileFormat(f);
			LOG.info("MidifileFormat=" + format.getType());
			Sequence seq = MidiSystem.getSequence(f);
			Track[] tracks = seq.getTracks();
			int ts = 0;
			LOG.info(f + " has " + tracks.length + " tracks");
			for(Track track : tracks)
			{	ts = track.size();
				LOG.info("track has " + ts + " events");
				for(int i = 0; i < ts; i++)
				{	MidiEvent e = track.get(i);
//LOG.info("reading event " + (i + 1) + "/" + ts);
					try
					{	XGMessage m = XGMessage.newMessage(f, e.getMessage());
						if(m instanceof XGMessageBulkDump) f.buffer.add((XGMessageBulkDump)m);
//						LOG.info(m.toHexString());
					}
					catch(InvalidMidiDataException ex)
					{	LOG.info(ex.getMessage());
					}
				}
			}
		}
		catch(InvalidMidiDataException e)
		{	LOG.warning("read (InMiDaEx)): " + e.getMessage());
			JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, e.getMessage());
		}
		catch(EOFException e)
		{	LOG.warning("read (EOFEx): " + e.getMessage());
		}
	}

	@Override void write(XGDatafile f)
	{	MidiFileFormat format = DEF_MIDIFILEFORMAT;
		Sequence seq = DEF_SEQUENCE;

		try
		{	seq = MidiSystem.getSequence(f);
			for(Track t : seq.getTracks()) seq.deleteTrack(t);
			format = MidiSystem.getMidiFileFormat(f);
		}
		catch(InvalidMidiDataException | IOException e)
		{	LOG.info("getSequence: " + e.getMessage());
		}

		if(seq == null) return;
		Track track = seq.createTrack();
		int i = 0;
		for(XGMessageBulkDump m : f.buffer)
		{	track.add(new MidiEvent(m, i += m.getLength()));
		}

		try
		{	MidiSystem.write(seq, format.getType(), f);
			LOG.info(f + " saved");
		}
		catch(IOException e)
		{	LOG.info("write error (I/O): " + e);
		}
	}
}
