package gui;

import java.awt.*;
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

public class XGProgramSelector extends XGFrame implements XGParameterChangeListener, XGValueChangeListener, TreeSelectionListener
{
	private static final long serialVersionUID = 1L;
//	private static final Dimension DIM = new Dimension(132, 44);
	private static final ImageIcon icon_next = new ImageIcon(XGUI.loadImage("arrow_right.png"));
	private static final ImageIcon icon_prev = new ImageIcon(XGUI.loadImage("arrow_left.png"));
//	private static final Dimension ARROWSIZE = new Dimension(icon_next.getIconWidth(), icon_next.getIconHeight());

/*********************************************************************************************/

	private final XGValue value;
	private final JButton select = new JButton();
	private JDialog dialog = null;

	public XGProgramSelector(XGValue val)
	{	super("");
		this.value = val;
		if(val == null)
		{	this.setVisible(false);
			this.setEnabled(false);
			return;
		}
		this.value.getParameterListeners().add(this);
		this.value.getValueListeners().add(this);

		this.addMouseListener(this);
//		this.addFocusListener(this);

		JButton prev = new JButton(icon_prev);
		prev.setHorizontalTextPosition(JButton.CENTER);
		prev.setVerticalTextPosition(JButton.CENTER);
		prev.addActionListener((ActionEvent e)->{this.value.addIndex(-1, true);});
		this.add(prev, "0,0,1,1");

		this.select.setText(this.value.toString());
		this.select.setHorizontalTextPosition(JButton.CENTER);
		this.select.setVerticalTextPosition(JButton.CENTER);
		this.select.addActionListener((ActionEvent e)->{this.openDialog();});
		this.add(this.select, "1,0,6,1");

		JButton next = new JButton(icon_next);
		next.setHorizontalTextPosition(JButton.CENTER);
		next.setVerticalTextPosition(JButton.CENTER);
		next.addActionListener((ActionEvent e)->{this.value.addIndex(1, true);});
		this.add(next, "7,0,1,1");

		this.parameterChanged(this.value.getParameter());
	}

	@Override public void contentChanged(XGValue v)
	{	this.select.setText(this.value.toString());
		super.repaint();
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.setName(p.getName());
		this.repaint();
	}

	private void openDialog()//TODO: vielleicht doch eher ein PopupMenu?
	{	this.dialog = new JDialog();
		this.dialog.setLocation(this.getLocationOnScreen());
		this.dialog.setModal(true);
		this.dialog.setUndecorated(true);
		this.dialog.setTitle("select " + this.value.getParameter().getName());

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
		t.addMouseListener(this);
		t.setShowsRootHandles(true);
		t.setRootVisible(false);
		this.dialog.setContentPane(new JScrollPane(t));
		this.dialog.pack();
		this.dialog.setMinimumSize(new Dimension(this.getWidth(), 400));
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
