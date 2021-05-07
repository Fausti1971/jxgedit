package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JPanel;
import adress.InvalidXGAddressException;
import adress.XGMemberNotFoundException;
import module.XGModule;
import xml.XGProperties;
import xml.XMLNode;

public class XGFrame extends JPanel implements XGComponent
{	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

	public XGFrame(String text, int gx, int gy)
	{	this.setLayout(new XGLayout(new Dimension(gx, gy)));
		this.setName(text);
		this.borderize();
	}

	public XGFrame(String text)
	{	this(text, GRID, GRID);
	}
}
