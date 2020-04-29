package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import file.XGSysexFile;
import value.ChangeableContent;
import xml.XMLNode;

public class XGPathSelector extends JTextField implements XGComponent, DocumentListener, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************************/

	private ChangeableContent<Path> value;
	private XMLNode config = new XMLNode("path-selector", null);

	public XGPathSelector(String name, ChangeableContent<Path> v)
	{	super(v.getContent().toString());
		this.setName(name);//TODO: suboptimal, über String XGComponent.name nachdenken...
		this.setSizes(5, 1);
		this.borderize();
		this.setToolTipText("press enter for fileselector");
		this.value = v;
		this.setAlignmentX(0.5f);
		this.addActionListener(this);
	}

	@Override public void actionPerformed(ActionEvent e)
	{	XGSysexFile f = new XGSysexFile(null, value.getContent().toString());
		Path p = f.selectPath(f.toString());
		value.setContent(p);
		setText(p.toString());
		getTopLevelAncestor().revalidate();
	}

	@Override public void insertUpdate(DocumentEvent e)
	{	value.setContent(Paths.get(getText()));
	}

	@Override public void removeUpdate(DocumentEvent e)
	{	value.setContent(Paths.get(getText()));
	}

	@Override public void changedUpdate(DocumentEvent e)
	{	value.setContent(Paths.get(getText()));
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public JComponent getJComponent()
	{	return this;
	}
}
