package gui;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class MultiPartTab extends JComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public MultiPartTab()
	{	super.setLayout(new BorderLayout());
		super.add(new JScrollPane(new MultiPartListView()), BorderLayout.WEST);
		super.add(new MultiPartParameterTab(), BorderLayout.CENTER);
	}
}
