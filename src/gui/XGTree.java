package gui;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class XGTree extends JTree implements TreeSelectionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=4038224172565298441L;

/*************************************************************************************************************/

	public XGTree(XGTreeNode root)
	{	super(root);
		this.addTreeSelectionListener(this);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		this.setExpandsSelectedPaths(true);
		this.setScrollsOnExpand(true);
		this.setShowsRootHandles(true);
	}

	public void valueChanged(TreeSelectionEvent e)
	{	if(!(this.getLastSelectedPathComponent() instanceof XGTreeNode)) return;
		TreePath t = e.getNewLeadSelectionPath();
		XGTreeNode n = (XGTreeNode)this.getLastSelectedPathComponent();
		n.nodeSelected();
		if(n.getWindow() == null) this.removeSelectionPath(t);
	}
}
