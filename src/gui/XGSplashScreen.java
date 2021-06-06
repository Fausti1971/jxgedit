package gui;

import static application.XGLoggable.LOG;import static gui.XGUI.LARGE_FONT;import static gui.XGUI.MEDIUM_FONT;import javax.swing.*;import java.awt.*;import java.util.logging.Handler;import java.util.logging.LogRecord;

public class XGSplashScreen extends JFrame 
{	private final Handler handler;

	public XGSplashScreen()
	{	this.setTitle("initializing JXG, please wait...");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JLabel status = new JLabel();
		status.setFont(MEDIUM_FONT);
		this.handler = new Handler()
		{	public void publish(LogRecord record)
			{	status.setText(record.getMessage());
			}
			public void flush()
			{
			}
			public void close() throws SecurityException
			{
			}
		};
		LOG.addHandler(this.handler);
		this.getContentPane().add(status);

		this.setResizable(false);
//		this.setUndecorated(true);
		this.setSize(600, 200);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void dispose()
	{	LOG.removeHandler(this.handler);
		super.dispose();
	}
}
