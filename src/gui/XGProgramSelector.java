package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import module.XGModule;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import parm.XGTableEntry;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGProgramSelector extends XGComponent implements XGParameterChangeListener, XGValueChangeListener, XGWindowSource, TreeSelectionListener
{
	private static final long serialVersionUID = 1L;

/*********************************************************************************************/

	private final XGValue value;
	private final XGAddress address;
	private XGWindow window;
	private final JButton inc = new JButton(">"), dec = new JButton("<"), select = new JButton();

	public XGProgramSelector(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{
		super(n, mod);
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
		this.value = mod.getType().getDevice().getValues().getFirstIncluded(this.address);
		this.value.addParameterListener(this);
		this.value.addValueListener(this);
		this.setName(this.value.getParameter().getName());

		this.borderize();

		this.addMouseListener(this);
		this.addFocusListener(this);

		this.inc.addActionListener((ActionEvent e)->{this.value.addIndex(1);});
		this.inc.setPreferredSize(new Dimension(GRID, GRID));
		this.add(this.inc, BorderLayout.EAST);

		this.dec.addActionListener((ActionEvent e)->{this.value.addIndex(-1);});
		this.dec.setPreferredSize(new Dimension(GRID, GRID));
		this.add(this.dec, BorderLayout.WEST);

		this.select.setText(this.value.toString());
		this.select.addActionListener((ActionEvent e)->{this.selectionWindow();});
		this.add(this.select, BorderLayout.CENTER);

		this.parameterChanged(this.value.getParameter());
	}

	private void selectionWindow()
	{	if(this.getChildWindow() == null) new XGWindow(this, (XGWindow)SwingUtilities.windowForComponent(this), true, true, "select " + this.getName());
		else this.getChildWindow().toFront();
	}

	@Override public void contentChanged(XGValue v)
	{	this.select.setText(this.value.toString());
		super.repaint();
	}

	@Override public void parameterChanged(XGParameter p)
	{	this.setName(p.getName());
		this.borderize();
	}

	@Override public XGWindow getChildWindow()
	{	return this.window;
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.window = win;
	}

	@Override public JComponent getChildWindowContent()
	{	JTree t = new JTree(new XGTableTreeModel(this.value.getParameter().getTranslationTable()));

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
		return t;
	}

	@Override public void valueChanged(TreeSelectionEvent e)
	{	TreePath p = e.getNewLeadSelectionPath();
		if(p == null) return;
		Object o = p.getLastPathComponent();
		if(o == null) return;
		if(o instanceof XGTableEntry) this.value.editEntry((XGTableEntry)o);
	}

	@Override public Point childWindowLocationOnScreen()
	{	return this.getLocationOnScreen();
	}
}
