package msg;

import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import application.XGLoggable;

/**
 * qualifiziert die implementierende Klasse als Sammler aller enthaltenen BuklDumps (getBulks()) und Transmitter derselben (transmitAll())
 * @author thomas
 *
 */
public interface XGBulkDumper extends XGLoggable
{	XGAddressableSet<XGBulkDump> getBulks();

	default void transmitAll(XGMessenger src, XGMessenger dest)
	{	if(src == null || dest == null) return;
		int count = 0;
		long time = System.currentTimeMillis();
		XGRequest r = null;
		XGAddressableSet<XGBulkDump> set = this.getBulks();
		BARDIM[MIN] = 0;
		BARDIM[MAX] = set.size();
		for(XGBulkDump b : set)
		{	try
			{	r = new XGMessageBulkRequest(dest, src, b);
				r.request();
				if(r.isResponsed())
				{	dest.submit(r.getResponse());
					BARDIM[VALUE] = ++count;
					LOG.log(PROGRESS, r + " responsed by " + r.getResponse() + " within " + (r.getResponse().getTimeStamp() - r.getTimeStamp()) + " ms");
				}
			}
			catch(InvalidXGAddressException | InvalidMidiDataException e)
			{	LOG.log(Level.SEVERE, e.getMessage());
			}
		}
		Level level;
		if(set.size() - count == 0) level = Level.INFO;
		else level = Level.SEVERE;
		LOG.log(level, count + "/" + set.size() + " dumps transmitted from " + src + " to " + dest + " within " + (System.currentTimeMillis() - time) + " ms");
	}

}
