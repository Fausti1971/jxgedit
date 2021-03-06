package gui;

import device.*;import static device.XGDeviceConstants.DEF_DEVNAME;import static device.XGMidi.*;import xml.XGProperty;import javax.sound.midi.*;import javax.swing.*;import javax.swing.event.*;
import java.awt.event.*;import java.util.*;

public class XGSettingsWindow extends XGWindow
{
	private static final String TAG = "settings";
	private static XGSettingsWindow WINDOW;

	public static XGSettingsWindow getWindow()
	{	if(WINDOW == null) WINDOW = new XGSettingsWindow();
		return WINDOW;
	}

/**************************************************************************************/

	private XGSettingsWindow()
	{	super(TAG);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setContentPane(this.createContent());
	}

	JComponent createContent()
	{	XGFrame root = new XGFrame(null);

		root.add(new JLabel("MIDI Input:", JLabel.LEADING), "0,0,1,1");
		JComboBox<MidiDevice.Info> mi = new JComboBox<>(new Vector<>(INPUTS));
		MidiDevice md = XGMidi.getMidi().getInput();
		if(md != null) mi.setSelectedItem(md.getDeviceInfo());
		mi.addItemListener((ItemEvent e)->XGMidi.getMidi().setInput((MidiDevice.Info)e.getItem()));
		root.add(mi, "1,0,1,1");

		root.add(new JLabel("MIDI Output:", JLabel.LEADING), "0,1,1,1");
		JComboBox<MidiDevice.Info> mo = new JComboBox<>(new Vector<>(OUTPUTS));
		md = XGMidi.getMidi().getOutput();
		if(md != null) mo.setSelectedItem(md.getDeviceInfo());
		mo.addItemListener((ItemEvent e)->XGMidi.getMidi().setOutput((MidiDevice.Info)e.getItem()));
		root.add(mo, "1,1,1,1");

		root.add(new JLabel("MIDI Timeout:", JLabel.LEADING), "0,2,1,1");
		JSpinner mto = new JSpinner(new SpinnerNumberModel(XGMidi.getMidi().getTimeout(), 10, 1000, 10));
		mto.addChangeListener((ChangeEvent e)->XGMidi.getMidi().setTimeout((Integer)((JSpinner)e.getSource()).getValue()));
		root.add(mto, "1,2,1,1");

		root.add(new JLabel("Device Name:", JLabel.LEADING), "0,3,1,1");
		root.add(new XGDeviceDetector(XGDevice.device.getConfig().getAttributes().getOrNew(ATTR_NAME, new XGProperty(ATTR_NAME, DEF_DEVNAME))), "1,3,1,1");

		root.add(new JLabel("Device ID:", JLabel.LEADING), "0,4,1,1");
		JSpinner syx = new JSpinner(new SpinnerNumberModel(XGDevice.device.getSysexID(), 0, 15, 1));
		syx.addChangeListener((ChangeEvent e)->XGDevice.device.setSysexID((Integer)((JSpinner)e.getSource()).getValue()));
		root.add(syx, "1,4,1,1");

		root.add(new JSeparator(), "0,5,2,1");

		root.add(new JLabel("Look & Feel:", JLabel.LEADING), "0,6,1,1");
		JComboBox<String> laf = new JComboBox<>(new Vector<>(XGUI.LOOKANDFEELS.keySet()));
//		laf.setSelectedItem(UIManager.getLookAndFeel().getName());//gibt f??r "GTK+" immer "GTK Look And Feel" zur??cK und wird somit nicht gefunden?!
		laf.setSelectedItem(XGUI.ENVIRONMENT.config.getStringAttribute(ATTR_LOOKANDFEEL));
		laf.addItemListener((ItemEvent e)->XGUI.setLookAndFeel((String)e.getItem()));
		root.add(laf, "1,6,1,1");

		root.add(new JLabel("Fontname:", JLabel.LEADING), "0,7,1,1");
		String fontname = XGUI.ENVIRONMENT.config.getStringAttribute(ATTR_FONT_NAME);
		JComboBox<String> fnt = new JComboBox<>(XGUI.FONTS);
		fnt.setSelectedItem(fontname);
		fnt.addItemListener((ItemEvent e)->XGUI.setFont((String)e.getItem()));
		root.add(fnt, "1,7,1,1");

		root.add(new JLabel("Fontsize:", JLabel.LEADING), "0,8,1,1");
		int size = XGUI.ENVIRONMENT.config.getIntegerAttribute(ATTR_FONT_SIZE, DEF_FONTSIZE);
		JSpinner sl = new JSpinner(new SpinnerNumberModel(size,8,72,1));
		sl.setValue(size);
		sl.addChangeListener((ChangeEvent e)->XGUI.setFontSize((Integer)((JSpinner)e.getSource()).getValue()));
		root.add(sl, "1,8,1,1");

		root.add(new JLabel("invert Mousewheel", JLabel.LEADING), "0,9,1,1");
		JCheckBox cb = new JCheckBox();
		cb.setSelected(XGUI.ENVIRONMENT.mouseWheelInverted);
		cb.addItemListener((ItemEvent ie)->XGUI.setMousewheelInverted(ie.getStateChange() == ItemEvent.SELECTED));
		root.add(cb, "1,9,1,1");

		root.add(new JLabel("Knob Behavior:", JLabel.LEADING), "0,10,1,1");
		JComboBox<XGKnob.KnobBehavior> kb = new JComboBox<>(XGKnob.KnobBehavior.values());
		kb.setSelectedItem(XGUI.ENVIRONMENT.knobBehavior);
		kb.addItemListener((ItemEvent e)->XGUI.setKnobBehavior((XGKnob.KnobBehavior)e.getItem()));
		root.add(kb, "1,10,1,1");

		return root;
	}

	@Override public String getTitle(){	return XGMainWindow.MAINWINDOW.getTitle() + " - " + TAG;}

	@Override public String getTag(){	return TAG;}
}
