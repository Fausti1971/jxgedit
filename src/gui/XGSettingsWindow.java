package gui;

import application.*;import static application.JXG.*;import device.*;import static device.XGDeviceConstants.DEF_DEVNAME;import static device.XGMidi.*;import javax.sound.midi.*;import javax.swing.*;import javax.swing.event.*;
import java.awt.*;import java.awt.event.*;import java.util.*;

public class XGSettingsWindow extends JDialog
{	private static final int DEF_PAD = 5;
	private static final Insets DEF_INSETS = new Insets(5,5,5,5);
	private static XGSettingsWindow WINDOW;

	public static XGSettingsWindow getWindow(String title)
	{	if(WINDOW == null) WINDOW = new XGSettingsWindow();
		WINDOW.setTitle(title);
		return WINDOW;
	}

/**************************************************************************************/

	private XGSettingsWindow()
	{	super(XGMainWindow.window);
		this.setLocationRelativeTo(XGMainWindow.window);
		this.setModal(true);
		this.setContentPane(this.createContent());
		this.pack();
	}

	private Container createContent()
	{	JPanel root = new JPanel();
		root.setLayout(new GridBagLayout());
		GridBagConstraints gbcl = new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH, DEF_INSETS, DEF_PAD, DEF_PAD);
		GridBagConstraints gbcr = new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.EAST, GridBagConstraints.BOTH, DEF_INSETS, DEF_PAD, DEF_PAD);

		root.add(new JLabel("MIDI Input:", JLabel.LEADING), gbcl);
		JComboBox<MidiDevice.Info> mi = new JComboBox<MidiDevice.Info>(new Vector<MidiDevice.Info>(INPUTS));
		MidiDevice md = XGMidi.getMidi().getInput();
		if(md != null) mi.setSelectedItem(md.getDeviceInfo());
		mi.addItemListener((ItemEvent e)->{XGMidi.getMidi().setInput((MidiDevice.Info)e.getItem());});
		root.add(mi, gbcr);

		gbcl.gridy = gbcr.gridy = 1;
		root.add(new JLabel("MIDI Output:", JLabel.LEADING), gbcl);
		JComboBox<MidiDevice.Info> mo = new JComboBox<MidiDevice.Info>(new Vector<MidiDevice.Info>(OUTPUTS));
		md = XGMidi.getMidi().getOutput();
		if(md != null) mo.setSelectedItem(md.getDeviceInfo());
		mo.addItemListener((ItemEvent e)->{XGMidi.getMidi().setOutput((MidiDevice.Info)e.getItem());});
		root.add(mo, gbcr);

		gbcl.gridy = gbcr.gridy = 2;
		root.add(new JLabel("MIDI Timeout:", JLabel.LEADING), gbcl);
		JSlider mto = new JSlider(10, 1000, XGMidi.getMidi().getTimeout());
		mto.setMajorTickSpacing(10);
		mto.setSnapToTicks(true);
		mto.addChangeListener((ChangeEvent e)->{XGMidi.getMidi().setTimeout(mto.getValue());});
		root.add(mto, gbcr);

		gbcl.gridy = gbcr.gridy = 3;
		root.add(new JLabel("Device Name:", JLabel.LEADING), gbcl);
		root.add(new XGDeviceDetector(XGDevice.config.getStringBufferAttributeOrNew(ATTR_NAME, DEF_DEVNAME)), gbcr);

		gbcl.gridy = gbcr.gridy = 4;
		root.add(new JSeparator(), gbcl);

		gbcl.gridy = gbcr.gridy = 5;
		root.add(new JLabel("Look & Feel:", JLabel.LEADING), gbcl);
		JComboBox<String> laf = new JComboBox<String>(new Vector<String>(XGUI.LOOKANDFEELS.keySet()));
		laf.setSelectedItem(UIManager.getLookAndFeel().getName());//TODO: gibt für GTK+ immer Nimbus zurücK?!
		laf.addItemListener((ItemEvent e)->{XGUI.setLookAndFeel((String)e.getItem());});
		root.add(laf, gbcr);


		return root;
	}
}
