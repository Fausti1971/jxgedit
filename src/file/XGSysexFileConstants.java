package file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public interface XGSysexFileConstants
{
	static final FileFilter SYX_FILEFILTER = new FileFilter()
	{	@Override public String getDescription()
		{	return "Raw SystemExlusive File";
		}
		@Override public boolean accept(File f)
		{	return f.exists() && f.canRead() && f.isFile() && f.getName().endsWith(".syx");
		}
	};
}
