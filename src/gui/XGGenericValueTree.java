package gui;

import java.awt.Dimension;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;
import adress.XGAddressableSet;
import value.XGValue;

public class XGGenericValueTree extends XGComponent
{
	private static final long serialVersionUID = 1L;

/*****************************************************************************************/

	private final JTree tree;
//	private final XGAddressableSet<XGValue> set;

	public XGGenericValueTree(XGAddressableSet<XGValue> set)
	{	super("Test");
		this.tree = new JTree();
		this.tree.setModel(new XGValueTreeModel(set));
		this.tree.setRootVisible(false);
		this.tree.setShowsRootHandles(true);
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setPreferredSize(new Dimension(300, 500));
//		this.set = set;

		this.add(this.tree);
	}
}
