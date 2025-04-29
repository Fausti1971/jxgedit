package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;import java.util.Vector;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;import javax.swing.event.PopupMenuListener;
import application.JXG;import device.XGDevice;import xml.XGProperty;

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
		this.oldString = this.deviceNameProp.getValue().toString();
		this.popup.setSelectedItem(this.oldString);

		this.popup.addPopupMenuListener(this);
		this.add(this.popup, BorderLayout.CENTER);

		JButton button = new JButton("detect");
		this.add(button, BorderLayout.EAST);
		button.addActionListener(this);
	}

	private void confirmQuit(String newString)
	{	if(this.oldString.equals(newString)) return;
		int answer = JOptionPane.showConfirmDialog(XGMainWindow.MAINWINDOW, "Switch " + this.oldString + " to " + newString + " will quit the application!\nYou must restart manually!");
		if(answer == JOptionPane.OK_OPTION)
		{	this.deviceNameProp.setValue(newString);
			JXG.quit();
		}
		else this.popup.setSelectedItem(this.oldString); 
	}

	@Override public void actionPerformed(ActionEvent e)
	{	XGDevice.DEVICE.requestInfo();
		this.confirmQuit(this.deviceNameProp.getValue().toString());
		this.deviceNameProp.setValue(this.oldString);
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent event)
	{	this.oldString = this.popup.getSelectedItem().toString();
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent event)
	{	confirmQuit(this.popup.getSelectedItem().toString());
	}

	public void popupMenuCanceled(PopupMenuEvent event)
	{	this.popup.setSelectedItem(this.oldString); 
	}
}
