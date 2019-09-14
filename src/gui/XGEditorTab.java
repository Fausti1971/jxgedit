package gui;

import javax.swing.JPanel;
import obj.XGObjectType;

public class XGEditorTab extends JPanel
{	/**
	*
	*/
	private static final long serialVersionUID=1918924802295469534L;

/****************************************************************************/

	private XGObjectType type;
	private XGInstanceSelector selector;

	public XGEditorTab(XGObjectType type, XGInstanceSelector sel)
	{	this.type = type;
		this.selector = sel;
	}
}
