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
import static xml.XMLNodeConstants.ATTR_LOOKANDFEEL;

public interface XGUI extends XGLoggable
{
	class Globals
	{	public MouseEvent dragEvent = null;
		public boolean mousePressed = false;
//		public XMLNode config = null;
	}
	Globals VARIABLES = new Globals();

	Map<String, String> LOOKANDFEELS = new HashMap<>();

	RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	GridBagConstraints DEF_GBC = new GridBagConstraints(0, 0, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);

	//String
	//	LAF_SYSTEM = UIManager.getSystemLookAndFeelClassName(),
	//	LAF_CROSS = UIManager.getCrossPlatformLookAndFeelClassName(),
	//	LAF_MOTIF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel",
	//	LAF_NIMBUS = "javax.swing.plaf.nimbus.NimbusLookAndFeel",
	//	LAF_GTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
	//	LAF_METAL = "javax.swing.plaf.metal.MetalLookAndFeel";
	//
	//MetalTheme
	//	LAF_METAL_DEFAULT = new DefaultMetalTheme(),
	//	LAF_METAL_OCEAN = new OceanTheme();
//		LAF_METAL_TEST = new TestTheme();

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
	{	for(String s : LOOKANDFEELS.keySet())
		{	if(s.equals(name))
			{	try
				{	UIManager.setLookAndFeel(LOOKANDFEELS.get(s));
					JXG.config.setStringAttribute(ATTR_LOOKANDFEEL, name);
					LOG.info(name);
					if(XGMainWindow.window != null) XGMainWindow.window.updateUI();
				}
				catch(UnsupportedLookAndFeelException|IllegalAccessException|InstantiationException|ClassNotFoundException e)
				{	LOG.severe(e.getMessage());
				}
			}
		}
//		setLookAndFeel(LOOKANDFEELS.get("System"));
	}

	static void init()
	{	JDialog.setDefaultLookAndFeelDecorated(true);

//		LOOKANDFEELS.put("System", UIManager.getSystemLookAndFeelClassName());
//		LOOKANDFEELS.put("Crossplatform", UIManager.getCrossPlatformLookAndFeelClassName());
		for(LookAndFeelInfo i : UIManager.getInstalledLookAndFeels()) LOOKANDFEELS.put(i.getName(), i.getClassName());

		XGUI.setLookAndFeel(JXG.config.getStringAttribute(ATTR_LOOKANDFEEL));
	}

		
//	String ICON_LEAF32 = "gui/XGLogo32.gif", ICON_LEAF24 = "gui/XGLogo24.gif";

	Color
		COL_TRANSPARENT = new Color(0, 0, 0, 0),
		COL_BAR_FORE = Color.blue,
		COL_BAR_BACK = Color.white,
		COL_SHAPE = new Color(COL_BAR_FORE.getRed(), COL_BAR_FORE.getGreen(), COL_BAR_FORE.getBlue(), 20);

	int SMALL_FONTSIZE = 14, MEDIUM_FONTSIZE = 18, LARGE_FONTSIZE = 24;
	Font SMALL_FONT = new Font(Font.decode(null).getName(), Font.PLAIN, SMALL_FONTSIZE);
	Font MEDIUM_FONT = new Font(Font.decode(null).getName(), Font.PLAIN, MEDIUM_FONTSIZE);
	Font LARGE_FONT = new Font(Font.decode(null).getName(), Font.PLAIN, LARGE_FONTSIZE);

	int GRID = SMALL_FONTSIZE * 2,
		//COL_STEP = 16,
		//GAP = 5,
		DEF_STROKEWIDTH = 4,
		ROUND_RADIUS = 6;

	BasicStroke DEF_ARCSTROKE = new BasicStroke(DEF_STROKEWIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	BasicStroke DEF_STROKE = new BasicStroke(2f);
	BasicStroke DEF_DOTTED_STROKE = new BasicStroke(0.0f, DEF_STROKE.getEndCap(), DEF_STROKE.getLineJoin(), DEF_STROKE.getMiterLimit(), new float[]{1f,2f}, DEF_STROKE.getDashPhase());

	int START_ARC = 225;
	int END_ARC = 315;
	int LENGTH_ARC = -270;

//	Border defaultLineBorder = BorderFactory.createLineBorder(COL_BORDER, 1, true);
//	Border focusLineBorder = BorderFactory.createLineBorder(COL_NODE_FOCUS, 1, true);

}
