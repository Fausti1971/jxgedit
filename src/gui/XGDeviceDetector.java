package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import device.XGDevice;
import value.ChangeableContent;

public class XGDeviceDetector extends JTextField implements DocumentListener, ActionListener, GuiConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/***********************************************************************************************************************************/

	private ChangeableContent<String> value;
	private XGDevice device;

	public XGDeviceDetector(String name, ChangeableContent<String> v, XGDevice dev)
	{	super(v.getContent());
		this.setToolTipText("press enter to autodetect device");
		this.value = v;
		this.device = dev;
		this.setAlignmentX(0.5f);
		this.setName(name);
		this.setBorder(new TitledBorder(defaultLineBorder, this.getName(), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, FONT, COL_BORDER));
		Dimension dim = new Dimension(GRID * 5, GRID * 2);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
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
}
