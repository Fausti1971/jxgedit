package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import javax.swing.JTextField;
import file.XGSysexFile;
import value.ObservableValue;

public class XGPathSelector extends JTextField implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************************************/

	ObservableValue<Path> value;

	public XGPathSelector(ObservableValue<Path> v)
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
}
