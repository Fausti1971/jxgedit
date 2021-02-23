package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JLabel;
import application.XGLoggable;

public class XGStatusBar extends JLabel implements XGLoggable
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/***********************************************************************************************************/

	public XGStatusBar()
	{
		this.setOpaque(true);
		LOG.addHandler(new BarLogger(this));
//		this.logInitSuccess();
	}

	@Override public String toString()
	{	return "StatusBar";
	}

/***********************************************************************************************************/

	private class BarLogger extends Handler
	{

/*************************************************************************************************************/

		private final XGStatusBar bar;
		private Color color;

		public BarLogger(XGStatusBar bar)
		{	this.bar = bar;
			this.color = bar.getBackground();
		}

		@Override public void publish(LogRecord record)
		{	int level = record.getLevel().intValue();
			String msg = record.getMessage();

			if(level == Level.SEVERE.intValue()) color = Color.red;
			if(level == Level.WARNING.intValue()) color = Color.yellow;
			if(level == Level.INFO.intValue()) color = Color.green;

			this.bar.setText(msg);
			this.bar.setToolTipText(msg);
			this.bar.setBackground(color);
			}

		@Override public void flush()
		{
System.out.println("Handler.flush() called...");
		}

		@Override public void close() throws SecurityException
		{
System.out.println("Handler.close() called...");
		}
	}
}
