package gui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public interface XGBorderable extends XGComponent
{
	Border roundedLineBorder = BorderFactory.createLineBorder(Color.lightGray, 1, true);

	public default void borderize()
	{	this.getJComponent().setBorder(new TitledBorder(roundedLineBorder, this.getJComponent().getName()));
	}

	public default void deborderize()
	{	this.getJComponent().setBorder(null);
	}
}
