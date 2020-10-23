package gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import application.XGLoggable;

/**
 * qualifiziert das implementierende Objekt als in einem XGTree darstellbar.
 * @author thomas
 *
 */

public interface XGTreeNode extends TreeNode, XGContext, XGLoggable
{

/*****************************************************************************************************************/

/**
 * returniert den XGTree, zu dem diese XGTreeNode gehört;
 * this.tree ist im Normalfall null und nur bei rootNodes gesetzt, deshalb im Regelfall this.getRootNode().getTreeComponent() aufrufen;
 * @return tree
 */
	XGTree getTreeComponent();

/**
 * setzt den übergebenen XGTree in dieser XGTreeNode; wird standardmäßig bei der XGTree-Konstruktion mittels RootNode in derselben gesetzt;
 * @param t
 */
	void setTreeComponent(XGTree t);

/**
 * übergibt den selected-Status an die XGTreeNode, damit diese darauf reagieren kann; Darstellung und Aktualisierung übernimmt weiterhin der XGTree;
 * @param s
 */
	void setSelected(boolean s);

/**
 * returniert den selected-Status der XGTreeNode; erfragt XGTree zur Darstellung
 * @return Status
 */
	boolean isSelected();

/**
 * informiert die XGTreeNode darüber, dass sich der focussed-Status der Node verändert hat und gibt dieser somit die Möglichkeit, darauf zu reagieren;
 * @param b status
 */
	void nodeFocussed(boolean b);

/**
 * erstellt den Text zur Repräsentation im Tree
 * @return text
 */
	String getNodeText();
	Collection<? extends TreeNode> getChildNodes();

	default XGTreeNode getRootNode()
	{	return (XGTreeNode)this.getTreePath().getPathComponent(0);
	}

	default void repaintNode()
	{	XGTree t = this.getRootNode().getTreeComponent();
		((DefaultTreeModel)t.getModel()).nodeChanged(this);
	}

	default void reloadTree()
	{	((DefaultTreeModel) this.getRootNode().getTreeComponent().getModel()).reload(this.getParent());
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

	default Point locationOnScreen()
	{	XGTree t = this.getTreeComponent();
		Point p = t.getLocationOnScreen();
		Rectangle r = t.getPathBounds(this.getTreePath());
		if(r == null) return p;
		p.x += r.x + r.width;
		p.y += r.y + r.height;
		return p;
	}

	@Override public default boolean getAllowsChildren()
	{	return !this.isLeaf();
	}

	@Override public default Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(this.getChildNodes());
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
