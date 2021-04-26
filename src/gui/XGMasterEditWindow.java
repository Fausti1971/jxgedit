package gui;

import static javax.swing.SwingConstants.CENTER;import static module.XGModuleType.TYPES;
import static msg.XGMessageConstants.*;
import static value.XGValueStore.STORE;

public class XGMasterEditWindow extends XGEditWindow 
{	private static final byte[] MSG = {(byte)SOX, VENDOR, MSG_PC, MODEL, 0, 0, 0x7D, 0, (byte)EOX};

/**********************************************************************************************************/

	public XGMasterEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod, mod.toString());
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	private javax.swing.JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel(), parms = new javax.swing.JPanel();
		XGFrame reset = new gui.XGFrame("Reset");
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));
		parms.setLayout(new java.awt.GridLayout());
		reset.setLayout(new javax.swing.BoxLayout(reset, javax.swing.BoxLayout.Y_AXIS));
		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		parms.add(new XGKnob(values.get("master_volume")));
		parms.add(new XGKnob(values.get("master_damp")));
		parms.add(new XGKnob(values.get("master_transpose")));
		parms.add(new XGKnob(values.get("master_tune")));

		root.add(parms);

		for(module.XGModuleType mt : TYPES)
		{	if(mt instanceof module.XGDrumsetModuleType)
			{	javax.swing.JButton rb = new javax.swing.JButton("Reset " + mt.getName());
				rb.addActionListener((java.awt.event.ActionEvent e)->{this.resetDrumset(mt);});
				reset.add(rb);
			}
		}
		device.XGDevice dev = device.XGDevice.getDevice();
		javax.swing.JButton rx = new javax.swing.JButton("XG System On");
		rx.addActionListener((java.awt.event.ActionEvent e)->{dev.resetXG(true, true);});
		reset.add(rx);

		javax.swing.JButton ra = new javax.swing.JButton("Reset All Parameters");
		ra.addActionListener((java.awt.event.ActionEvent e)->{dev.resetAll(true, true);});
		reset.add(ra);

		root.add(reset);

		return root;
	};

	private void resetDrumset(module.XGModuleType drumset)
	{	try
		{	MSG[6] = 0x7D;
			MSG[7] = (byte)(drumset.getAddress().getHi().getValue() - 0x30);
			new msg.XGMessageParameterChange(STORE, device.XGMidi.getMidi(), MSG, true).transmit();
			drumset.resetValues();
		}
		catch(adress.InvalidXGAddressException | msg.XGMessengerException | javax.sound.midi.InvalidMidiDataException e)
		{	LOG.severe(e.getMessage());
		}
	}
}
