package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import application.XGStrings;
import file.*;import xml.*;

public class XGFileSelector extends JFileChooser implements XGSysexFileConstants, XGUI, DocumentListener, ActionListener, XGStrings
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************/

	private String path;
	private final JTextField text;
	private final JButton button;

	public XGFileSelector(final String path, String title, String button, FileFilter filter)
	{	this.path = path;
		this.text = new JTextField(this.path.toString());
		this.button = new JButton(button);
		this.button.addActionListener(this);
		this.setDialogTitle(title);
		this.setApproveButtonText(button);
		this.setAcceptAllFileFilterUsed(false);
		this.setFileFilter(filter);
	}

	public final int select(Component par)
	{	File f = new File(this.path.toString());
		this.setSelectedFile(f);
		int res = this.showDialog(par, this.getApproveButtonText());
		this.path = this.getSelectedFile().getAbsolutePath();
//		this.path.replace(0, this.path.length(), this.getSelectedFile().getAbsolutePath());
		return res;
	}

	public JComponent small()
	{	JPanel root = new JPanel();
		this.setName(this.getDialogTitle());
		Color c = Color.gray;
		root.setBorder(new TitledBorder(BorderFactory.createLineBorder(c, 1, true), this.getName(), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.getFont(), c));

		root.setLayout(new BorderLayout());
		root.setToolTipText(this.path.toString());
		root.setAlignmentX(0.5f);
//		Dimension dim = new Dimension(GRID * 5, GRID * 2);
//		this.text.setMinimumSize(dim);
//		this.text.setPreferredSize(dim);
		this.text.getDocument().addDocumentListener(this);
		root.add(text, BorderLayout.CENTER);
		root.add(button, BorderLayout.EAST);
		return root;
	}

	public String getPath(){	return this.path;}

	@Override public void insertUpdate(DocumentEvent e){	this.path = this.text.getText();}

	@Override public void removeUpdate(DocumentEvent e){	this.path = this.text.getText();}

	@Override public void changedUpdate(DocumentEvent e){	this.path = this.text.getText();}

	@Override public void actionPerformed(ActionEvent e)
	{	this.select(this.button);
		this.text.setText(this.path.toString());
	}
}
