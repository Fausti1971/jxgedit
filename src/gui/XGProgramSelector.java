package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import table.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;

public class XGProgramSelector extends XGFrame implements XGParameterChangeListener, XGValueChangeListener
{
	private static final long serialVersionUID = 1L;
	private static final ImageIcon icon_next = new ImageIcon(XGUI.loadImage("arrow_right.png"));
	private static final ImageIcon icon_prev = new ImageIcon(XGUI.loadImage("arrow_left.png"));

/*********************************************************************************************/

	private final XGValue value;
	private final JButton select = new JButton();
	private XGWindow dialog = null;

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

		JButton prev = new JButton(icon_prev);
		prev.setHorizontalTextPosition(JButton.CENTER);
		prev.setVerticalTextPosition(JButton.CENTER);
		prev.addActionListener((ActionEvent e)->this.value.addIndex(-1, true));
		this.add(prev, "0,0,1,2");

		this.select.setText(this.value.toString());
		this.select.setHorizontalTextPosition(JButton.CENTER);
		this.select.setVerticalTextPosition(JButton.CENTER);
		this.select.addActionListener((ActionEvent e)->this.openDialog());
		this.add(this.select, "1,0,6,2");

		JButton next = new JButton(icon_next);
		next.setHorizontalTextPosition(JButton.CENTER);
		next.setVerticalTextPosition(JButton.CENTER);
		next.addActionListener((ActionEvent e)->this.value.addIndex(1, true));
		this.add(next, "7,0,1,2");

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

	private void openDialog()
	{	if(this.dialog == null) this.dialog = new XGProgramSelectorWindow(this);
		this.dialog.setVisible(true);
	}

/************************************************************************************************************************/

	private class XGProgramSelectorWindow extends XGWindow implements TreeSelectionListener
	{	final XGProgramSelector selector;
		private JTree tree;
	
		XGProgramSelectorWindow(XGProgramSelector selector)
		{	super(selector.value.getTag());
			this.selector = selector;
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.setContentPane(new JScrollPane(this.createContent()));
		}

		JComponent createContent()
		{	XGTableTreeModel model = new XGTableTreeModel(this.selector.value.getParameter().getTranslationTable());
			this.tree = new JTree(model);

			this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			this.tree.setExpandsSelectedPaths(true);
			this.tree.setScrollsOnExpand(true);
			this.tree.addTreeSelectionListener(this);
			this.tree.setShowsRootHandles(true);
			this.tree.setRootVisible(false);
			return this.tree;
		}

		private void setTreePath()
		{	XGTableEntry e = this.selector.value.getEntry();
			Object[] o;
			if(e.getCategories().isEmpty()) o = new Object[]{this.tree.getModel().getRoot(), e};
			else o = new Object[]{this.tree.getModel().getRoot(), e.getCategories().iterator().next(), e};
			TreePath p = new TreePath(o);
			this.tree.setSelectionPath(p);
			this.tree.scrollPathToVisible(p);
		}

		@Override public String getTitle(){	return this.selector.value.getParameter().getName();}

		@Override public void valueChanged(TreeSelectionEvent e)
		{	TreePath p = e.getNewLeadSelectionPath();
			if(p == null) return;
			Object o = p.getLastPathComponent();
			if(o instanceof XGTableEntry) this.selector.value.setEntry((XGTableEntry)o, true, true);
		}

		@Override public void windowActivated(WindowEvent e)
		{	super.windowActivated(e);
			this.setLocation(this.selector.getLocationOnScreen());
			this.setTreePath();
		}

		@Override public void windowDeactivated(WindowEvent e)
		{	super.windowDeactivated(e);
			this.setVisible(false);
		}
	}
}
