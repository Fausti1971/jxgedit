package gui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.UIManager;
import application.ConfigurationConstants;

public interface GuiConstants extends ConfigurationConstants
{
	static enum XGControl{frame, knob, slider, envelope};

	static final Color
		COL_NODESELECTEDBACK = UIManager.getColor("Tree.selectionBackground"),
		COL_NODEBACK = UIManager.getColor("Tree.textBackground"),
		COL_NODEFOCUS = UIManager.getColor("Tree.selectionBorderColor"),
		COL_NODETEXT = Color.darkGray,
		COL_NODESELECTEDTEXT = Color.white,

		COL_BORDER = Color.gray;

	static final int
		COL_STEP = 16,
		FONTSIZE = 10,
		GAP = 5,
		SL_W = 160,
		SL_H = 22,
		ROUND_RADIUS = 10,
		FONTMIDDLE = SL_H / 2 + FONTSIZE / 2;

	static final Dimension
		SL_DIM = new Dimension(SL_W, SL_H);
}
