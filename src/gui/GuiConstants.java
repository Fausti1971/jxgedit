package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import application.ConfigurationConstants;

public interface GuiConstants extends ConfigurationConstants
{
	static final RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	String ICON_LEAF32 = "XGLogo32.gif", ICON_LEAF24 = "XGLogo24.gif";

	Color
		COL_FOCUS = UIManager.getColor("Focus.color"),
		COL_TRANSPARENT = new Color(0, 0, 0, 0),
		COL_NODE_SELECTED_BACK = Color.lightGray,
		COL_NODE_BACK = UIManager.getColor("Tree.textBackground"),
		COL_NODE_FOCUS = UIManager.getColor("Tree.selectionBorderColor"),
		COL_NODE_TEXT = Color.darkGray,
		COL_NODE_SELECTED_TEXT = Color.white,
		COL_BORDER = Color.lightGray,
		COL_BAR_BACK = SystemColor.scrollbar.brighter(),
		COL_BAR_FORE = UIManager.getColor("Tree.selectionBackground").brighter();

	float FONTSIZE = 10f;

	Font FONT = new JEditorPane().getFont().deriveFont(FONTSIZE);

	int
		GRID = 22,
		COL_STEP = 16,
		GAP = 5,
		DEF_STROKEWIDTH = 4,
		ROUND_RADIUS = 6;

	BasicStroke DEF_ARCSTROKE = new BasicStroke(DEF_STROKEWIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	BasicStroke DEF_STROKE = new BasicStroke(2f);

	int START_ARC = 225;
	int END_ARC = 315;
	int LENGTH_ARC = -270;

	Border defaultLineBorder = BorderFactory.createLineBorder(COL_BORDER, 1, true);
	Border focusLineBorder = BorderFactory.createLineBorder(COL_NODE_FOCUS, 1, true);

}
