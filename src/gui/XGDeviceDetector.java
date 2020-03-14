package gui;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import value.ObservableValue;

public class XGDeviceDetector extends JTextField implements DocumentListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/***********************************************************************************************************************************/

	ObservableValue<String> value;

	public XGDeviceDetector(ObservableValue<String> v)
	{	super(v.get());
		this.setToolTipText("press enter for autodetect");
		this.value = v;
		this.setAlignmentX(0.5f);
		this.getDocument().addDocumentListener(this);

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
}
