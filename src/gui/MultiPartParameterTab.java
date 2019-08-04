package gui;

import java.awt.Dimension;
import javax.swing.JTabbedPane;

public class MultiPartParameterTab extends JTabbedPane
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public MultiPartParameterTab()
	{	addTab("Midi", new MultiPartMidiTab());
		addTab("Voice", new MultiPartVoiceTab());
		addTab("Control", new MultiPartControlTab());
		addTab("Drums", new MultiPartDrumTab());
		setMaximumSize(new Dimension(1024,720));
	}
}
