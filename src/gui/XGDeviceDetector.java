package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import device.XGDevice;
import value.ChangeableContent;

public class XGDeviceDetector extends JTextField implements DocumentListener, ActionListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/***********************************************************************************************************************************/

	ChangeableContent<String> value;
	XGDevice device;

	public XGDeviceDetector(ChangeableContent<String> v, XGDevice dev)
	{	super(v.get());
		this.setToolTipText("press enter to autodetect device");
		this.value = v;
		this.device = dev;
		this.setAlignmentX(0.5f);
		this.getDocument().addDocumentListener(this);
		this.addActionListener(this);
	}

	@Override public void insertUpdate(DocumentEvent e)
	{	value.set(getText());
	}

	@Override public void removeUpdate(DocumentEvent e)
	{	value.set(getText());
	}

	@Override public void changedUpdate(DocumentEvent e)
	{	value.set(getText());
	}

	@Override public void actionPerformed(ActionEvent e)
	{	device.requestInfo();
		setText(value.get());
	}
}
