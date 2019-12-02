package gui;

import java.awt.Color;
import java.awt.Dimension;
import application.ConfigurationConstants;

public interface GuiConstants extends ConfigurationConstants
{
	static Color
		COL_NODESELECTEDBACK = Color.lightGray,
		COL_NODEFOCUSEDBACK = Color.darkGray,
		COL_NODETEXT = Color.darkGray,
		COL_NODESELECTEDTEXT = Color.white,

		COL_BORDER = Color.gray;

	static int
		COL_STEP = 16,
		FONTSIZE = 10,
		GAP = 5,
		SL_W = 160,
		SL_H = 22,
		ROUND_RADIUS = 10,
		FONTMIDDLE = SL_H / 2 + FONTSIZE / 2;

	static final Dimension
		SL_DIM = new Dimension(SL_W, SL_H);

	static String
		TAG_WIN = "window",
		TAG_WINX = "X",
		TAG_WINY = "Y",
		TAG_WINW = "W",
		TAG_WINH = "H",

		TAG_COLOR = "color",
		TAG_BASECOLOR = "base",
		TAG_FOCUSCOLOR = "focus";

}
