package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
//TODO: denke bezüglich Tastaturbedienbarkeit nochmal über die Benutzung des DefaultTreeSelectionModel nach: focus up=previous sibling, down=next sibling, left=parent, enter=expand/open editor
//man muss dem TreeSelctionModel seine Eigenarten abgewöhnen, oder es zum FocusSelectionModel degradieren...
public class XGTree extends JTree implements MouseListener, XGColorable, KeyListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=4038224172565298441L;

/*************************************************************************************************************/

	private XGTreeNode focus;

	public XGTree(XGTreeNode root)
	{	super(root);
	if(root == null) System.out.println("Alarm!");
		this.addMouseListener(this);
//		this.addTreeSelectionListener(this);
		this.addKeyListener(this);
//		this.setOpaque(true);
//		this.colorize();
//		this.setCellRenderer(XGTreeNode.defaultTreeCellRenderer);
//		this.setSelectionModel(null);
		this.setExpandsSelectedPaths(true);
		this.setScrollsOnExpand(true);
		this.setShowsRootHandles(true);
		this.setFocus(root);
	}

	public void mouseClicked(MouseEvent e)
	{	TreePath p = this.getPathForLocation(e.getX(), e.getY());
		if(p == null) return;
		this.setFocus((XGTreeNode)p.getLastPathComponent());
		System.out.println("clicked: " + this.focus);
	}

	public XGTreeNode getFocus()
	{	return this.focus;
	}

	private void setFocus(XGTreeNode n)
	{	XGTreeNode old = this.focus;
		this.focus = n;
		if(this.focus != null) ((XGTreeNodeComponent)this.focus.getGuiComponent()).setStatus();
		if(old != null) ((XGTreeNodeComponent)old.getGuiComponent()).setStatus();
	}

	public JComponent getGuiComponent()
	{	return this;
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public void keyPressed(KeyEvent e)	//zuerst
	{	TreePath p = this.focus.getTreePath();
		switch(e.getKeyCode())
		{	case KeyEvent.VK_UP:		this.setFocus(this.focus.getPreviousOrLast()); break;
			case KeyEvent.VK_DOWN:		this.setFocus(this.focus.getNextOrFirst()); break; 
			case KeyEvent.VK_LEFT:		if(this.focus.getParent() == null) break;
										this.setFocus((XGTreeNode)this.focus.getParent()); break;
			case KeyEvent.VK_RIGHT:		if(this.focus.isLeaf() || !this.isExpanded(p)) break;
										this.setFocus((XGTreeNode)this.focus.getChildAt(0)); break;
			case KeyEvent.VK_ENTER:		if(this.focus.isLeaf())
										{	this.focus.setSelected(true);
											System.out.println("action: " + this.focus);
											break;
										}
										if(this.isCollapsed(p)) this.expandPath(p);
										else this.collapsePath(p); break;
			default:					break;
		}
	}

	public void keyReleased(KeyEvent e)
	{
	}
}
