package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import application.ConfigurationConstants;

public interface GuiConstants extends ConfigurationConstants
{
	static final RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	static final GridBagConstraints GBCONSTRAINTS = new GridBagConstraints();
	Map<String, Integer> GBC = new HashMap<>()
	{	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{	put("RELATIVE", GridBagConstraints.RELATIVE);
			put("REMAINDER", GridBagConstraints.REMAINDER);
			put("RESIZE_NONE", GridBagConstraints.NONE);
			put("RESIZE_BOTH", GridBagConstraints.BOTH);
			put("RESIZE_HORIZONTAL", GridBagConstraints.HORIZONTAL);
			put("RESIZE_VERTICAL", GridBagConstraints.VERTICAL);
			put("CENTER", GridBagConstraints.CENTER);
			put("NORTH", GridBagConstraints.NORTH);
			put("NORTHEAST", GridBagConstraints.NORTHEAST);
			put("EAST", GridBagConstraints.EAST);
			put("SOUTHEAST", GridBagConstraints.SOUTHEAST);
			put("SOUTH", GridBagConstraints.SOUTH);
			put("SOUTHWEST", GridBagConstraints.SOUTHWEST);
			put("WEST", GridBagConstraints.WEST);
			put("NORTHWEST", GridBagConstraints.NORTHWEST);
			put("PAGE_START", GridBagConstraints.PAGE_START);
			put("PAGE_END", GridBagConstraints.PAGE_END);
			put("LINE_START", GridBagConstraints.LINE_START);
			put("LINE_END", GridBagConstraints.LINE_END);
			put("FIRST_LINE_START", GridBagConstraints.FIRST_LINE_START);
			put("FIRST_LINE_END", GridBagConstraints.FIRST_LINE_END);
			put("LAST_LINE_START", GridBagConstraints.LAST_LINE_START);
			put("LAST_LINE_END", GridBagConstraints.LAST_LINE_END);
		}
	};

	Color
		COL_FOCUS = UIManager.getColor("Focus.color"),
		COL_TRANSPARENT = new Color(0, 0, 0, 0),
		COL_NODE_SELECTED_BACK = UIManager.getColor("Tree.selectionBackground"),
		COL_NODE_BACK = UIManager.getColor("Tree.textBackground"),
		COL_NODE_FOCUS = UIManager.getColor("Tree.selectionBorderColor"),
		COL_NODE_TEXT = Color.darkGray,
		COL_NODE_SELECTED_TEXT = Color.white,
		COL_BORDER = Color.lightGray,
		COL_BAR_BACK = SystemColor.scrollbar.brighter(),
		COL_BAR_FORE = COL_NODE_SELECTED_BACK.brighter();

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
