package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;import java.util.*;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;
import application.*;
import config.XGConfigurable;import xml.XGProperty;import xml.XMLNode;import static xml.XMLNodeConstants.ATTR_LOOKANDFEEL;import static xml.XMLNodeConstants.TAG_UI;

public interface XGUI extends XGLoggable, XGConfigurable
{
	Color
		COL_TRANSPARENT = new Color(0, 0, 0, 0),
		COL_BAR_FORE = Color.blue,
		COL_BAR_BACK = Color.white,
		COL_SHAPE = new Color(COL_BAR_FORE.getRed(), COL_BAR_FORE.getGreen(), COL_BAR_FORE.getBlue(), 20),
		COL_TEXT = Color.black;

	int SMALL_FONTSIZE = 14, MEDIUM_FONTSIZE = 18;
	Font SMALL_FONT = new Font(Font.decode(null).getName(), Font.PLAIN, SMALL_FONTSIZE);
	Font MEDIUM_FONT = new Font(Font.decode(null).getName(), Font.PLAIN, MEDIUM_FONTSIZE);

	int GRID = SMALL_FONTSIZE * 2,
		DEF_STROKEWIDTH = 4,
		ROUND_RADIUS = 6;

	BasicStroke DEF_ARCSTROKE = new BasicStroke(DEF_STROKEWIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	BasicStroke DEF_STROKE = new BasicStroke(2f);
	BasicStroke DEF_DOTTED_STROKE = new BasicStroke(0.0f, DEF_STROKE.getEndCap(), DEF_STROKE.getLineJoin(), DEF_STROKE.getMiterLimit(), new float[]{1f,2f}, DEF_STROKE.getDashPhase());

	int START_ARC = 225;
	int END_ARC = 315;
	int LENGTH_ARC = -270;

	class Globals
	{	public MouseEvent dragEvent = null;
		public boolean mousePressed = false;
		public XMLNode config;
	}
	Globals VARIABLES = new Globals();

	Map<String, String> LOOKANDFEELS = new HashMap<>();

	RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	GridBagConstraints DEF_GBC = new GridBagConstraints(0, 0, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);

	static void init()
	{	JDialog.setDefaultLookAndFeelDecorated(true);
		VARIABLES.config = JXG.config.getChildNodeOrNew(TAG_UI);
		for(LookAndFeelInfo i : UIManager.getInstalledLookAndFeels())
		{	LOOKANDFEELS.put(i.getName(), i.getClassName());
		}
		XGUI.setLookAndFeel(VARIABLES.config.getStringAttribute(ATTR_LOOKANDFEEL));
	}

	static java.awt.Image loadImage(String name)
	{	try
		{	return javax.imageio.ImageIO.read(XGUI.class.getResource(name));
		}
		catch(java.io.IOException e)
		{	LOG.warning(e.getMessage());
			return null;
		}
	}

	static void setLookAndFeel(String name)
	{
//		String lfcn = LOOKANDFEELS.getOrDefault(name, UIManager.getCrossPlatformLookAndFeelClassName());
		String lfcn = LOOKANDFEELS.getOrDefault(name, UIManager.getSystemLookAndFeelClassName());
		try
		{	UIManager.setLookAndFeel(lfcn);
			VARIABLES.config.setStringAttribute(ATTR_LOOKANDFEEL, name);
			LOG.info(name);
			if(XGMainWindow.window != null) XGMainWindow.window.updateUI();
		}
		catch(UnsupportedLookAndFeelException|IllegalAccessException|InstantiationException|ClassNotFoundException e)
		{	LOG.severe(e.getMessage());
		}
	}

	@Override public default XMLNode getConfig()
	{	return VARIABLES.config;
	}

	@Override default void propertyChanged(XGProperty p)
	{
	}
}
