package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import device.XGDevice;
import value.ChangeableContent;
import xml.XMLNode;

public class XGDeviceDetector extends JTextField implements XGComponent, DocumentListener, ActionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/***********************************************************************************************************************************/

	private ChangeableContent<String> value;
	private XGDevice device;
	private XMLNode config = new XMLNode("device-detector", null);

	public XGDeviceDetector(String name, ChangeableContent<String> v, XGDevice dev)
	{	super(v.getContent());
		this.setToolTipText("press enter to autodetect device");
		this.value = v;
		this.device = dev;
		this.setAlignmentX(0.5f);
		this.setName(name);
		this.borderize();
		this.setSizes(5, 1);
		this.getDocument().addDocumentListener(this);
		this.addActionListener(this);
	}

	@Override public void insertUpdate(DocumentEvent e)
	{	value.setContent(getText());
	}

	@Override public void removeUpdate(DocumentEvent e)
	{	value.setContent(getText());
	}

	@Override public void changedUpdate(DocumentEvent e)
	{	value.setContent(getText());
	}

	@Override public void actionPerformed(ActionEvent e)
	{	device.requestInfo();
		setText(value.getContent());
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public JComponent getJComponent()
	{	return this;
	}
}
