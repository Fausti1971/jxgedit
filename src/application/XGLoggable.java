package application;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public interface XGLoggable
{
	static final Logger LOG = Logger.getAnonymousLogger();
	static final Level PROGRESS = new XGLogLevel("PROGRESS", Level.INFO.intValue() - 1, null);
	static final int[] BARDIM = {0, 0, 0};
	static final int MIN = 0, MAX = 1, VALUE = 2;
	static final Filter FINEFILTER = new Filter()
	{	@Override public boolean isLoggable(LogRecord record)
		{	return(record.getLevel().intValue() == Level.FINE.intValue());
		}
	};

	class XGLogLevel extends Level
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected XGLogLevel(String name, int value, String resourceBundleName)
		{	super(name, value, resourceBundleName);
		}
	}
}
