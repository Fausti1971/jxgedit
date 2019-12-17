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
import javax.swing.tree.TreeSelectionModel;

public class XGTree extends JTree implements MouseListener, KeyListener, GuiConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=4038224172565298441L;
	private static JButton BUTTON = new JButton();
	private static Border BORDER = new LineBorder(COL_FOCUS, 1, true);

	private static final TreeCellRenderer defaultTreeCellRenderer = new TreeCellRenderer()
	{	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{	//selected == hasFocus
			XGTreeNode n = (XGTreeNode)value;
			BUTTON.setBorder(BORDER);

			if(hasFocus)
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

			BUTTON.setText(n.toString());
			return BUTTON;
		}
	};

/*************************************************************************************************************/

	public XGTree(XGTreeNode root)
	{	super(root);
		this.addMouseListener(this);
//		this.addTreeSelectionListener(this);
		this.addKeyListener(this);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//		this.setOpaque(true);
//		this.colorize();
		this.setCellRenderer(defaultTreeCellRenderer);
//		this.setSelectionModel(null);
		this.setExpandsSelectedPaths(true);
		this.setScrollsOnExpand(true);
		this.setShowsRootHandles(true);
	}

	public void mouseClicked(MouseEvent e)
	{	XGTreeNode n = (XGTreeNode)this.getLastSelectedPathComponent();
		if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
		{	System.out.println("double: " + n);
//			n.getActions(null).actionPerformed();
		}
		if(e.getButton() == MouseEvent.BUTTON3)
		{	this.setSelectionPath(this.getPathForLocation(e.getX(), e.getY()));
			n = (XGTreeNode)this.getLastSelectedPathComponent();
			System.out.println("popup: " + n);
//			n.getActions(popup()).actionPerformed();
		}
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
	{	XGTreeNode n = (XGTreeNode)this.getLastSelectedPathComponent();
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{	System.out.println("key: " + n);
		}
	}

	public void keyReleased(KeyEvent e)
	{
	}
}
