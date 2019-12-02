package gui;

import java.awt.Color;

public interface XGFrameable extends XGDisplayable
{

	public default void framize(String text)
	{	this.getGuiComponent().setBackground(Color.green);
	}

}
