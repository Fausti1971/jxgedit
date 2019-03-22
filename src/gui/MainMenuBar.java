package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.midi.MidiDevice;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import application.MU80;
import file.SysexFile;
import midi.Midi;

public class MainMenuBar extends JMenuBar
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	private static JMenuItem
		load = new JMenuItem("Open SYX..."),
		save = new JMenuItem("Save SYX..."),
		quit = new JMenuItem("Quit"),
		upload = new JMenuItem("Upload"),
		download = new JMenuItem("Download"),
		input = new JMenu("MIDI Input"),
		output = new JMenu("MIDI Output");

/**************************************************************************************************************************/

	public MainMenuBar()
	{	super();
		JMenu file = new JMenu("File");
		load.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	new SysexFile().load(true);
			}
		});
		save.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	System.out.println("not implemented");
			}
		});
		quit.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	System.exit(0);
			}
		});
		file.add(load);
		file.add(save);
		file.add(new JSeparator());
		file.add(quit);

		JMenu midi = new JMenu("MIDI");
		midi.add(upload);
		midi.add(download);

		JMenu setting = new JMenu("Settings");
		setting.add(input);
		for(MidiDevice d : Midi.getInputs())
		{	JCheckBoxMenuItem m = new JCheckBoxMenuItem(d.getDeviceInfo().getName());
			m.addActionListener(new ActionListener()
			{	public void actionPerformed(ActionEvent e)
				{	MU80.device.getMidi().setInput(d);
				}
			});
			input.add(m);
			if(MU80.device.getMidi().getInput() == d) m.setSelected(true);
			else m.setSelected(false);
		}
		setting.add(output);
		for(MidiDevice d : Midi.getOutputs())
		{	JCheckBoxMenuItem m = new JCheckBoxMenuItem(d.getDeviceInfo().getName());
			m.addActionListener(new ActionListener()
			{	public void actionPerformed(ActionEvent e)
				{	MU80.device.getMidi().setOutput(d);
				}
			});
			output.add(m);
			if(MU80.device.getMidi().getOutput() == d) m.setSelected(true);
			else m.setSelected(false);
		}

		super.add(file);
		super.add(midi);
		super.add(setting);
	}
}
