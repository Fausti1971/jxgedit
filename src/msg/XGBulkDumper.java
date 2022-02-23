package msg;

import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.ProgressMonitor;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import application.*;
import gui.*;

/**
 * qualifiziert die implementierende Klasse als Sammler aller enthaltenen BuklDumps (getBulks()) und Transmitter derselben (transmitAll())
 * @author thomas
 *
 */
public interface XGBulkDumper extends XGLoggable
{
	XGAddressableSet<XGAddress> getBulks();

/**
 * erfragt alle im BulkDumper konfigurierten (in structure.xml deklarierten) Bulks von der src und übermittelt die jeweilige Response zum dest
 * @param src Datenquelle
 * @param dest Datenziel
 */
	default void transmitAll(XGMessenger src, XGMessenger dest)
	{	if(src == null || dest == null) return;

		int requested = 0, responsed = 0;
		long time = System.currentTimeMillis();
		XGRequest r = null;
		XGAddressableSet<XGAddress> set = this.getBulks();
		XGAddressableSet<XGRequest> missed = new XGAddressableSet<>();
		ProgressMonitor pm = new ProgressMonitor(XGMainWindow.window, src + " to " + dest, "", 0, set.size());
		pm.setMillisToDecideToPopup(0);
		pm.setMillisToPopup(0);
		for(XGAddress b : set)
		{	try
			{	r = new XGMessageBulkRequest(dest, src, b);
				src.request(r);
				++requested;
				if(r.isResponsed())
				{	dest.submit(r.getResponse());
					pm.setNote(r.toString());
					pm.setProgress(++responsed);
					LOG.info("response for " + r + " within " + (r.getResponse().getTimeStamp() - r.getTimeStamp()) + "ms");
				}
				else
				{	LOG.severe("no response for " + r + " within " + (System.currentTimeMillis() - r.getTimeStamp()) + "ms");
					pm.setNote(r.toString());
					missed.add(r);
				}
				if(pm.isCanceled()) break;
			}
			catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e)
			{	LOG.log(Level.SEVERE, e.getMessage());
			}
		}
		Level level;
		if(requested - responsed == 0) level = Level.INFO;
		else level = Level.SEVERE;
		LOG.log(level, responsed + " (of " + requested + ") requests responsed by \"" + src + "\" and transmitted to \"" + dest + "\" within " + (System.currentTimeMillis() - time) + " ms");
	}

}
