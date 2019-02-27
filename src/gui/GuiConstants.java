package gui;

import java.awt.Color;
import java.awt.Dimension;

public interface GuiConstants
{	static Color BACK = Color.WHITE, FORE = Color.LIGHT_GRAY;
	static double FONTSIZE = 10;
	static int GAP = 5, SL_W = 160, SL_H = 22, SP_W = (int)(SL_W + GAP), SP_H = (int)(SL_H + GAP + FONTSIZE), SL_RADI = 10;
	static final Dimension SL_DIM = new Dimension(SL_W, SL_H);
}
