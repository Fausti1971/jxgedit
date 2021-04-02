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
 * erfragt alle im BulkDumper konfigurierten (in bulks.xml deklarierten) Bulks von bei der src und übermittelt die jeweilige Response zum dest
 * @param src
 * @param dest
 */
	default void transmitAll(XGMessenger src, XGMessenger dest)
	{	if(src == null || dest == null) return;

		int requested = 0, responsed = 0;
		long time = System.currentTimeMillis();
		XGRequest r = null;
		XGAddressableSet<XGAddress> set = this.getBulks();
		XGAddressableSet<XGRequest> missed = new XGAddressableSet<>();
		ProgressMonitor pm = new ProgressMonitor(XGMainWindow.getWindow(), src + " to " + dest, "", 0, set.size());
		pm.setMillisToDecideToPopup(0);
		pm.setMillisToPopup(0);
		for(XGAddress b : set)
		{	try
			{	r = new XGMessageBulkRequest(dest, src, b);
				r.request();
				++requested;
				if(r.isResponsed())
				{	dest.submit(r.getResponse());
					pm.setNote(r.toString());
					pm.setProgress(++responsed);
				}
				else
				{	LOG.severe("no response for " + r);
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
