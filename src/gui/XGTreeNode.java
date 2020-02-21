package gui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * qualifiziert das implementierende Objekt als in einem XGTree darstellbar.
 * @author thomas
 *
 */

public interface XGTreeNode extends TreeNode, XGContext
{
	static final Logger log = Logger.getAnonymousLogger();

/*****************************************************************************************************************/

/**
 * this.tree ist im Normalfall null und nur bei rootNodes gesetzt, deshalb immer this.getRootNode().getTree() aufrufen;
 * @return
 */
	XGTree getTree();
	void setTree(XGTree t);
	void setSelected(boolean s);
	boolean isSelected();
	void nodeFocussed(boolean b);

	default XGTreeNode getRootNode()
	{	return (XGTreeNode)this.getTreePath().getPathComponent(0);
	}

	default void repaintNode()
	{	XGTree t = this.getRootNode().getTree();
		TreePath p = this.getTreePath();
		Rectangle r = t.getPathBounds(p);
		if(r != null) t.repaint(r);	//falls sichtbar
	}

	default void reloadTree()
	{	((DefaultTreeModel) this.getRootNode().getTree().getModel()).reload(this.getParent());
	}

	default public TreePath getTreePath()
	{	List<TreeNode> array = new ArrayList<>();
		TreeNode n = this;
		while(n != null)
		{	array.add(0, n);
			n = n.getParent();
		}
		return new TreePath(array.toArray());
	}

	default String getNodeText()
	{	return this.toString();
	}

	@Override default int getChildCount()
	{	return Collections.list(this.children()).size();
	}

	@Override default public int getIndex(TreeNode node)
	{	return Collections.list(this.children()).indexOf(node);
	}

	@Override default public TreeNode getChildAt(int childIndex)
	{	return Collections.list(this.children()).get(childIndex);
	}

	@Override default public boolean isLeaf()
	{	return this.getChildCount() == 0;
	}
}
