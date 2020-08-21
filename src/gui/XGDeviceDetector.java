package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import device.XGDevice;

public class XGDeviceDetector extends XGFrame implements DocumentListener, ActionListener, GuiConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/***********************************************************************************************************************************/

	private StringBuffer value;
	private XGDevice device;
	private JTextField text = new JTextField();
	private JButton button = new JButton("detect");

	public XGDeviceDetector(String title, StringBuffer name, XGDevice dev)
	{	super(name.toString());
		this.setLayout(new BorderLayout());
		this.value = name;
		this.device = dev;
		this.setAlignmentX(0.5f);
		this.setName(title);
		Dimension dim = new Dimension(GRID * 5, GRID * 2);
		this.text.setMinimumSize(dim);
		this.text.setPreferredSize(dim);
		this.text.setText(name.toString());
		this.text.getDocument().addDocumentListener(this);
		this.add(text, BorderLayout.CENTER);
		this.add(button, BorderLayout.EAST);
		this.button.addActionListener(this);
	}

	@Override public void insertUpdate(DocumentEvent e)
	{	this.value.replace(0, this.value.length(), this.text.getText());
	}

	@Override public void removeUpdate(DocumentEvent e)
	{	this.value.replace(0, this.value.length(), this.text.getText());
	}

	@Override public void changedUpdate(DocumentEvent e)
	{	this.value.replace(0, this.value.length(), this.text.getText());
	}

	@Override public void actionPerformed(ActionEvent e)
	{	this.device.requestInfo();
		this.text.setText(value.toString());
	}
}
