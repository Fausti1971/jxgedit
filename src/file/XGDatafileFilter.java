package file;

import application.XGLoggable;import tag.XGTagable;import tag.XGTagableSet;import java.io.File;import java.io.IOException;
import javax.swing.filechooser.FileFilter;

public abstract class XGDatafileFilter extends FileFilter implements XGLoggable, XGTagable
{
	public static final XGTagableSet<XGDatafileFilter> DATAFILE_FILTERS = new XGTagableSet<>();

	public final FileFilter SUPPORTED_FILEFILTER = new FileFilter()
	{	@Override public String getDescription()
		{	return "All Supported Files";
		}
		@Override public boolean accept(File f)
		{	for(XGDatafileFilter ff : DATAFILE_FILTERS) if(ff.accept(f)) return true;
			return false;
		}
	};

	public static FileFilter[] getFilters()
	{	FileFilter[] ff = new FileFilter[DATAFILE_FILTERS.size() + 1];
		int i = 0;
		ff[i++] = new FileFilter()
			{	@Override public String getDescription()
				{	return "All Supported Files";
				}
				@Override public boolean accept(File f)
				{	for(XGDatafileFilter ff : DATAFILE_FILTERS) if(ff.accept(f)) return true;
					return false;
				}
			};
		for(FileFilter f : DATAFILE_FILTERS)  ff[i++] = f;
		return ff;
	}

	public static XGDatafileFilter getFilter(File f) throws XGDatafileFilterException
	{	for(XGDatafileFilter ff : DATAFILE_FILTERS) if(ff.accept(f)) return ff;
		throw new XGDatafileFilterException("unknown fileformat of " + f);
	}

/**********************************************************************************************************************/

	XGDatafileFilter()
	{	DATAFILE_FILTERS.add(this);
	}

	abstract String getSuffix();
	abstract void read(XGDatafile f)throws IOException;
	abstract void write(XGDatafile f);

	@Override public boolean accept(File f)
	{	if(f.isDirectory()) return true;
		int dot = f.getName().lastIndexOf(".");
		if(dot == -1) return false;
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
	@Override public String getTag()
	{	return this.getSuffix();
	}

	@Override public String toString(){	return this.getDescription();}
}
