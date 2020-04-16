package gui;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public interface XGBorderable extends XGComponent
{
	Border roundedLineBorder = BorderFactory.createLineBorder(COL_BORDER, 1, true);

	public default void borderize()
	{	String name = this.getJComponent().getName();
		this.getJComponent().setBorder(new TitledBorder(roundedLineBorder, name, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
	}

	public default void deborderize()
	{	this.getJComponent().setBorder(null);
	}
}
