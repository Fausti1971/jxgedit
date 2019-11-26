package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import application.ConfigurationConstants;

/**
 * qualifiziert das implementierende Objekt als in einem Baum darstellbar.
 * @author thomas
 *
 */
public interface XGTreeNode extends TreeNode, ConfigurationConstants, XGWindowSource
{	static final Logger log = Logger.getAnonymousLogger();

	public void nodeSelected();
	public void unselectNode();
	public void selectNode();

	default void windowOpened()
	{	this.selectNode();
	}

	default void windowClosed()
	{	this.unselectNode();
	}

	default void reloadTree(XGTree tree)
	{	((DefaultTreeModel)tree.getModel()).reload();
	}

	default public TreePath getTreePath()
	{	List<TreeNode> array = new ArrayList<>();
		TreeNode n = this;
		while(n != null)
		{	array.add(0, n);
			n = n.getParent();
		}
		return new TreePath(array);
	}

	default public int getIndex(TreeNode node)
	{	return Collections.list(this.children()).indexOf(node);
	}

	default public TreeNode getChildAt(int childIndex)
	{	return Collections.list(this.children()).get(childIndex);
	}

	default public boolean isLeaf()
	{	return this.getChildCount() == 0;
	}
}
