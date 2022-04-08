package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import device.XGDevice;import xml.XGProperty;

public class XGDeviceDetector extends JPanel implements DocumentListener, ActionListener, XGUI
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/***********************************************************************************************************************************/

	private final XGProperty property;
	private final JTextField text = new JTextField();

	public XGDeviceDetector(XGProperty prop)
	{	this.setLayout(new BorderLayout());
		this.property = prop;
//		this.setAlignmentX(0.5f);
		this.text.setText(this.property.getValue().toString());
		this.text.getDocument().addDocumentListener(this);
		this.add(text, BorderLayout.CENTER);
		JButton button = new JButton("detect");this.add(button, BorderLayout.EAST);
		button.addActionListener(this);
	}

	@Override public void insertUpdate(DocumentEvent e)
	{	this.property.setValue(this.text.getText());
	}

	@Override public void removeUpdate(DocumentEvent e)
	{	this.property.setValue(this.text.getText());
	}

	@Override public void changedUpdate(DocumentEvent e)
	{	this.property.setValue(this.text.getText());
	}

	@Override public void actionPerformed(ActionEvent e)
	{	XGDevice.device.requestInfo();
		this.text.setText(this.property.getValue().toString());
	}
}
