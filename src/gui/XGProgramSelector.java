package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JTree;
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
	private static final java.awt.Dimension DEF_MINDIM = new java.awt.Dimension(396, 44);
	private static javax.swing.ImageIcon icon_next = new javax.swing.ImageIcon(XGUI.loadImage("arrow_right.png")), icon_prev = new javax.swing.ImageIcon(XGUI.loadImage("arrow_left.png"));
	private static java.awt.Dimension ARROWSIZE = new java.awt.Dimension(icon_next.getIconWidth(), icon_next.getIconHeight());

/*********************************************************************************************/

	private final XGValue value;
	private final JButton next = new JButton(icon_next), prev = new JButton(icon_prev), select = new JButton();

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
		this.value.addParameterListener(this);
		this.value.addValueListener(this);
		this.setName(this.value.getParameter().getName());

		this.borderize();

		this.addMouseListener(this);
		this.addFocusListener(this);

		this.next.addActionListener((ActionEvent e)->{this.value.addIndex(1);});
		this.next.setMinimumSize(ARROWSIZE);
		this.add(this.next, BorderLayout.EAST);

		this.prev.addActionListener((ActionEvent e)->{this.value.addIndex(-1);});
		this.prev.setMinimumSize(ARROWSIZE);
		this.add(this.prev, BorderLayout.WEST);

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

	private void openDialog()
	{	javax.swing.JDialog d = new javax.swing.JDialog();
//		d.setLocationRelativeTo(this);
		d.setLocation(this.getLocationOnScreen());
		d.setModal(true);
		d.setTitle("select " + this.value.getParameter().getName());

		JTree t = new JTree(new XGTableTreeModel(this.value.getParameter().getTranslationTable()));

		XGTableEntry e = this.value.getEntry();
		Object[] o;
		if(e.getCategories().isEmpty()) o = new Object[]{t.getModel().getRoot(), e};
		else o = new Object[]{t.getModel().getRoot(), e.getCategories().iterator().next(), e};
		TreePath p = new TreePath(o);

		t.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		t.setSelectionPath(p);
		t.scrollPathToVisible(p);
		t.setExpandsSelectedPaths(true);
		t.setScrollsOnExpand(true);
		t.addTreeSelectionListener(this);
//		this.inc.addActionListener((ActionEvent)->{t.setSelectionPath(path);};);
		t.setShowsRootHandles(true);
		t.setRootVisible(false);
		d.setContentPane(new javax.swing.JScrollPane(t));
		d.pack();
		d.setMinimumSize(new java.awt.Dimension(this.getWidth(), 400));
		d.setVisible(true);
		d.dispose();
	}

	@Override public void valueChanged(TreeSelectionEvent e)
	{	TreePath p = e.getNewLeadSelectionPath();
		if(p == null) return;
		Object o = p.getLastPathComponent();
		if(o == null) return;
		if(o instanceof XGTableEntry) this.value.editEntry((XGTableEntry)o);
	}
}
