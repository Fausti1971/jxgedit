package gui;

import java.awt.*;import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import value.XGValue;

public class XGLabel extends JLabel implements XGUI
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************/

	public XGLabel(String text)
	{	this.setText(text);
		this.setOpaque(false);
		this.setBorder(null);
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.CENTER);
	}
}
