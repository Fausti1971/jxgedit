package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;import java.awt.event.ItemEvent;import java.awt.event.ItemListener;import java.util.Collection;import java.util.Vector;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;import javax.swing.event.PopupMenuEvent;import javax.swing.event.PopupMenuListener;
import config.XGPropertyChangeListener;import device.XGDevice;import xml.XGProperty;import xml.XMLNodeConstants;

public class XGDeviceSelector extends JPanel implements ActionListener, PopupMenuListener
{
/***********************************************************************************************************************************/

	private final JComboBox<String> popup;
	private final XGProperty deviceNameProp;
	private String oldString;

	public XGDeviceSelector()
	{	this.deviceNameProp = XGDevice.DEVICE.getName();
		this.setLayout(new BorderLayout());

		this.popup = new JComboBox<>(new Vector<>(XGDevice.getAvailableDevices()));
		this.popup.setSelectedItem(this.deviceNameProp.getValue().toString());

		this.popup.addPopupMenuListener(this);
		this.add(this.popup, BorderLayout.CENTER);

		JButton button = new JButton("detect");
		this.add(button, BorderLayout.EAST);
		button.addActionListener(this);
	}

	@Override public void actionPerformed(ActionEvent e)
	{	XGDevice.DEVICE.requestInfo();
		this.popup.setSelectedItem(this.deviceNameProp.getValue());
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent event)
	{	this.oldString = this.popup.getSelectedItem().toString();
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent event)
	{	String newString = this.popup.getSelectedItem().toString();
		if(this.oldString.equals(newString)) return;
		int answer = JOptionPane.showConfirmDialog(XGMainWindow.MAINWINDOW, "Switch " + this.oldString + " to " + newString + " will reinitialize the device!");
		if(answer == JOptionPane.OK_OPTION) this.deviceNameProp.setValue(newString);
		else this.popup.setSelectedItem(this.oldString); 
	}

	public void popupMenuCanceled(PopupMenuEvent event)
	{	this.popup.setSelectedItem(this.oldString); 
	}
}
