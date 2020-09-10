package msg;

import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.ProgressMonitor;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import application.XGLoggable;
import gui.XGWindow;

/**
 * qualifiziert die implementierende Klasse als Sammler aller enthaltenen BuklDumps (getBulks()) und Transmitter derselben (transmitAll())
 * @author thomas
 *
 */
public interface XGBulkDumper extends XGLoggable
{
	XGAddressableSet<XGBulkDump> getBulks();

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
		XGAddressableSet<XGBulkDump> set = this.getBulks();
		XGAddressableSet<XGRequest> missed = new XGAddressableSet<>();
		ProgressMonitor pm = new ProgressMonitor(XGWindow.getRootWindow(), src + " -> " + dest, "", 0, set.size());
		pm.setMillisToDecideToPopup(0);
		pm.setMillisToPopup(0);
		for(XGBulkDump b : set)
		{	try
			{	r = new XGMessageBulkRequest(dest, src, b);
				r.request();
				++requested;
				if(r.isResponsed())
				{	dest.submit(r.getResponse());
					pm.setNote(r.toString());
					pm.setProgress(++responsed);
				}
				missed.add(r);
			}
			catch(InvalidXGAddressException | InvalidMidiDataException | XGMessengerException e)
			{	LOG.log(Level.SEVERE, e.getMessage());
			}
		}
		Level level;
		if(requested - responsed == 0) level = Level.INFO;
		else level = Level.SEVERE;
		LOG.log(level, requested + " requested / " + responsed + " responsed by " + src + " transmitted to " + dest + " within " + (System.currentTimeMillis() - time) + " ms");
	}

}
