package gui;

import javax.swing.JTabbedPane;

public class MainTab extends JTabbedPane
{

	/**
	 * 
	 */
	private static final long serialVersionUID=1L;


	public MainTab()
	{	addTab("MultiPart", new MultiPartTab());
		addTab("A/D-Part", new ADPartTab());
		addTab("System/FX", new SysFXTab());


	}

}
