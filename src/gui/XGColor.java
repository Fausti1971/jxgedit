package gui;

import java.awt.Color;

public class XGColor extends Color
{
	private static final long serialVersionUID = 1L;
	private static final int COL_MAX = 255, COL_MIN = 0;

	public XGColor(Color col)
	{	this(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
	}

	public XGColor(int r, int g, int b, int a)
	{	super(r, g, b, a);
	}

	public XGColor(int i)
	{	super(i);
	}

	public XGColor add(int i, int a)
	{	
		int red = Math.max(COL_MIN, Math.min(this.getRed() + i, COL_MAX));
		int green = Math.max(COL_MIN, Math.min(this.getGreen() + i, COL_MAX));
		int blue = Math.max(COL_MIN, Math.min(this.getBlue() + i, COL_MAX));
		int alpha = Math.max(COL_MIN, Math.min(this.getAlpha() + a, COL_MAX));
		return new XGColor(red, green, blue, alpha);
	}
}
