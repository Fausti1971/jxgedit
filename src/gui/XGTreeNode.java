package gui;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * qualifiziert das implementierende Objekt als in einem Baum darstellbar.
 * @author thomas
 *
 */
public interface XGTreeNode extends TreeNode, XGDisplayable
{	static final Logger log = Logger.getAnonymousLogger();
	
	static final TreeCellRenderer defaultTreeCellRenderer = new TreeCellRenderer()
	{	public Component getTreeCellRendererComponent(JTree tree,Object value,boolean selected,boolean expanded,boolean leaf,int row,boolean hasFocus)
		{	XGDisplayable d = (XGDisplayable)value;
			return d.getGuiComponent();
		}
	};

/*****************************************************************************************************************/

	JTree getTree();
	void setSelected(boolean s);
	boolean isSelected();

	default boolean hasFocus()
	{	return ((XGTree)this.getTree()).getFocus() == this;
	}

	default void repaintNode()
	{	JTree t = this.getTree();
		TreePath p = this.getTreePath();
		Rectangle r = t.getPathBounds(p);
		t.repaint(r);
	}

	default void reloadTree()
	{	((DefaultTreeModel)this.getTree().getModel()).reload(this.getParent());
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

	default XGTreeNode getNextOrFirst()
	{	TreeNode p = this.getParent();
		if(p == null) return this;
		int index = p.getIndex(this);
		int max = p.getChildCount() - 1;
		if(++index > max) index = 0;
		return (XGTreeNode)p.getChildAt(index);
	}

	default XGTreeNode getPreviousOrLast()
	{	TreeNode p = this.getParent();
		if(p == null) return this;
		int index = p.getIndex(this);
		if(--index < 0) index = p.getChildCount() - 1;
		return (XGTreeNode)p.getChildAt(index);
	}

	default int getChildCount()
	{	return Collections.list(this.children()).size();
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
