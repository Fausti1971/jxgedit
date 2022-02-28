package gui;

import device.XGDevice;
import static module.XGModuleType.TYPES;
import static msg.XGMessageConstants.*;
import javax.swing.*;

public class XGMasterEditWindow extends XGEditWindow 
{	private static final byte[] MSG = {(byte)SOX, VENDOR, MSG_PC, MODEL, 0, 0, 0x7D, 0, (byte)EOX};

/**********************************************************************************************************/

	public XGMasterEditWindow(module.XGModule mod)
	{	super(XGMainWindow.window, mod);
		this.setContentPane(this.createContent());
		this.pack();
		this.setVisible(true);
	}

	JComponent createContent()
	{	javax.swing.JPanel root = new javax.swing.JPanel();
		XGFrame parms = new XGFrame(null, GRID * 3, GRID * 4);
		XGFrame reset = new XGFrame("Reset", GRID * 12,  GRID * 2);
		root.setLayout(new javax.swing.BoxLayout(root, javax.swing.BoxLayout.Y_AXIS));

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		parms.add(new XGKnob(values.get("master_volume")), "a0a0");
		parms.add(new XGKnob(values.get("master_damp")), "b0b0");
		parms.add(new XGKnob(values.get("master_transpose")), "c0c0");
		parms.add(new XGKnob(values.get("master_tune")), "d0d0");

		root.add(parms);

		char x = 'a';
		int y = 0;
		String c;
		for(module.XGModuleType mt : TYPES)
		{	if(mt instanceof module.XGDrumsetModuleType)
			{	javax.swing.JButton rb = new javax.swing.JButton("Reset " + mt.getName());
				rb.addActionListener((java.awt.event.ActionEvent e)->{this.resetDrumset(mt);});
				c = "" + x + y + x + y++;
				reset.add(rb, c);
			}
		}
		javax.swing.JButton rx = new javax.swing.JButton("XG System On");
		rx.addActionListener((java.awt.event.ActionEvent e)->{XGDevice.device.resetXG(true, true);});
		c = "" + x + y + x + y++;
		reset.add(rx, c);

		javax.swing.JButton ra = new javax.swing.JButton("Reset All Parameters");
		ra.addActionListener((java.awt.event.ActionEvent e)->{XGDevice.device.resetAll(true, true);});
		c = "" + x + y + x + y++;
		reset.add(ra, c);

		root.add(reset);

		return root;
	};

	private void resetDrumset(module.XGModuleType drumset)
	{	try
		{	MSG[6] = 0x7D;
			MSG[7] = (byte)(drumset.getAddress().getHi().getValue() - 0x30);
			new msg.XGMessageParameterChange(null, device.XGMidi.getMidi(), MSG, true).transmit();
			drumset.resetValues();
		}
		catch(adress.InvalidXGAddressException | msg.XGMessengerException | javax.sound.midi.InvalidMidiDataException e)
		{	LOG.severe(e.getMessage());
		}
	}
}
