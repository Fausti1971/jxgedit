package gui;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import adress.XGAdress;
import obj.XGObjectConstants;
import parm.XGParameterConstants;

public class MultiPartTab extends JComponent implements XGParameterConstants
{	private static XGObjectTableView tv = new XGObjectTableView(new XGAdress(XGObjectConstants.MULTI));

	public static XGObjectTableView getTableView()
	{	return tv;}

	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public MultiPartTab()
	{	
		super.setLayout(new BorderLayout());
//		super.add(new JScrollPane(new MultiPartListView()), BorderLayout.WEST);
		super.add(new JScrollPane(tv), BorderLayout.WEST);
		super.add(new MultiPartParameterTab(), BorderLayout.CENTER);
	}
}
