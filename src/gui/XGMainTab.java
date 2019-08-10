package gui;

import javax.swing.JTabbedPane;
import adress.InvalidXGAdressException;
import obj.XGObjectType;

public class XGMainTab extends JTabbedPane
{

	/**
	 * 
	 */
	private static final long serialVersionUID=1L;


	public XGMainTab() throws InvalidXGAdressException
	{	for(XGObjectType t : XGObjectType.getAllObjectTypes())
		{	this.addTab(t.getName(), new XGObjectTypeTab(t));
		}

//		addTab("MultiPart", new MultiPartTab());
//		addTab("A/D-Part", new ADPartTab());
//		addTab("System/FX", new SysFXTab());
//		addTab("Insert FX", new InsFXTab());

		addTab("Configuration", new ConfigTab());


	}

}
