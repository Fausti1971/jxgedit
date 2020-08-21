package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import application.JXG;
import application.XGStrings;
import file.XGSysexFileConstants;

public class XGFileSelector extends JFileChooser implements XGSysexFileConstants, GuiConstants, DocumentListener, ActionListener, XGStrings
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************/

	private final StringBuffer path;
	private final JTextField text;
	private final JButton button;

	public XGFileSelector(final StringBuffer path, String title, String button, FileFilter filter)
	{	if(path == null) this.path = new StringBuffer(JXG.HOMEPATH.toString());
		else this.path = path;
		this.text = new JTextField(this.path.toString());
		this.button = new JButton(button);
		this.button.addActionListener(this);
		this.setDialogTitle(title);
		this.setApproveButtonText(button);
		this.setAcceptAllFileFilterUsed(false);
		this.setFileFilter(filter);
	}

	public final String select(Component par) throws FileNotFoundException
	{	this.setSelectedFile(new File(this.path.toString()));
		int res = this.showDialog(par, this.getApproveButtonText());
		if(res == JFileChooser.APPROVE_OPTION) return this.getSelectedFile().getAbsolutePath();
		throw new FileNotFoundException("fileselection aborted");
	}

	public JComponent small()
	{	XGFrame root = new XGFrame(this.getDialogTitle());
		root.setLayout(new BorderLayout());
		root.setToolTipText(this.path.toString());
		root.setAlignmentX(0.5f);
		Dimension dim = new Dimension(GRID * 5, GRID * 2);
		this.text.setMinimumSize(dim);
		this.text.setPreferredSize(dim);
		this.text.getDocument().addDocumentListener(this);
		root.add(text, BorderLayout.CENTER);
		root.add(button, BorderLayout.EAST);
		return root;
	}

	@Override public void insertUpdate(DocumentEvent e)
	{	this.path.replace(0, this.path.length(), this.text.getText());
	}

	@Override public void removeUpdate(DocumentEvent e)
	{	this.path.replace(0, this.path.length(), this.text.getText());
	}

	@Override public void changedUpdate(DocumentEvent e)
	{	this.path.replace(0, this.path.length(), this.text.getText());
	}

	@Override public void actionPerformed(ActionEvent e)
	{	try
		{	String s = select(this.button);
			this.path.replace(0, this.path.length(), s);
			this.text.setText(this.path.toString());
		}
		catch(FileNotFoundException e1)
		{
		}
	}
}
