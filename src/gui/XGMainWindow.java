package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import adress.XGIdentifiableSet;import application.*;
import device.*;
import file.XGDatafile;
import static java.awt.BorderLayout.*;
import module.*;
import static module.XGModuleType.MODULE_TYPES;
import value.XGValue;import xml.*;

public class XGMainWindow extends XGWindow
{

/***********************************************************************************************************************/

	public XGMainWindow(XMLNode cfg)
	{	super("main");
//		this.setJMenuBar(this.createMenu());
		this.setLayout(new BorderLayout());
		this.add(this.createToolbar(), NORTH);
		this.add(this.createContent(), CENTER);
		this.add(new XGStatusBar(), SOUTH);
		this.setVisible(true);
	}

	private XGToolbar createToolbar()
	{	XGToolbar tb = new XGToolbar();
		tb.addAction(ACTION_LOADFILE, "Open", "load datadumpfile to memory", (ActioEvent)->XGDatafile.load(XGDevice.DEVICE));
		tb.addAction(ACTION_RECENT, "Open Recent", "load one of recently loaded datadumpfiles", (ActioEvent)->XGDatafile.recent(XGDevice.DEVICE));
		tb.addAction(ACTION_SAVEFILE, "Save", "save memory to datadumpfile", (ActioEvent)->XGDatafile.save(XGDevice.DEVICE));
		tb.addSeparator();
		tb.addAction(XGUI.ACTION_REQUEST, "Request", "request complete dump from " + XGDevice.DEVICE, (ActionEvent)->new Thread(() -> XGDevice.DEVICE.requestAll(XGMidi.getMidi())).start());
		tb.addAction(XGUI.ACTION_TRANSMIT, "Transmit", "transmit complete memory to " + XGDevice.DEVICE, (ActionEvent)->new Thread(() -> XGDevice.DEVICE.transmitAll(XGMidi.getMidi())).start());
		tb.add(Box.createHorizontalGlue());
		tb.addAction(XGUI.ACTION_CONFIGURE, "Settings", "show the settings-window", (ActionEvent)->XGSettingsWindow.getWindow().setVisible(true));

		return tb;
	}

	//private JMenuBar createMenu()
	//{	JMenuBar bar = new JMenuBar();
	//	JMenu file = new JMenu("File");
	//
	//	JMenuItem load = new JMenuItem("Open Dump...");
	//	load.addActionListener((ActioEvent)->XGDatafile.load(XGDevice.DEVICE));
	//	file.add(load);
	//
	//	JMenuItem save = new JMenuItem("Save Dump...");
	//	save.addActionListener((ActioEvent)->XGDatafile.save(XGDevice.DEVICE));
	//	file.add(save);
	//
	//	file.addSeparator();
	//
	//	JMenuItem settings = new JMenuItem("Settings");
	//	settings.addActionListener((ActionEvent)->XGSettingsWindow.getWindow().setVisible(true));
	//	file.add(settings);
	//
	//	file.addSeparator();
	//
	//	JMenuItem quit = new JMenuItem("Quit");
	//	quit.addActionListener((ActioEvent)->this.windowClosing(null));
	//	file.add(quit);
	//
	//	bar.add(file);
	//
	//	JMenu midi = new JMenu("Midi");
	//
	//	JMenuItem requestAll = new JMenuItem("Request All");
	//	requestAll.addActionListener((ActionEvent)->new Thread(() -> XGDevice.DEVICE.requestAll(XGMidi.getMidi())).start());
	//	midi.add(requestAll);
	//
	//	JMenuItem transmitAll = new JMenuItem("Transmit All");
	//	transmitAll.addActionListener((ActionEvent)->new Thread(() -> XGDevice.DEVICE.transmitAll(XGMidi.getMidi())).start());
	//	midi.add(transmitAll);
	//
	//	bar.add(midi);
	//
	//	return bar;
	//}

	@Override public JComponent createContent()
	{
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		JPanel sysPane = new JPanel();
		sysPane.setLayout(new BoxLayout(sysPane, BoxLayout.X_AXIS));

		JTabbedPane tabPane = new JTabbedPane();

		for(XGModuleType mt : MODULE_TYPES)
		{	XGIdentifiableSet<XGModule> set = mt.getModules();
			XGButton2 button;
			if(set.size() == 1)
			{	for(XGModule mod : set)//die forEach-Schleife dient lediglich dazu, den ersten Eintrag des Sets zu extrahieren
				{	XGValue v = null;
					for(String infoTag : mt.getInfoTags())//die forEach-Schleife dient lediglich dazu, den ersten Eintrag des Sets zu extrahieren
					{	v = mod.getValues().get(infoTag);
						break;
					}
					button = new XGButton2(mt.getName(), v);
						button.addActionListener((ActionEvent e)->XGEditWindow.getEditWindow(mod).setVisible(true));
						sysPane.add(button);
					break;
				}
			}
			if(set.size() > 1)
			{	XGModuleTable table = new XGModuleTable(mt);
				tabPane.addTab(mt.getName(), new JScrollPane(table));
			}
		}
		content.add(sysPane, NORTH);
		content.add(tabPane, CENTER);
		return content; 
	}

	@Override public void windowClosing(WindowEvent e)
	{	if(JOptionPane.showConfirmDialog(this, "Quit?", "Closing...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		{	this.dispose();
			JXG.quit();
		}
	}

	@Override public void copy()
	{	LOG.info("später");
	}

	@Override public void paste()
	{	LOG.info("später");
	}
}
