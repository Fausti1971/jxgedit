package gui;

import java.awt.Dimension;
import javax.swing.JTabbedPane;

public class MultiPartParameterTab extends JTabbedPane
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public MultiPartParameterTab()
	{	addTab("Midi", new MidiTab());
		addTab("Voice", new VoiceTab());
		addTab("Control", new ControlTab());
		addTab("Drums", new DrumTab());
		setMaximumSize(new Dimension(1024,720));
	}
}
