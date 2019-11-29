package gui;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class XGTree extends JTree implements TreeSelectionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=4038224172565298441L;

/*************************************************************************************************************/

	public XGTree(XGTreeNode root)
	{	super(root);
		this.addTreeSelectionListener(this);
		this.setCellRenderer(XGTreeNode.getDefaultNodeRenderer());
//		this.setSelectionModel(null);
		this.setExpandsSelectedPaths(true);
		this.setScrollsOnExpand(true);
		this.setShowsRootHandles(true);
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		if(!(this.getLastSelectedPathComponent() instanceof XGTreeNode)) return;
		XGTreeNode n = (XGTreeNode)this.getLastSelectedPathComponent();
		this.clearSelection();	//verhindert das bisweilen fragwürdige Verhalten des TreeSelectionModels
		n.nodeClicked();
	}
}
