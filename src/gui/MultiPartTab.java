package gui;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import obj.XGAdress;
import obj.XGObject;
import obj.XGObjectConstants;

public class MultiPartTab extends JComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public MultiPartTab()
	{	super.setLayout(new BorderLayout());
		super.add(new JScrollPane(new MultiPartListView()), BorderLayout.WEST);
//		super.add(new JScrollPane(new XGObjectTableView(XGObject.getXGObjectInstances(new XGAdress(XGObjectConstants.MULTI, 0, 0)))), BorderLayout.WEST);
		super.add(new MultiPartParameterTab(), BorderLayout.CENTER);
	}
}
