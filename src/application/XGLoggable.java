package application;

import java.util.logging.Logger;

public interface XGLoggable
{
	static final Logger LOG = Logger.getAnonymousLogger();

//	default void logInitSuccess()
//	{	LOG.info(this.getClass().getSimpleName() + " " + this + " initialized");
//	}
}
