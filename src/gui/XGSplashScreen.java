package gui;

import static application.XGLoggable.LOG;
import javax.swing.*;
import java.awt.*;import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class XGSplashScreen extends JFrame 
{	private final Handler handler;

	public XGSplashScreen()
	{	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle("initializing JXG, please wait...");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JTextArea status = new JTextArea();
		status.setFont(status.getFont().deriveFont((float)d.height / 100));
		this.handler = new Handler()
		{	public void publish(LogRecord record)
			{	status.append(record.getMessage() + "\n");
			}
			public void flush()
			{
			}
			public void close() throws SecurityException
			{
			}
		};
		LOG.addHandler(this.handler);
		this.getContentPane().add(new JScrollPane(status));

		this.setResizable(false);
		this.setSize(d.width/5, d.height/5);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void dispose()
	{	LOG.removeHandler(this.handler);
		super.dispose();
	}
}
