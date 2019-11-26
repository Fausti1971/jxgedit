package gui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import application.ConfigurationConstants;

public interface GuiConstants extends ConfigurationConstants
{	static Color BACK = Color.WHITE, FORE = Color.LIGHT_GRAY;
	static int FONTSIZE = 10;
	static int GAP = 5, SL_W = 160, SL_H = 22, SL_RADI = 10, FONTMIDDLE = SL_H / 2 + FONTSIZE / 2;
	static final Dimension SL_DIM = new Dimension(SL_W, SL_H);

	static String
	TAG_WIN = "window",
	TAG_WINX = "X",
	TAG_WINY = "Y",
	TAG_WINW = "W",
	TAG_WINH = "H",

	TAG_NAME = "name";

	public default Border getDefaultBorder(String text)
	{	return BorderFactory.createTitledBorder(text);
	}
}
