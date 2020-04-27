package gui;

import java.awt.Dimension;

public class XGDimension extends Dimension implements GuiConstants
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XGDimension(int w, int h)
	{	super(w * GRID_W, h * GRID_H);
	}
}
