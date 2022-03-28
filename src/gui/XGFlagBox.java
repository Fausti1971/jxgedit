package gui;

import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.*;
import value.XGValue;

public class XGFlagBox extends XGFrame
{
	private static final long serialVersionUID = 1L;
	private static final Dimension DIM = new Dimension(132, 88);
	private final Set<XGValue> values = new LinkedHashSet<>();

	public XGFlagBox(String name, XGValue... vals)
	{	if(vals == null)
		{	this.setEnabled(false);
			this.setVisible(false);
			return;
		}
//		this.setName(name);

		for(XGValue v : vals){	if(v != null) this.values.add(v);}

		JButton button = new JButton(name);
		button.addActionListener((ActionEvent)->{new XGPopup(this, this.values);});
		button.setAlignmentY(0.5f);
		button.setAlignmentX(0.5f);

		this.add(button, "0,1,1,2");
	}
}