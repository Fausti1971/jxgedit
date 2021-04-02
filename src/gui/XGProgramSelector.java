package gui;

import java.awt.BorderLayout;
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
import static value.XGValueStore.STORE;import xml.XMLNode;

public class XGProgramSelector extends XGFrame implements XGComponent, XGParameterChangeListener, XGValueChangeListener, TreeSelectionListener
{
	private static final long serialVersionUID = 1L;

/*********************************************************************************************/

	private final XGValue value;
	private final XGAddress address;
	private final JButton inc = new JButton("+"), dec = new JButton("-"), select = new JButton();

	public XGProgramSelector(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	super(n);
		this.setLayout(new BorderLayout());
		
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
		this.value = STORE.getFirstIncluded(this.address);
		this.value.addParameterListener(this);
		this.value.addValueListener(this);
		this.setName(this.value.getParameter().getName());

		this.borderize();

		this.addMouseListener(this);
		this.addFocusListener(this);

		this.inc.addActionListener((ActionEvent e)->{this.value.addIndex(1);});
//		this.inc.setPreferredSize(new Dimension(GRID, GRID));
		this.add(this.inc, BorderLayout.EAST);

		this.dec.addActionListener((ActionEvent e)->{this.value.addIndex(-1);});
//		this.dec.setPreferredSize(new Dimension(GRID, GRID));
		this.add(this.dec, BorderLayout.WEST);

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
	}

	private void openDialog()
	{	javax.swing.JDialog d = new javax.swing.JDialog();
		d.setLocationRelativeTo(this);
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
		d.setContentPane(t);
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
