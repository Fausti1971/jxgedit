package gui;

import device.*;import static device.XGDeviceConstants.DEF_DEVNAME;import static device.XGMidi.*;import xml.XGProperty;import javax.sound.midi.*;import javax.swing.*;import javax.swing.event.*;import javax.swing.plaf.FontUIResource;
import java.awt.*;import java.awt.event.*;import java.util.*;

public class XGSettingsWindow extends XGWindow
{
	private static final String TAG = "settings";
	private static XGSettingsWindow WINDOW;

	public static XGSettingsWindow getWindow(String title)
	{	if(WINDOW == null) WINDOW = new XGSettingsWindow();
		WINDOW.setTitle(title);
		return WINDOW;
	}

/**************************************************************************************/

	private XGSettingsWindow()
	{	super(XGMainWindow.MAINWINDOW, TAG);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setContentPane(this.createContent());
	}

	JComponent createContent()
	{	XGFrame root = new XGFrame(false);

		root.add(new JLabel("MIDI Input:", JLabel.LEADING), "0,0,1,1");
		JComboBox<MidiDevice.Info> mi = new JComboBox<MidiDevice.Info>(new Vector<MidiDevice.Info>(INPUTS));
		MidiDevice md = XGMidi.getMidi().getInput();
		if(md != null) mi.setSelectedItem(md.getDeviceInfo());
		mi.addItemListener((ItemEvent e)->{XGMidi.getMidi().setInput((MidiDevice.Info)e.getItem());});
		root.add(mi, "1,0,1,1");

		root.add(new JLabel("MIDI Output:", JLabel.LEADING), "0,1,1,1");
		JComboBox<MidiDevice.Info> mo = new JComboBox<MidiDevice.Info>(new Vector<MidiDevice.Info>(OUTPUTS));
		md = XGMidi.getMidi().getOutput();
		if(md != null) mo.setSelectedItem(md.getDeviceInfo());
		mo.addItemListener((ItemEvent e)->{XGMidi.getMidi().setOutput((MidiDevice.Info)e.getItem());});
		root.add(mo, "1,1,1,1");

		root.add(new JLabel("MIDI Timeout:", JLabel.LEADING), "0,2,1,1");
		JSpinner mto = new JSpinner(new SpinnerNumberModel(XGMidi.getMidi().getTimeout(), 10, 1000, 10));
		mto.addChangeListener((ChangeEvent e)->{XGMidi.getMidi().setTimeout((Integer)((JSpinner)e.getSource()).getValue());});
		root.add(mto, "1,2,1,1");

		root.add(new JLabel("Device Name:", JLabel.LEADING), "0,3,1,1");
		root.add(new XGDeviceDetector(XGDevice.device.getConfig().getAttributes().getOrNew(ATTR_NAME, new XGProperty(ATTR_NAME, DEF_DEVNAME))), "1,3,1,1");

		root.add(new JSeparator(), "0,4,2,1");

		root.add(new JLabel("Look & Feel:", JLabel.LEADING), "0,5,1,1");
		JComboBox<String> laf = new JComboBox<>(new Vector<>(XGUI.LOOKANDFEELS.keySet()));
//		laf.setSelectedItem(UIManager.getLookAndFeel().getName());//gibt für "GTK+" immer "GTK Look And Feel" zurücK und wird somit nicht gefunden?!
		laf.setSelectedItem(XGUI.ENVIRONMENT.config.getStringAttribute(ATTR_LOOKANDFEEL));
		laf.addItemListener((ItemEvent e)->{XGUI.setLookAndFeel((String)e.getItem());});
		root.add(laf, "1,5,1,1");

		root.add(new JLabel("Fontname:", JLabel.LEADING), "0,6,1,1");
		String fontname = XGUI.ENVIRONMENT.config.getStringAttribute(ATTR_FONT_NAME);
		JComboBox<String> fnt = new JComboBox<>(XGUI.FONTS);
		fnt.setSelectedItem(fontname);
		fnt.addItemListener((ItemEvent e)->{XGUI.setFont((String)e.getItem());});
		root.add(fnt, "1,6,1,1");

		root.add(new JLabel("Fontsize:", JLabel.LEADING), "0,7,1,1");
		int size = XGUI.ENVIRONMENT.config.getIntegerAttribute(ATTR_FONT_SIZE, DEF_FONTSIZE);
		JSpinner sl = new JSpinner(new SpinnerNumberModel(size,8,72,1));
		sl.setValue(size);
		sl.addChangeListener((ChangeEvent e)->{	XGUI.setFontSize((Integer)((JSpinner)e.getSource()).getValue());});
		root.add(sl, "1,7,1,1");

		return root;
	}

	public String getTag()
	{	return TAG;
	}
}
