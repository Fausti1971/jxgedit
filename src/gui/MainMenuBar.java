package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import application.XG;
import file.XGSysexFile;
import obj.XGObjectType;

public class MainMenuBar extends JMenuBar
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;
	private static Logger log = Logger.getAnonymousLogger();

/**************************************************************************************************************************/

	private JMenuItem
		load = new JMenuItem("Open SYX..."),
		save = new JMenuItem("Save SYX..."),
		quit = new JMenuItem("Quit"),
		upload = new JMenuItem("Upload"),
		download = new JMenuItem("Download");

	private JMenu
		file = new JMenu("File"),
		midi = new JMenu("MIDI");

	public MainMenuBar()
	{	super();
		
		this.load.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	new XGSysexFile().load();
			}
		});
		this.save.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	System.out.println("not implemented");
			}
		});
		this.quit.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	XG.quit();
			}
		});
		this.file.add(this.load);
		this.file.add(this.save);
		this.file.add(new JSeparator());
		this.file.add(this.quit);

		this.midi.add(this.upload);

		this.download.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	XGObjectType.requestAll();
			}
		});
		this.midi.add(this.download);

		super.add(this.file);
		super.add(this.midi);
	}
}
