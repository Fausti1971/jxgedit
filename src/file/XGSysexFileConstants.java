package file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public interface XGSysexFileConstants
{	String SYX_SUFFIX = ".syx";

	static final FileFilter SYX_FILEFILTER = new FileFilter()
	{	@Override public String getDescription()
		{	return "Raw SystemExlusive File";
		}
		@Override public boolean accept(File f)
		{	if(f.isDirectory()) return true;
			String suffix = f.getName().substring(f.getName().length() - 4);
			return f.isFile() && suffix.equalsIgnoreCase(SYX_SUFFIX);
		}
	};
}
