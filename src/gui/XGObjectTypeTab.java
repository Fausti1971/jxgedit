package gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import obj.XGObjectConstants;
import obj.XGObjectType;
import parm.XGParameterConstants;

public class XGObjectTypeTab extends JPanel implements XGObjectConstants, XGParameterConstants
{	/**
 	* 
 	*/
	private static final long serialVersionUID=1918924802295469534L;

/*******************************************************************************/

	XGObjectType type;
	XGObjectInstanceTable table;

	public XGObjectTypeTab(XGObjectType t)
	{	this.type = t;
		this.table = new XGObjectInstanceTable(t.getName());

		super.setLayout(new BorderLayout());
		if(t.maxInstanceCount() != 0) super.add(new JScrollPane(this.table) , BorderLayout.WEST);
		super.add(new XGEditorTab(this.type, this.table), BorderLayout.CENTER);
	}


	
}
