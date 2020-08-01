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
	{	this.setPreferredSize(new Dimension(0, XGComponent.GRID));
		this.setOpaque(true);
		LOG.addHandler(new BarLogger(this));
	}

/***********************************************************************************************************/

	private class BarLogger extends Handler
	{

/*************************************************************************************************************/

		private final XGStatusBar bar;

		public BarLogger(XGStatusBar bar)
		{	this.bar = bar;
		}

		@Override public void publish(LogRecord record)
		{	int level = record.getLevel().intValue();
			String msg = record.getMessage();

			if(level == Level.SEVERE.intValue()) bar.setBackground(Color.red);
			if(level == Level.WARNING.intValue()) bar.setBackground(Color.yellow);
			if(level == Level.INFO.intValue()) bar.setBackground(Color.green);

			this.bar.setText(msg);
			this.bar.setToolTipText(msg);
		}

		@Override public void flush()
		{
			// TODO Auto-generated method stub
			
		}

		@Override public void close() throws SecurityException
		{
			// TODO Auto-generated method stub
			
		}
	}
}
