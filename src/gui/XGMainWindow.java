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

public class XGMainWindow extends JFrame implements WindowListener, ComponentListener, ConfigurationConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	private static XMLNode config = null;
	private static XGMainWindow window = null;

	public static XGMainWindow getWindow()
	{	if(window == null) XGMainWindow.init();
		return window;
	}

	public static void init()
	{	config = JXG.config.getChildNodeOrNew(XMLNodeConstants.TAG_WIN);
		window = new XGMainWindow();
	}

	public static void updateUI()
	{	if(window != null)
		{	SwingUtilities.updateComponentTreeUI(window);
			for(Window w :window.getOwnedWindows())
			SwingUtilities.updateComponentTreeUI(w);
		}
	}

/**********************************************************************************************************************/

	private final XGStatusBar status = new XGStatusBar();

	public XGMainWindow()
	{
		this.setJMenuBar(this.createMenu());
		this.setContentPane(this.createContent());

		this.setTitle(APPNAME + " - " + XGDevice.getDevice());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(this);
		this.addComponentListener(this);

		this.setMinimumSize(new Dimension(MIN_W, MIN_H));
		this.setBounds(
			config.getIntegerAttribute(ATTR_X, MIN_X),
			config.getIntegerAttribute(ATTR_Y, MIN_Y),
			config.getIntegerAttribute(ATTR_W, MIN_W),
			config.getIntegerAttribute(ATTR_H, MIN_H));
//		this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
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
		sysPane.add(new XGKnob(master.getValues().get("master_volume")), gbc);

		gbc.gridx = 1;
		sysPane.add(new XGKnob(master.getValues().get("master_damp")), gbc);

		gbc.gridx = 2;
		sysPane.add(new XGKnob(master.getValues().get("master_transpose")), gbc);

		gbc.gridx = 3;
		sysPane.add(new XGKnob(master.getValues().get("master_tune")), gbc);

		gbc.gridx = 4;
		XGButton2 rb = new XGButton2(reverb.getValues().get("rev_program"));
		rb.addActionListener((ActionEvent e)->{XGEditWindow.getEditWindow(reverb).toFront();});
		sysPane.add(rb, gbc);

		gbc.gridx = 5;
		XGButton2 cb = new XGButton2(chorus.getValues().get("cho_program"));
		cb.addActionListener((ActionEvent e)->{XGEditWindow.getEditWindow(chorus).toFront();});
		sysPane.add(cb, gbc);

		gbc.gridx = 6;
		XGButton2 vb = new XGButton2(variation.getValues().get("var_program"));
		vb.addActionListener((ActionEvent e)->{XGEditWindow.getEditWindow(variation).toFront();});
		sysPane.add(vb, gbc);

		gbc.gridx = 7;
		XGButton2 eb = new XGButton2(eq.getValues().get("eq_program"));
		eb.addActionListener((ActionEvent e)->{XGEditWindow.getEditWindow(eq).toFront();});
		sysPane.add(eb, gbc);

		JTabbedPane tabPane = new JTabbedPane();
		String[] adr = {"mp", "ad", "ins", "plugin"};
		for(String s : adr)
		{	XGModuleType t = null;
			t = TYPES.get(s);
				if(t != null) tabPane.addTab(t.getName(), new JScrollPane(new XGModuleTable(t)));
			}

		content.add(tabPane, CENTER);
		content.add(sysPane, NORTH);
		content.add(this.status, SOUTH);
		return content; 
	}

	@Override public void windowOpened(WindowEvent e)
	{
	}

	@Override public void windowClosing(WindowEvent e)
	{
	}

	@Override public void windowClosed(WindowEvent e)
	{	JXG.quit();
	}

	@Override public void windowIconified(WindowEvent e)
	{
	}

	@Override public void windowDeiconified(WindowEvent e)
	{
	}

	@Override public void windowActivated(WindowEvent e)
	{
	}

	@Override public void windowDeactivated(WindowEvent e)
	{
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
