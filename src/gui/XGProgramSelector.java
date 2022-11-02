package gui;

import java.awt.*;
import java.awt.event.*;import java.util.Objects;
import javax.swing.*;
import javax.swing.event.DocumentEvent;import javax.swing.event.DocumentListener;import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import table.XGTable;import table.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;

public class XGProgramSelector extends XGFrame implements XGParameterChangeListener, XGValueChangeListener
{
	private static final ImageIcon icon_next = XGUI.loadImage("arrow_right.png");
	private static final ImageIcon icon_prev = XGUI.loadImage("arrow_left.png");

/*********************************************************************************************/

	private final XGValue value;
	private final JButton select = new JButton();
	private XGWindow dialog = null;

	public XGProgramSelector(XGValue val)throws XGComponentException
	{	super("");
		this.value = val;
		if(this.value == null) throw new XGComponentException("value is null");

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

	private class XGProgramSelectorWindow extends XGWindow implements TreeSelectionListener, DocumentListener
	{	final XGProgramSelector selector;
		private final XGTable table;
		private XGTable filteredTable;
		private JTree tree;
		private final JTextField filterBar = new JTextField();

		XGProgramSelectorWindow(XGProgramSelector selector)
		{	super(selector.value.getTag());
			this.selector = selector;
			this.table = this.filteredTable = this.selector.value.getParameter().getTranslationTable();

			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

			this.filterBar.getDocument().addDocumentListener(this);
			this.add(this.filterBar, BorderLayout.NORTH);
			this.add(new JScrollPane(this.createContent()), BorderLayout.CENTER);
//			this.setContentPane(new JScrollPane(this.createContent()));
		}

		JComponent createContent()
		{	XGCategorizedTableTreeModel model = new XGCategorizedTableTreeModel(this.table);
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

		@Override public void insertUpdate(DocumentEvent event)
		{	try
			{	String s = event.getDocument().getText(0, event.getDocument().getLength());
				this.filteredTable = this.filteredTable.filter(s);
				this.tree.setModel(new XGFlattenTableTreeModel(this.filteredTable));
			}
			catch(BadLocationException e)
			{	LOG.info(e.getMessage());
				this.filteredTable = this.table;
				this.tree.setModel(new XGCategorizedTableTreeModel(this.filteredTable));
			}
		}

		@Override public void removeUpdate(DocumentEvent event)
		{	try
			{	String s = event.getDocument().getText(0, event.getDocument().getLength());
				if(s.isEmpty())
				{	this.filteredTable = this.table;
					this.tree.setModel(new XGCategorizedTableTreeModel(this.filteredTable));
				}
				else
				{	this.filteredTable = this.table.filter(s);
					this.tree.setModel(new XGFlattenTableTreeModel(this.filteredTable));
				}
			}
			catch(BadLocationException e)
			{	LOG.info(e.getMessage());
				this.filteredTable = this.table;
				this.tree.setModel(new XGCategorizedTableTreeModel(this.filteredTable));
			}
		}

		@Override public void changedUpdate(DocumentEvent event)
		{	LOG.info(event.getDocument().toString());
		}

		@Override public void copy(){}

		@Override public void paste(){}
	}
}
