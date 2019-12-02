package gui;

import java.awt.Component;
import java.awt.event.MouseListener;
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
		{	XGTreeNode n = (XGTreeNode)value;
			return n.getGuiComponent();
		}
	};
	static TreeCellRenderer getDefaultNodeRenderer()
	{	return defaultTreeCellRenderer;
	}

/*****************************************************************************************************************/

	void nodeClicked();
	JTree getTree();

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
		return new TreePath(array);
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
