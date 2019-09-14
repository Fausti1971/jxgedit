package gui;

import java.awt.Component;
import java.awt.GridLayout;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import device.XGMidi;
import device.XGDevice;

public class ConfigTab extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID=-712271878400337625L;

	public ConfigTab()
	{	this.setLayout(new GridLayout(1, 0, 10, 10));

		JList<MidiDevice> inputs = new JList<MidiDevice>(XGMidi.getInputs());
		inputs.setToolTipText("Select MIDI Input");
		inputs.setCellRenderer(new ListCellRenderer<MidiDevice>()
		{	public Component getListCellRendererComponent(JList<? extends MidiDevice> list,MidiDevice value,int index,boolean isSelected,boolean cellHasFocus)
			{	JCheckBox t = new JCheckBox(value.getDeviceInfo().getName());
				t.setSelected(XGDevice.getDevice().getInput().equals(value));
				t.setFocusPainted(cellHasFocus);
				return t;
			}
		});
		inputs.addListSelectionListener(new ListSelectionListener()
		{	public void valueChanged(ListSelectionEvent e)
			{	if(e.getValueIsAdjusting()) return;
				if(e.getSource() instanceof JList)
				{	JList<?> l = (JList<?>)e.getSource();
					if(l.getSelectedValue() instanceof MidiDevice)
					{	try
						{	XGDevice.getDevice().setInput((MidiDevice)l.getSelectedValue());
						}
						catch(MidiUnavailableException e1)
						{	e1.printStackTrace();
						}
					}
				}
			}
		});

		JList<MidiDevice> outputs = new JList<>(XGMidi.getOutputs());
		outputs.setToolTipText("Select MIDI Output");
		outputs.setCellRenderer(new ListCellRenderer<MidiDevice>()
		{	public Component getListCellRendererComponent(JList<? extends MidiDevice> list,MidiDevice value,int index,boolean isSelected,boolean cellHasFocus)
			{	JCheckBox t = new JCheckBox(value.getDeviceInfo().getName());
				t.setSelected(XGDevice.getDevice().getOutput().equals(value));
				t.setFocusPainted(cellHasFocus);
				return t;
			}
		});
		outputs.addListSelectionListener(new ListSelectionListener()
		{	public void valueChanged(ListSelectionEvent e)
			{	if(e.getValueIsAdjusting()) return;
				if(e.getSource() instanceof JList)
				{	JList<?> l = (JList<?>)e.getSource();
					if(l.getSelectedValue() instanceof MidiDevice)
					{	try
						{	XGDevice.getDevice().setOutput((MidiDevice)l.getSelectedValue());
						}
						catch(MidiUnavailableException e1)
						{	e1.printStackTrace();
						}
					}
				}
			}
		});

		this.add(inputs);
		this.add(outputs);
	}
}
