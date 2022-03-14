package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;

public class XGProgramSelector extends javax.swing.JPanel implements XGComponent, XGParameterChangeListener, XGValueChangeListener, TreeSelectionListener
{
	private static final long serialVersionUID = 1L;
	private static final java.awt.Dimension DEF_MINDIM = new java.awt.Dimension(GRID * 18, GRID * 2);
	private static javax.swing.ImageIcon icon_next = new javax.swing.ImageIcon(XGUI.loadImage("arrow_right.png")), icon_prev = new javax.swing.ImageIcon(XGUI.loadImage("arrow_left.png"));
	private static java.awt.Dimension ARROWSIZE = new java.awt.Dimension(icon_next.getIconWidth(), icon_next.getIconHeight());

/*********************************************************************************************/

	private final XGValue value;
	private final JButton next = new JButton(icon_next), prev = new JButton(icon_prev), select = new JButton();
	private JDialog dialog = null;

	public XGProgramSelector(XGValue val)
	{	this.setLayout(new BorderLayout());
		this.setPreferredSize(DEF_MINDIM);
		this.setMinimumSize(DEF_MINDIM);

		this.value = val;
		if(val == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			return;
		}
		this.value.getParameterListeners().add(this);
		this.value.getValueListeners().add(this);
		this.setName(this.value.getParameter().getName());

		this.borderize();

		this.addMouseListener(this);
		this.addFocusListener(this);

		this.next.addActionListener((ActionEvent e)->{this.value.addIndex(1, true);});
		this.next.setMinimumSize(ARROWSIZE);
		this.add(this.next, BorderLayout.EAST);

		this.prev.addActionListener((ActionEvent e)->{this.value.addIndex(-1, true);});
		this.prev.setMinimumSize(ARROWSIZE);
		this.add(this.prev, BorderLayout.WEST);

		this.select.setFont(MEDIUM_FONT);
		this.select.setText(this.value.toString());
		this.select.addActionListener((ActionEvent e)->{this.openDialog();});
		this.add(this.select, BorderLayout.CENTER);

		this.parameterChanged(this.value.getParameter());
	}

	@Override public void contentChanged(XGValue v)
	{	this.select.setText(this.value.toString());
		super.repaint();
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.setName(p.getName());
		this.borderize();
		this.repaint();
	}

	private void openDialog()//TODO: vielleicht doch eher ein PopupMenu?
	{	this.dialog = new javax.swing.JDialog();
//		d.setLocationRelativeTo(this);
		this.dialog.setLocation(this.getLocationOnScreen());
		this.dialog.setModal(true);
		this.dialog.setTitle("select " + this.value.getParameter().getName());

		JTree t = new JTree(new XGTableTreeModel(this.value.getParameter().getTranslationTable()));

		XGTableEntry e = this.value.getEntry();
		Object[] o;
		if(e.getCategories().isEmpty()) o = new Object[]{t.getModel().getRoot(), e};
		else o = new Object[]{t.getModel().getRoot(), e.getCategories().iterator().next(), e};
		TreePath p = new TreePath(o);

		t.setFont(MEDIUM_FONT);
		t.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		t.setSelectionPath(p);
		t.scrollPathToVisible(p);
		t.setExpandsSelectedPaths(true);
		t.setScrollsOnExpand(true);
		t.addTreeSelectionListener(this);
		t.addMouseListener(this);
		t.setShowsRootHandles(true);
		t.setRootVisible(false);
		this.dialog.setContentPane(new javax.swing.JScrollPane(t));
		this.dialog.pack();
		this.dialog.setMinimumSize(new java.awt.Dimension(this.getWidth(), 400));
		this.dialog.setVisible(true);
		this.dialog.dispose();
	}

	@Override public void mouseClicked(MouseEvent e)
	{	if(e.getClickCount() == 2 && this.dialog.isVisible())
		{	this.dialog.dispose();
			e.consume();
		}
	}

	@Override public void valueChanged(TreeSelectionEvent e)
	{	TreePath p = e.getNewLeadSelectionPath();
		if(p == null) return;
		Object o = p.getLastPathComponent();
		if(o == null) return;
		if(o instanceof XGTableEntry) this.value.setEntry((XGTableEntry)o, true, true);
	}
}
