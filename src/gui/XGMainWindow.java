package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import adress.XGIdentifiableSet;import application.*;
import config.XGPropertyChangeListener;import device.*;
import file.XGDatafile;
import module.*;
import value.XGValue;import xml.*;

public class XGMainWindow extends XGWindow implements XGPropertyChangeListener
{	private static final int GAP = 10;

/***********************************************************************************************************************/

	public XGMainWindow()
	{	super(null, "main");

		XGDevice.DEVICE.getName().getListeners().add(this);
		JXG.CURRENT_CONTENT.getListeners().add(this);

		this.setLayout(new BorderLayout(GAP, GAP));
		this.add(this.createToolbar(), BorderLayout.NORTH);
		this.add(this.createContent(), BorderLayout.CENTER);
		this.add(new XGStatusBar(), BorderLayout.SOUTH);
		this.setVisible(true);
	}

	private XGToolbar createToolbar()
	{	XGToolbar tb = new XGToolbar();
		XGPopup<XMLNode> pop = new XGPopup<>(XGDatafile.CONFIG.getChildNodes(), (XMLNode n)->XGDatafile.load(XGDevice.DEVICE, n.getTextContent().toString()));

		tb.addAction(ACTION_LOADFILE, "Open", "load datadumpfile to memory; right-click for recentlist", (ActionEvent)->XGDatafile.load(XGDevice.DEVICE), pop);
		tb.addAction(ACTION_SAVEFILE, "Save", "save memory to datadumpfile", (ActioEvent)->XGDatafile.save(XGDevice.DEVICE));
		tb.addSeparator();
		tb.addAction(XGUI.ACTION_REQUEST, "Request", "request complete dump from " + XGDevice.DEVICE, (ActionEvent)->new Thread(() -> XGDevice.DEVICE.requestAll(XGMidi.getMidi())).start());
		tb.addAction(XGUI.ACTION_TRANSMIT, "Transmit", "transmit complete memory to " + XGDevice.DEVICE, (ActionEvent)->new Thread(() -> XGDevice.DEVICE.transmitAll(XGMidi.getMidi())).start());
		tb.add(Box.createHorizontalGlue());
		tb.addAction(XGUI.ACTION_CONFIGURE, "Settings", "show the settings-window", (ActionEvent)->XGSettingsWindow.getWindow().setVisible(true));
		tb.addAction(XGUI.ACTION_LOGWIN, "LogWindow", "show the log-window", (ActionEvent)->JXG.LOGWINDOW.setVisible(!JXG.LOGWINDOW.isVisible()));

		return tb;
	}

	@Override public JComponent createContent()
	{
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout(GAP, GAP));

		JPanel sysPane = new JPanel();
		sysPane.setLayout(new BoxLayout(sysPane, BoxLayout.X_AXIS));

		JTabbedPane tabPane = new JTabbedPane();

		for(XGModuleType mt : XGModuleType.MODULE_TYPES)
		{	XGIdentifiableSet<XGModule> set = mt.getModules();
			XGButton2 button;
			if(set.size() == 1)
			{	XGValue v = null;
				XGModule mod = set.iterator().next();
				if(mt.getInfoTags().size() != 0)
				{	String infoTag = mt.getInfoTags().iterator().next();
					v = mod.getValues().get(infoTag);
				}
				button = new XGButton2(mt.getName(), v);
				button.addActionListener((ActionEvent e)->XGEditWindow.getEditWindow(mod).setVisible(true));
				sysPane.add(button);
			}
			if(set.size() > 1)
			{	XGModuleTable table = new XGModuleTable(mt);
				tabPane.addTab(mt.getName(), new JScrollPane(table));
			}
		}
		content.add(sysPane, BorderLayout.NORTH);
		content.add(tabPane, BorderLayout.CENTER);
		return content; 
	}

	@Override public void windowClosing(WindowEvent e)//wird offenbar nur gerufen, wenn über gui fenster geschlossen wird
	{if(JOptionPane.showConfirmDialog(this, "Quit?", "Closing...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		{	this.dispose();
			XGWindow.MAINWINDOW = null;
			JXG.quit();
		}
	}

	@Override public void dispose()
	{	XGDevice.DEVICE.getName().getListeners().remove(this);
		JXG.CURRENT_CONTENT.getListeners().remove(this);
		super.dispose();
	}

	@Override public void propertyChanged(XGProperty p)
	{	this.setTitle(this.getTitle());
	}

	@Override public String getTitle()
	{	return XGDevice.DEVICE.getName().getValue() + " - " + JXG.CURRENT_CONTENT.getValue();
	}
}
