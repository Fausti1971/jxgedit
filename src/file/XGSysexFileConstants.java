package file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public interface XGSysexFileConstants
{
	static final FileFilter SYX_FILEFILTER = new FileFilter()
	{	public String getDescription()
		{	return "Raw SystemExlusive File";
		}
		public boolean accept(File f)
		{	return f.exists() && f.canRead() && f.isFile() && f.getName().endsWith(".syx");
		}
	};
}
