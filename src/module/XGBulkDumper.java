package module;

import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.ProgressMonitor;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import application.*;
import gui.*;
import module.XGBulk;import msg.XGMessageBulkRequest;import msg.XGMessenger;import msg.XGMessengerException;import msg.XGRequest;

/**
 * qualifiziert die implementierende Klasse als Sammler aller enthaltenen BuklDumps (getBulks()) und Transmitter (transmitAll()) bzw. Requester (requestAll()); folglich können lediglich Instanzen, die XGBulks halten (Module, Moduletype, Device) als XGBulkDumper fungieren; vielleicht auch einst für Copy & Paset zu missbrauchen;
 * @author thomas
 *
 */
public interface XGBulkDumper extends XGLoggable
{
	XGAddressableSet<XGBulk> getBulks();

	default void transmitAll(XGMessenger dest)
	{	if(dest == null) return;

		int transmitted = 0;
		long time = System.currentTimeMillis();
		XGAddressableSet<XGBulk> set = this.getBulks();
		ProgressMonitor pm = new ProgressMonitor(XGMainWindow.window, "transmitting to " + dest, "", 0, set.size());
		pm.setMillisToDecideToPopup(0);
		pm.setMillisToPopup(0);
		for(XGBulk b : set)
		{	try
			{	dest.submit(b.getMessage());
				pm.setNote(b.getMessage().toString());
				pm.setProgress(++transmitted);
				LOG.info(b.getMessage() + " transmitted");
				if(pm.isCanceled()) break;
			}
			catch(InvalidXGAddressException |  XGMessengerException e)
			{	LOG.severe(e.getMessage());
			}
		}
		LOG.info(transmitted + " messages transmitted to " + dest + " within " + (System.currentTimeMillis() - time) + " ms");
	}

/**
 * erfragt alle im BulkDumper konfigurierten Bulks von dest und übermittelt die jeweilige Response zu bulk.getSource()
 * @param dest Datenherkunft (woher sollen die Daten abgefragt werden); Addressat steht im Bulk, an diesen wird die Antwort zurückübermittelt
 */
	default void requestAll(XGMessenger dest)
	{	if(dest == null) return;

		int requested = 0, responsed = 0;
		long time = System.currentTimeMillis();
		XGRequest r;
		XGAddressableSet<XGBulk> set = this.getBulks();
		XGAddressableSet<XGRequest> missed = new XGAddressableSet<>();
		ProgressMonitor pm = new ProgressMonitor(XGMainWindow.window, "transmitting...", "", 0, set.size());
		pm.setMillisToDecideToPopup(0);
		pm.setMillisToPopup(0);
		for(XGBulk b : set)
		{	try
			{	r = new XGMessageBulkRequest(b, b);
				dest.submit(r);
				++requested;
				if(r.isResponsed())
				{	pm.setNote(r.toString());
					pm.setProgress(++responsed);
					LOG.info("response for " + r + " within " + (r.getResponse().getTimeStamp() - r.getTimeStamp()) + " ms");
//					LOG.info(r.getResponse().toHexString());
				}
				else
				{	LOG.severe("no response for " + r + " within " + (System.currentTimeMillis() - r.getTimeStamp()) + " ms");
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
		else level = Level.WARNING;
		LOG.log(level, responsed + " (of " + requested + ") requests responsed by " + dest + " and transmitted within " + (System.currentTimeMillis() - time) + " ms");
	}

}
