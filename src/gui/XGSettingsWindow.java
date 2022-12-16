package gui;

import device.*;
import static device.XGDevice.DEF_DEVNAME;
import static device.XGMidi.*;
import xml.XGProperty;
import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.*;import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;import java.util.*;

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
	{	super(XGMainWindow.MAINWINDOW, TAG);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setContentPane(this.createContent());
	}

	JComponent createContent()
	{	XGFrame root = new XGFrame(null);
		Rectangle r = new Rectangle(0,0,1,1);

		root.add(new JLabel("MIDI Input:", JLabel.LEADING), r);
		JComboBox<MidiDevice.Info> mi = new JComboBox<>(new Vector<>(INPUTS));
		MidiDevice md = XGMidi.getMidi().getInput();
		if(md != null) mi.setSelectedItem(md.getDeviceInfo());
		mi.addItemListener((ItemEvent e)->XGMidi.getMidi().setInput((MidiDevice.Info)e.getItem()));
		r.x = 1;
		root.add(mi, r);

		r.y++;

		r.x = 0;
		root.add(new JLabel("MIDI Output:", JLabel.LEADING), r);
		JComboBox<MidiDevice.Info> mo = new JComboBox<>(new Vector<>(OUTPUTS));
		md = XGMidi.getMidi().getOutput();
		if(md != null) mo.setSelectedItem(md.getDeviceInfo());
		mo.addItemListener((ItemEvent e)->XGMidi.getMidi().setOutput((MidiDevice.Info)e.getItem()));
		r.x = 1;
		root.add(mo, r);

		r.y++;

		r.x = 0;
		root.add(new JLabel("MIDI Timeout:", JLabel.LEADING), r);
		JSpinner mto = new JSpinner(new SpinnerNumberModel(XGMidi.getMidi().getTimeout(), 10, 1000, 10));
		mto.addChangeListener((ChangeEvent e)->XGMidi.getMidi().setTimeout((Integer)((JSpinner)e.getSource()).getValue()));
		r.x = 1;
		root.add(mto, r);

		r.y++;

		r.x = 0;
		root.add(new JLabel("Device Name:", JLabel.LEADING), r);
		r.x = 1;
		root.add(new XGDeviceDetector(XGDevice.DEVICE.getConfig().getAttributes().getOrNew(ATTR_NAME, new XGProperty(ATTR_NAME, DEF_DEVNAME))), r);

		r.y++;

		r.x = 0;
		root.add(new JLabel("Device ID:", JLabel.LEADING), r);
		JSpinner syx = new JSpinner(new SpinnerNumberModel(XGDevice.DEVICE.getSysexID(), 0, 15, 1));
		syx.addChangeListener((ChangeEvent e)->XGDevice.DEVICE.setSysexID((Integer)((JSpinner)e.getSource()).getValue()));
		r.x = 1;
		root.add(syx, r);

		r.y++;

		r.x = 0;
		root.add(new JLabel("Initial Message:", JLabel.LEADING), r);
		StringBuffer sb = XGDevice.DEVICE.getConfig().getChildNodeOrNew(TAG_INIT_MESSAGE).getTextContent();
		JTextField tf = new JTextField(sb.toString());
		tf.getDocument().addDocumentListener(new DocumentListener()
		{	public void insertUpdate(DocumentEvent event){	sb.replace(0, sb.length(), tf.getText());}
			public void removeUpdate(DocumentEvent event){	sb.replace(0, sb.length(), tf.getText());}
			public void changedUpdate(DocumentEvent event){	sb.replace(0, sb.length(), tf.getText());}
		});
		r.x = 1;
		root.add(tf, r);

		r.y++;

		r.x = 0;
		r.width = 2;
		root.add(new JSeparator(), r);
		r.width = 1;

		r.y++;

		r.x = 0;
		root.add(new JLabel("Look & Feel:", JLabel.LEADING), r);
		JComboBox<String> laf = new JComboBox<>(new Vector<>(XGUI.LOOKANDFEELS.keySet()));
//		laf.setSelectedItem(UIManager.getLookAndFeel().getName());//gibt für "GTK+" immer "GTK Look And Feel" zurücK und wird somit nicht gefunden?!
		laf.setSelectedItem(XGUI.ENVIRONMENT.config.getStringAttribute(ATTR_LOOKANDFEEL));
		laf.addItemListener((ItemEvent e)->XGUI.setLookAndFeel((String)e.getItem()));
		r.x = 1;
		root.add(laf, r);

		r.y++;

		r.x = 0;
		root.add(new JLabel("Fontname:", JLabel.LEADING), r);
		String fontname = XGUI.ENVIRONMENT.config.getStringAttribute(ATTR_FONT_NAME);
		JComboBox<String> fnt = new JComboBox<>(XGUI.FONTS);
		fnt.setSelectedItem(fontname);
		fnt.addItemListener((ItemEvent e)->XGUI.setFont((String)e.getItem()));
		r.x = 1;
		root.add(fnt, r);

		r.y++;

		r.x = 0;
		root.add(new JLabel("Fontsize:", JLabel.LEADING), r);
		int size = XGUI.ENVIRONMENT.config.getIntegerAttribute(ATTR_FONT_SIZE, DEF_FONTSIZE);
		JSpinner sl = new JSpinner(new SpinnerNumberModel(size,8,72,1));
		sl.setValue(size);
		sl.addChangeListener((ChangeEvent e)->XGUI.setFontSize((Integer)((JSpinner)e.getSource()).getValue()));
		r.x = 1;
		root.add(sl, r);

		r.y++;

		r.x = 0;
		root.add(new JLabel("invert Mousewheel", JLabel.LEADING), r);
		JCheckBox cb = new JCheckBox();
		cb.setSelected(XGUI.ENVIRONMENT.mouseWheelInverted);
		cb.addItemListener((ItemEvent ie)->XGUI.setMousewheelInverted(ie.getStateChange() == ItemEvent.SELECTED));
		r.x = 1;
		root.add(cb, r);

		r.y++;

		r.x = 0;
		root.add(new JLabel("Knob Behavior:", JLabel.LEADING), r);
		JComboBox<XGKnob.KnobBehavior> kb = new JComboBox<>(XGKnob.KnobBehavior.values());
		kb.setSelectedItem(XGUI.ENVIRONMENT.knobBehavior);
		kb.addItemListener((ItemEvent e)->XGUI.setKnobBehavior((XGKnob.KnobBehavior)e.getItem()));
		r.x = 1;
		root.add(kb, r);

		return root;
	}

	@Override public String getTitle(){	return XGMainWindow.MAINWINDOW.getTitle() + " - " + TAG;}

	@Override public String getTag(){	return TAG;}
}
