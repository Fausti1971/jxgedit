package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import file.XGSysexFile;
import value.ChangeableContent;

public class XGPathSelector extends JTextField implements DocumentListener, ActionListener, GuiConstants
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************************/

	private ChangeableContent<Path> value;

	public XGPathSelector(String name, ChangeableContent<Path> v)
	{	super(v.getContent().toString());
		this.setName(name);//TODO: suboptimal, über String XGComponent.name nachdenken...
		Dimension dim = new Dimension(5, 1);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setBorder(new TitledBorder(defaultLineBorder, this.getName(), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, FONT, COL_BORDER));
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
}
