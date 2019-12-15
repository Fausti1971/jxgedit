package gui;

import java.awt.Color;
import javax.swing.border.LineBorder;

public interface XGFrameable extends XGDisplayable
{

	public default void framize(String text)
	{	this.getGuiComponent().setBorder(new LineBorder(Color.blue, 2, true));
	}

	public default void deframize()
	{	this.getGuiComponent().setBorder(null);
	}
}
