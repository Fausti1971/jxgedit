package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import file.XGSysexFile;
import value.ChangeableContent;

public class XGPathSelector extends JTextField implements DocumentListener, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************************/

	ChangeableContent<Path> value;

	public XGPathSelector(ChangeableContent<Path> v)
	{	super(v.get().toString());
		this.setToolTipText("press enter for fileselector");
		this.value = v;
		this.setAlignmentX(0.5f);
		this.addActionListener(this);
	}

	@Override public void actionPerformed(ActionEvent e)
	{	XGSysexFile f = new XGSysexFile(null, value.get().toString());
		Path p = f.selectPath(f.toString());
		value.set(p);
		setText(p.toString());
		getTopLevelAncestor().revalidate();
	}

	@Override public void insertUpdate(DocumentEvent e)
	{	value.set(Paths.get(getText()));
	}

	@Override public void removeUpdate(DocumentEvent e)
	{	value.set(Paths.get(getText()));
	}

	@Override public void changedUpdate(DocumentEvent e)
	{	value.set(Paths.get(getText()));
	}
}
