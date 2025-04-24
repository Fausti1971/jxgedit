package file;

import application.XGLoggable;import java.io.File;import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;import javax.swing.filechooser.FileFilter;

public abstract class XGDatafileFilter extends FileFilter implements XGLoggable
{
	public static final XGDatafileFilter SYX_FILEFILTER = new XGSysexFileFilter();
	public static final XGDatafileFilter MID_FILEFILTER = new XGMidiFileFilter();
	public static final FileFilter SUPPORTED_FILEFILTER = new FileFilter()
	{	@Override public String getDescription()
		{	return "All Supported Files";
		}
		@Override public boolean accept(File f)
		{	return MID_FILEFILTER.accept(f) || SYX_FILEFILTER.accept(f);
		}
	};

	public static XGDatafileFilter getFilter(File f) throws XGDatafileFilterException
	{	if(SYX_FILEFILTER.accept(f)) return SYX_FILEFILTER;
		if(MID_FILEFILTER.accept(f)) return MID_FILEFILTER;
		throw new XGDatafileFilterException("can't determine fileformat of " + f);
	}

/**********************************************************************************************************************/

	abstract String getSuffix();
	abstract void read(XGDatafile f)throws IOException;
	abstract void write(XGDatafile f);

	@Override public boolean accept(File f)
	{	if(f.isDirectory()) return true;
		int dot = f.getName().lastIndexOf(".");
		if(dot == -1) return false;
	//	String suffix = f.getName().substring(f.getName().length() - 4);
		String suffix = f.getName().substring(dot);
		return suffix.equalsIgnoreCase(this.getSuffix());
	}
///**
//* überprüft, ob das im Fileselector selektierte File das Suffix des im FileSelector selektierten FileFilters besitzt, ändert dies bei Bedarf und liefert den Filepathstring zurück 
//*/
//	static void appendSuffix(StringBuffer s, XGDatafileFilter ff)
//	{	if(ff.getSuffix().equalsIgnoreCase(s.substring(s.length() - 4))) return;
//		else s.replace(s.length() - 4, s.length(), ff.getSuffix());
//	}

	@Override public String toString(){	return this.getDescription();}
}
