package gui;

import static application.XGLoggable.LOG;
import javax.swing.*;import javax.swing.text.DefaultCaret;
import java.awt.*;import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class XGSplashScreen extends JFrame 
{	private final Handler handler;

	public XGSplashScreen()
	{	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle("initializing JXG, please wait...");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JTextArea status = new JTextArea();
		((DefaultCaret)status.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);//autoscroll
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

		this.setResizable(true);
		this.setSize(d.width/4, d.height/4);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void dispose()
	{	LOG.removeHandler(this.handler);
		super.dispose();
	}
}
