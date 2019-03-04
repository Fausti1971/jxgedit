package gui;

import java.awt.Color;
import java.awt.Dimension;

public interface GuiConstants
{	static Color BACK = Color.WHITE, FORE = Color.LIGHT_GRAY;
	static int FONTSIZE = 10;
	static int GAP = 5, SL_W = 160, SL_H = 22, SL_RADI = 10, FONTMIDDLE = SL_H / 2 + FONTSIZE / 2;
	static final Dimension SL_DIM = new Dimension(SL_W, SL_H);
}
