package bulk;

import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import application.*;
import gui.*;
import msg.*;

/**
 * qualifiziert die implementierende Klasse als Sammler aller enthaltenen BuklDumps (getBulks()) und Transmitter (transmitAll()) bzw. Requester (requestAll()); folglich können lediglich Instanzen, die XGBulks halten (Module, Moduletype, Device) als XGBulkDumper fungieren; vielleicht auch einst für Copy & Paste zu missbrauchen;
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
		ProgressMonitor pm = new ProgressMonitor(XGMainWindow.MAINWINDOW, "transmitting to " + dest, "", 0, set.size());
		pm.setMillisToDecideToPopup(0);
		pm.setMillisToPopup(0);
		for(XGBulk b : set)
		{	try
			{	dest.submit(b.getMessage());
				pm.setNote(b.toString());
				pm.setProgress(++transmitted);
//				LOG.info(b + " transmitted");
				if(pm.isCanceled()) break;
			}
			catch( XGMessengerException | InvalidMidiDataException e)
			{	LOG.severe(e.getMessage());
				JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, e.getMessage());
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
		long startTime = System.currentTimeMillis(), wholeTime = 0, maxResponseTime = 0, responseTime = 0;
		XGAddressableSet<XGBulk> set = this.getBulks();
		ProgressMonitor pm = new ProgressMonitor(XGMainWindow.MAINWINDOW, "requesting from " + dest, "", 0, set.size());
		pm.setMillisToDecideToPopup(0);
		pm.setMillisToPopup(0);
		for(XGBulk b : set)
		{	try
			{	requested++;
				XGMessageBulkRequest req = new XGMessageBulkRequest(b, b);
				dest.submit(req);
				if(req.isResponsed())
				{	pm.setNote(b.toString());
					pm.setProgress(++responsed);
					responseTime = (req.getResponse().getTimeStamp() - req.getTimeStamp());
					wholeTime += responseTime;
					maxResponseTime = Math.max(maxResponseTime, responseTime);
//					LOG.info("response for " + b + " within " + (b.getMessage().getTimeStamp() - reqTime) + " ms");
//					LOG.info(r.getResponse().toHexString());
				}
				else
				{	LOG.severe("no response for " + b + " within " + (System.currentTimeMillis() - req.getTimeStamp()) + " ms");
					pm.setNote(b.toString());
				}
				if(pm.isCanceled()) break;
			}
			catch(InvalidMidiDataException | XGMessengerException e)
			{	LOG.severe(e.getMessage());
			}
		}
		Level level;
		if(requested - responsed == 0)
			level = Level.INFO;
		else level = Level.WARNING;
		LOG.log(level, responsed + " (of " + requested + ") requests responsed by " + dest + " and transmitted within " + (System.currentTimeMillis() - startTime) + " (avg=" + wholeTime / responsed + "/max=" + maxResponseTime + ") ms");
	}

}
