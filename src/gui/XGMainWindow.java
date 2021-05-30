package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import application.*;
import static application.JXG.*;
import device.*;
import static java.awt.BorderLayout.*;
import module.*;
import static module.XGModuleType.TYPES;
import static value.XGValueStore.STORE;
import xml.*;

public class XGMainWindow extends XGWindow implements ComponentListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	private static XMLNode config = null;
	public static XGMainWindow window = null;

	public static void init()
	{	config = JXG.config.getChildNodeOrNew(XMLNodeConstants.TAG_WIN);
		window = new XGMainWindow();
	}

/**********************************************************************************************************************/

	private final XGStatusBar status = new XGStatusBar();

	public XGMainWindow()
	{	super(null, APPNAME + " - " + XGDevice.getDevice());
		this.setJMenuBar(this.createMenu());
		this.setContentPane(this.createContent());

		this.addComponentListener(this);

		this.setMinimumSize(new Dimension(MIN_W, MIN_H));
		this.setBounds(
			config.getIntegerAttribute(ATTR_X, MIN_X),
			config.getIntegerAttribute(ATTR_Y, MIN_Y),
			config.getIntegerAttribute(ATTR_W, MIN_W),
			config.getIntegerAttribute(ATTR_H, MIN_H));
//		this.pack();
		this.setVisible(true);
	}

	public XGStatusBar getStatusBar()
	{	return this.status;
	}

	private JMenuBar createMenu()
	{	XGDevice dev = XGDevice.getDevice();
		JMenuBar bar = new JMenuBar();

		JMenu file = new JMenu("File");

		JMenuItem load = new JMenuItem("Load Dump...");
		load.addActionListener((ActioEvent)->{dev.load();});
		file.add(load);

		JMenuItem save = new JMenuItem("Save Dump...");
		save.addActionListener((ActioEvent)->{dev.save();});
		file.add(save);

		file.addSeparator();

		JMenuItem settings = new JMenuItem("Settings");
		settings.addActionListener((ActionEvent)->{XGSettingsWindow.getWindow(APPNAME + " Settings").setVisible(true);});
		file.add(settings);

		file.addSeparator();

		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener((ActioEvent)->{quit();});
		file.add(quit);

		bar.add(file);

		JMenu midi = new JMenu("Midi");

		JMenuItem requestAll = new JMenuItem("Request All");
		requestAll.addActionListener((ActionEvent)->{new Thread(() -> {dev.transmitAll(XGMidi.getMidi(), STORE);}).start();});
		midi.add(requestAll);

		JMenuItem transmitAll = new JMenuItem("Transmit All");
		transmitAll.addActionListener((ActionEvent)->{new Thread(() -> {dev.transmitAll(STORE, XGMidi.getMidi());}).start();});
		midi.add(transmitAll);

		bar.add(midi);

		return bar;
	}

	private JComponent createContent()
	{
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		XGModule master = TYPES.get("master").getModules().get(0);
		XGModule reverb = TYPES.get("rev").getModules().get(0);
		XGModule chorus = TYPES.get("cho").getModules().get(0);
		XGModule variation = TYPES.get("var").getModules().get(0);
		XGModule eq = TYPES.get("eq").getModules().get(0);

		JPanel sysPane = new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2), 2, 2);
		JButton mb = new JButton("<html><center><b>" + "Master" + "</b></center></html>");
		mb.setFont(MEDIUM_FONT);
		mb.addActionListener((ActionEvent e)->{XGEditWindow.getEditWindow(master).setVisible(true);});
		sysPane.add(mb, gbc);

		gbc.gridx = 1;
		XGButton2 rb = new XGButton2(reverb.getValues().get("rev_program"));
		rb.addActionListener((ActionEvent e)->{XGEditWindow.getEditWindow(reverb).setVisible(true);});
		sysPane.add(rb, gbc);

		gbc.gridx = 3;
		XGButton2 cb = new XGButton2(chorus.getValues().get("cho_program"));
		cb.addActionListener((ActionEvent e)->{XGEditWindow.getEditWindow(chorus).setVisible(true);});
		sysPane.add(cb, gbc);

		gbc.gridx = 4;
		XGButton2 vb = new XGButton2(variation.getValues().get("var_program"));
		vb.addActionListener((ActionEvent e)->{XGEditWindow.getEditWindow(variation).setVisible(true);});
		sysPane.add(vb, gbc);

		gbc.gridx = 5;
		XGButton2 eb = new XGButton2(eq.getValues().get("eq_program"));
		eb.addActionListener((ActionEvent e)->{XGEditWindow.getEditWindow(eq).setVisible(true);});
		sysPane.add(eb, gbc);

		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setFont(MEDIUM_FONT);
		java.util.Set<String> adr = new java.util.LinkedHashSet<>();
		adr.add("mp");
		adr.add("ad");
		adr.add("ins");
		adr.add("plugin");
		for(XGModuleType mt : TYPES) if(mt instanceof XGDrumsetModuleType) adr.add(mt.getTag());
		for(String s : adr)
		{	XGModuleType t = TYPES.get(s);
			if(t != null) tabPane.addTab(t.getName(), new JScrollPane(new XGModuleTable(t)));//TODO: hier müssen evtl. (?) noch die unbenutzten Drumsets ausgeblendet werden;
		}

		content.add(tabPane, CENTER);
		content.add(sysPane, NORTH);
		content.add(this.status, SOUTH);
		return content; 
	}

	@Override public void windowClosing(WindowEvent e)
	{	this.dispose();
		JXG.quit();
	}

	@Override public void componentResized(ComponentEvent e)
	{	config.setIntegerAttribute(ATTR_W, e.getComponent().getWidth());
		config.setIntegerAttribute(ATTR_H, e.getComponent().getHeight());
	}

	@Override public void componentMoved(ComponentEvent e)
	{	config.setIntegerAttribute(ATTR_X, e.getComponent().getX());
		config.setIntegerAttribute(ATTR_Y, e.getComponent().getY());
	}

	@Override public void componentShown(ComponentEvent e)
	{
	}

	@Override public void componentHidden(ComponentEvent e)
	{
	}
}
