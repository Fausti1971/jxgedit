package gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public class XGTree extends JTree implements MouseListener, KeyListener, GuiConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=4038224172565298441L;

	private static final DefaultTreeCellRenderer DTCR = new DefaultTreeCellRenderer();
	static
	{	DTCR.setBackgroundSelectionColor(COL_NODE_SELECTED_BACK);
	}
	private static final TreeCellRenderer TCR = new TreeCellRenderer()
	{	@Override public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{	XGTree t = (XGTree)tree;
			XGTreeNode n = (XGTreeNode)value;
			XGTreeNode f = t.getFocussedNode();
			hasFocus = f != null && f.equals(n);
			return DTCR.getTreeCellRendererComponent(tree, n.getNodeText(), n.isSelected(), expanded, leaf, row, hasFocus);
		}
	};

/*************************************************************************************************************/

	private XGTreeNode focussedNode;
/**
 * Im Konstruktor muss der XGTree (mittels root.setTree(this)) in der root-node abgelegt werden.
 * @param root
 */
	public XGTree(XGTreeNode root, boolean showRoot)
	{
		super(root);
		root.setTreeComponent(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.setToggleClickCount(10);
		this.setCellRenderer(TCR);
		this.setExpandsSelectedPaths(true);
		this.setScrollsOnExpand(true);
		this.setRootVisible(showRoot);
		this.setShowsRootHandles(true);
		DTCR.setLeafIcon(new ImageIcon(this.getClass().getResource(ICON_LEAF24)));
	}

//	public XGTree(XGTableTreeModel table)
//	{	super(table);
//	}

	private void setFocussedNode(XGTreeNode n)
	{	if(this.focussedNode != null)
		{	this.focussedNode.nodeFocussed(false);
			this.focussedNode.repaintNode();
		}
		this.focussedNode = n;
		if(this.focussedNode != null)
		{	this.focussedNode.nodeFocussed(true);
			this.focussedNode.repaintNode();
		}
	}

	XGTreeNode getFocussedNode()
	{	return this.focussedNode;
	}

	@Override public void mouseClicked(MouseEvent e)
	{
		TreePath p = this.getPathForLocation(e.getX(), e.getY());
		if(p == null)
		{	this.setFocussedNode(null);
			return;
		}
		Object c = p.getLastPathComponent();

		if(!(c instanceof XGTreeNode))
		{	this.setFocussedNode(null);
			return;
		}
		this.setFocussedNode((XGTreeNode)c);
		if(e.getButton() == MouseEvent.BUTTON3 && this.focussedNode != null && !this.focussedNode.getContexts().isEmpty()) //e.isPopupTrigger() funktioniert manchmal nicht
		{	new XGPopup(this, this.focussedNode, e.getLocationOnScreen());
		}
	}

	@Override public void mousePressed(MouseEvent e)
	{	//if(e.isPopupTrigger()) this.mouseClicked(e);
	}

	@Override public void mouseReleased(MouseEvent e)
	{	//if(e.isPopupTrigger()) this.mouseClicked(e);
	}

	@Override public void mouseEntered(MouseEvent e)
	{
	}

	@Override public void mouseExited(MouseEvent e)
	{
	}

	@Override public void keyTyped(KeyEvent e)
	{	if(!(e.getExtendedKeyCode() == KeyEvent.VK_ENTER)) return;
		if(this.focussedNode == null || this.focussedNode.getContexts().isEmpty()) return;
		this.focussedNode.actionPerformed(new ActionEvent(this, 0, this.focussedNode.getContexts().iterator().next()));
//		System.out.println("nothing to do...");
	}

	@Override public void keyPressed(KeyEvent e)	//zuerst
	{	//if(this.focussedNode != null && e.getKeyCode() == KeyEvent.VK_ENTER) System.out.println("nothing to do...");
	}

	@Override public void keyReleased(KeyEvent e)
	{
	}
/*
	@Override public void valueChanged(TreeSelectionEvent e)	//Kompromiss zur Tastatursteuerung, doppelt aber MouseClicked
	{
		TreePath p = e.getNewLeadSelectionPath();
		if(p == null)
		{	setFocussedNode(null);
			return;
		}
		
		Object c = p.getLastPathComponent();

		if(!(c instanceof XGTreeNode))
		{	this.setFocussedNode(null);
			return;
		}
		this.setFocussedNode((XGTreeNode)c);
	}
*/
}
