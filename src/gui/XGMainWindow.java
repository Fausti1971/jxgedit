package gui;

import java.awt.*;
import java.awt.event.*;import java.util.NoSuchElementException;
import javax.swing.*;
import adress.XGAddressableSet;import application.*;
import static application.JXG.*;
import device.*;
import file.XGSysexFile;import static java.awt.BorderLayout.*;
import module.*;
import static module.XGModuleType.TYPES;
import value.XGValue;import xml.*;

public class XGMainWindow extends XGWindow
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/***********************************************************************************************************************/

	public XGMainWindow(XMLNode cfg)
	{	super("main");
		this.setJMenuBar(this.createMenu());
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	private JMenuBar createMenu()
	{	JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");

		JMenuItem load = new JMenuItem("Open Dump...");
		load.addActionListener((ActioEvent)->XGSysexFile.load(XGDevice.device));
		file.add(load);

		JMenuItem save = new JMenuItem("Save Dump...");
		save.addActionListener((ActioEvent)->XGSysexFile.save(XGDevice.device));
		file.add(save);

		file.addSeparator();

		JMenuItem settings = new JMenuItem("Settings");
		settings.addActionListener((ActionEvent)->XGSettingsWindow.getWindow(APPNAME + " Settings").setVisible(true));
		file.add(settings);

		file.addSeparator();

		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener((ActioEvent)->this.windowClosing(null));
		file.add(quit);

		bar.add(file);

		JMenu midi = new JMenu("Midi");

		JMenuItem requestAll = new JMenuItem("Request All");
		requestAll.addActionListener((ActionEvent)->new Thread(() -> XGDevice.device.requestAll(XGMidi.getMidi())).start());
		midi.add(requestAll);

		JMenuItem transmitAll = new JMenuItem("Transmit All");
		transmitAll.addActionListener((ActionEvent)->new Thread(() -> XGDevice.device.transmitAll(XGMidi.getMidi())).start());
		midi.add(transmitAll);

		bar.add(midi);

		return bar;
	}

	@Override public JComponent createContent()
	{
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		JPanel sysPane = new JPanel();
		sysPane.setLayout(new BoxLayout(sysPane, BoxLayout.X_AXIS));

		JTabbedPane tabPane = new JTabbedPane();

		for(XGModuleType mt : TYPES)
		{	XGAddressableSet<XGModule> set = mt.getModules();
			XGButton2 button;
			if(set.size() == 1)
			{	XGModule mod = set.get(0);
				XGValue v;
				try
				{	String infoTag = mt.getInfoTags().iterator().next();
					v = mod.getValues().get(infoTag);
				}
				catch(NoSuchElementException e){	v = null;}
				button = new XGButton2(mt.getName(), v);
				button.addActionListener((ActionEvent e)->XGEditWindow.getEditWindow(mod).setVisible(true));
				sysPane.add(button);
			}
			if(set.size() > 1) tabPane.addTab(mt.getName(), new JScrollPane(new XGModuleTable(mt)));
		}

		content.add(sysPane, NORTH);
		content.add(tabPane, CENTER);
		/**********************************************************************************************************************/
		XGStatusBar status;content.add(status = new XGStatusBar(), SOUTH);
		return content; 
	}

	@Override public void windowClosing(WindowEvent e)
	{	if(JOptionPane.showConfirmDialog(this, "Quit?", "Closing...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		{	this.dispose();
			JXG.quit();
		}
	}

	public void propertyChanged(XGProperty attr){	System.out.println("property changed: " + attr);}
}
