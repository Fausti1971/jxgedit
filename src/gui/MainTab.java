package gui;

import javax.swing.JTabbedPane;
import adress.InvalidXGAdressException;

public class MainTab extends JTabbedPane
{

	/**
	 * 
	 */
	private static final long serialVersionUID=1L;


	public MainTab() throws InvalidXGAdressException
	{	addTab("MultiPart", new MultiPartTab());
		addTab("A/D-Part", new ADPartTab());
		addTab("System/FX", new SysFXTab());
		addTab("Insert FX", new InsFXTab());


	}

}
