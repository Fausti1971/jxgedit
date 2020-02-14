package gui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public class XGTree extends JTree implements MouseListener, KeyListener, GuiConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=4038224172565298441L;
	private static JButton BUTTON = new JButton();
	private static Border BORDER = new LineBorder(COL_FOCUS, 2, true);

	private static final TreeCellRenderer defaultTreeCellRenderer = new TreeCellRenderer()
	{	@Override public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{	//selected == hasFocus
			XGTreeNode n = (XGTreeNode)value;
			XGTree t = (XGTree)tree;
			BUTTON.setBorder(BORDER);

			if(n == t.getFocussedNode())
			{	BUTTON.setBorderPainted(true);
			}
			else
			{	BUTTON.setBorderPainted(false);
			}

			if(n.isSelected())
			{	BUTTON.setOpaque(true);
				BUTTON.setBackground(GuiConstants.COL_NODESELECTEDBACK);
			}
			else BUTTON.setOpaque(false);

			BUTTON.setText(n.getNodeText());
			return BUTTON;
		}
	};

/*************************************************************************************************************/

	private XGTreeNode focussedNode;

	public XGTree(XGTreeNode root)
	{	super(root);
		this.addMouseListener(this);
		this.addKeyListener(this);
//		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);	//"selected" bedeutet hier "focussed"
		this.setSelectionModel(null);
		this.setToggleClickCount(10);
		this.setCellRenderer(defaultTreeCellRenderer);
		this.setExpandsSelectedPaths(true);
		this.setScrollsOnExpand(true);
		this.setShowsRootHandles(true);
	}

	private void setFocussedNode(XGTreeNode n)
	{	XGTreeNode old = this.focussedNode;
		this.focussedNode = n;
		if(this.focussedNode != null) this.focussedNode.repaintNode();
		if(old != null) old.repaintNode();
	}

	private XGTreeNode getFocussedNode()
	{	return this.focussedNode;
	}

	@Override public void mouseClicked(MouseEvent e)
	{	TreePath p = this.getPathForLocation(e.getX(), e.getY());
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

		if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
		{	if(this.focussedNode != null) this.focussedNode.performDefaultAction();
		}

		if(e.getButton() == MouseEvent.BUTTON3)
		{	this.setComponentPopupMenu(new XGPopup(this, this.focussedNode, e.getLocationOnScreen()));
			this.setComponentPopupMenu(null);	//TODO: ?
			System.out.println("popup: " + this.focussedNode);
		}
	}

	@Override public void mousePressed(MouseEvent e)
	{
	}

	@Override public void mouseReleased(MouseEvent e)
	{
	}

	@Override public void mouseEntered(MouseEvent e)
	{
	}

	@Override public void mouseExited(MouseEvent e)
	{
	}

	@Override public void keyTyped(KeyEvent e)
	{
	}

	@Override public void keyPressed(KeyEvent e)	//zuerst
	{	XGTreeNode n = (XGTreeNode)this.getLastSelectedPathComponent();
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{	System.out.println("key: " + n);
		}
	}

	@Override public void keyReleased(KeyEvent e)
	{
	}
}
