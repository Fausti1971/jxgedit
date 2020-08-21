package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JLabel;
import application.XGLoggable;
import application.XGMath;

public class XGStatusBar extends JLabel implements XGLoggable
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/***********************************************************************************************************/

	private int min, max, value;
	private Color color;
	private int width, height;
	private Graphics2D g2;

	public XGStatusBar()
	{	this.setPreferredSize(new Dimension(0, XGComponent.GRID));
//		this.setOpaque(true);
		LOG.addHandler(new BarLogger(this));
	}

	void setProgress(Color col, String msg, int min, int max, int value)
	{	this.min = min;
		this.max = max;
		this.value = value;
		this.setText(msg);
		this.setToolTipText(msg);
		this.color = col;
	}

	@Override public void paint(Graphics g)
	{	this.height = this.getHeight();
		this.width = XGMath.linearIO(this.value, this.min, this.max, 0, this.getWidth());
		this.g2 = (Graphics2D)g.create();
		this.g2.setColor(this.color);
		this.g2.fillRect(0, 0, this.width, this.height);
		this.g2.dispose();
		super.paint(g);
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
			if(level == Level.FINE.intValue()) color = Color.green;

			this.bar.setProgress(color, msg, BARDIM[MIN], BARDIM[MAX], BARDIM[VALUE]);
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
