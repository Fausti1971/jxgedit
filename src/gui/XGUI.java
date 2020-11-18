package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;
import application.ConfigurationConstants;
import application.XGLoggable;
import xml.XMLNode;

public interface XGUI extends ConfigurationConstants, XGLoggable
{
	class Globals
	{	public MouseEvent dragEvent = null;
		public Cursor lastCursor = null;
		public boolean mousePressed = false;
	}

	static final Globals VARIABLES = new Globals();

	final RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	final GridBagConstraints DEF_GBC = new GridBagConstraints(0, 0, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);

	static final String
		LAF_SYSTEM = UIManager.getSystemLookAndFeelClassName(),
		LAF_CROSS = UIManager.getCrossPlatformLookAndFeelClassName(),
		LAF_MOTIF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel",
		LAF_NIMBUS = "javax.swing.plaf.nimbus.NimbusLookAndFeel",
		LAF_GTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
		LAF_METAL = "javax.swing.plaf.metal.MetalLookAndFeel";

	static final MetalTheme
		LAF_METAL_DEFAULT = new DefaultMetalTheme(),
		LAF_METAL_OCEAN = new OceanTheme();
//		LAF_METAL_TEST = new TestTheme();

		static void init(XMLNode n)
		{
			LookAndFeelInfo[] array = UIManager.getInstalledLookAndFeels();
			for(LookAndFeelInfo i : array) System.out.println(i.getName());
			try
			{
//				MetalLookAndFeel.setCurrentTheme(LAF_METAL_DEFAULT);
//				UIManager.setLookAndFeel(new MetalLookAndFeel()); 

				UIManager.setLookAndFeel(LAF_SYSTEM);
			}
			catch(UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
			{	LOG.info(e.getMessage());
			}
		}

	String ICON_LEAF32 = "XGLogo32.gif", ICON_LEAF24 = "XGLogo24.gif";

	Color
		COL_TRANSPARENT = new Color(0, 0, 0, 0),
		COL_BAR_FORE = UIManager.getColor("Tree.selectionBackground").brighter(),
		COL_SHAPE = new XGColor(COL_BAR_FORE).add(0, -210);

	float FONTSIZE = 10f;

	Font SMALL_FONT = new JEditorPane().getFont().deriveFont(FONTSIZE);

	int
		GRID = 22,
		COL_STEP = 16,
		GAP = 5,
		DEF_STROKEWIDTH = 4,
		ROUND_RADIUS = 6,
		DEF_DEVCOLOR = 0xF0F0F0;

	BasicStroke DEF_ARCSTROKE = new BasicStroke(DEF_STROKEWIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	BasicStroke DEF_STROKE = new BasicStroke(2f);
	BasicStroke DEF_DOTTED_STROKE = new BasicStroke(0.0f, DEF_STROKE.getEndCap(), DEF_STROKE.getLineJoin(), DEF_STROKE.getMiterLimit(), new float[]{1f,2f}, DEF_STROKE.getDashPhase());

	int START_ARC = 225;
	int END_ARC = 315;
	int LENGTH_ARC = -270;

//	Border defaultLineBorder = BorderFactory.createLineBorder(COL_BORDER, 1, true);
//	Border focusLineBorder = BorderFactory.createLineBorder(COL_NODE_FOCUS, 1, true);

}
