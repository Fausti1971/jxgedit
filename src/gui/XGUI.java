package gui;

import java.awt.*;
import java.awt.event.MouseEvent;import java.awt.event.MouseWheelEvent;import java.io.IOException;import java.util.*;
import javax.imageio.ImageIO;import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;import javax.swing.plaf.FontUIResource;
import application.*;
import config.XGConfigurable;import xml.XGProperty;import xml.XMLNode;

public interface XGUI extends XGLoggable, XGConfigurable
{
	int MAX_COL = 255;
	Color
		COL_TRANSPARENT = new Color(0, 0, 0, 0),
		COL_BAR_FORE = Color.blue,
		COL_BAR_FORE_LIGHT = brighter(COL_BAR_FORE, 150),
		COL_BAR_BACK = Color.white,
		COL_BAR_BACK_DARK = darker(COL_BAR_BACK, 20),
		COL_SHAPE = new Color(COL_BAR_FORE.getRed(), COL_BAR_FORE.getGreen(), COL_BAR_FORE.getBlue(), 20),
		COL_BORDER = Color.gray,
		COL_BORDER_TEXT = Color.black;

	static Color brighter(Color col, int gap)
	{	return new Color(Math.min(MAX_COL, col.getRed() + gap), Math.min(MAX_COL, col.getGreen() + gap),Math.min(MAX_COL, col.getBlue() + gap));
	}

	static Color darker(Color col, int gap)
	{	return new Color(Math.max(0, col.getRed() - gap), Math.max(0, col.getGreen() - gap),Math.max(0, col.getBlue() - gap));
	}

//	int SMALL_FONTSIZE = 14, MEDIUM_FONTSIZE = 18;
//	Font SMALL_FONT = new Font(Font.decode(null).getName(), Font.PLAIN, SMALL_FONTSIZE);
//	Font MEDIUM_FONT = new Font(Font.decode(null).getName(), Font.PLAIN, MEDIUM_FONTSIZE);

	int //GRID = SMALL_FONTSIZE * 2,
		DEF_STROKEWIDTH = 4,
		ROUND_RADIUS = 6;

	BasicStroke DEF_ARCSTROKE = new BasicStroke(DEF_STROKEWIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	BasicStroke DEF_STROKE = new BasicStroke(DEF_STROKEWIDTH);
	BasicStroke DEF_DOTTED_STROKE = new BasicStroke(0.0f, DEF_STROKE.getEndCap(), DEF_STROKE.getLineJoin(), DEF_STROKE.getMiterLimit(), new float[]{1f,2f}, DEF_STROKE.getDashPhase());

	int START_ARC = 225;
	int END_ARC = 315;
	int LENGTH_ARC = -270;

	class Environment
	{	public MouseEvent dragEvent = null;
		public boolean mousePressed = false, mouseWheelInverted = false;
		public XGKnob.KnobBehavior knobBehavior = XGKnob.KnobBehavior.HORIZONTAL;
		public XMLNode config;
	}
	Environment ENVIRONMENT = new Environment();

	Map<String, String> LOOKANDFEELS = new HashMap<>();
	String[] FONTS = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	int DEF_FONTSIZE = Toolkit.getDefaultToolkit().getScreenSize().height / 100;
	RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	GridBagConstraints DEF_GBC = new GridBagConstraints(0, 0, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);

	static void init()
	{	JDialog.setDefaultLookAndFeelDecorated(false);
		ENVIRONMENT.config = JXG.config.getChildNodeOrNew(TAG_UI);
		for(LookAndFeelInfo i : UIManager.getInstalledLookAndFeels())
		{	LOOKANDFEELS.put(i.getName(), i.getClassName());
		}
		XGUI.setLookAndFeel(ENVIRONMENT.config.getStringAttribute(ATTR_LOOKANDFEEL));
		XGUI.ENVIRONMENT.mouseWheelInverted = Boolean.parseBoolean(ENVIRONMENT.config.getStringBufferAttributeOrNew(ATTR_MOUSEWHEEL_INVERTED, "false").toString());
		XGUI.ENVIRONMENT.knobBehavior = XGKnob.KnobBehavior.valueOf(ENVIRONMENT.config.getStringBufferAttributeOrNew(ATTR_KNOB_BEHAVIOR, XGKnob.KnobBehavior.HORIZONTAL.name()).toString());
	}

	static Image loadImage(String name)
	{	try
		{	return ImageIO.read(XGUI.class.getResource(name));
		}
		catch(IOException e)
		{	LOG.warning(e.getMessage());
			return null;
		}
	}

	static void setKnobBehavior(XGKnob.KnobBehavior b)
	{	ENVIRONMENT.knobBehavior = b;
		ENVIRONMENT.config.setStringAttribute(ATTR_KNOB_BEHAVIOR, b.name());
	}

	static void setMousewheelInverted(boolean b)
	{	XGUI.ENVIRONMENT.mouseWheelInverted = b;
		XGUI.ENVIRONMENT.config.setStringAttribute(ATTR_MOUSEWHEEL_INVERTED, Boolean.toString(b));
	}

	static int getWheelRotation(MouseWheelEvent e)
	{	int result = e.getWheelRotation();
		if(XGUI.ENVIRONMENT.mouseWheelInverted) result = e.getWheelRotation() * -1;
		e.consume();
		return result;
	}

	static void setFont(String name)
	{	int fontstyle = XGUI.ENVIRONMENT.config.getIntegerAttribute(ATTR_FONT_STYLE, Font.PLAIN);
		int fontsize = XGUI.ENVIRONMENT.config.getIntegerAttribute(ATTR_FONT_SIZE, DEF_FONTSIZE);
		setUIFont(new FontUIResource(name, fontstyle, fontsize));
	}

	static void setFontSize(int size)
	{	String fontname = XGUI.ENVIRONMENT.config.getStringAttribute(ATTR_FONT_NAME);
		int fontstyle = XGUI.ENVIRONMENT.config.getIntegerAttribute(ATTR_FONT_STYLE, Font.PLAIN);
		setUIFont(new FontUIResource(fontname, fontstyle, size));
	}

	static void setUIFont(FontUIResource f)
	{	Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements())
		{	Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
			UIManager.put(key, f);
		}
		ENVIRONMENT.config.setStringAttribute(ATTR_FONT_NAME, f.getFontName());
		ENVIRONMENT.config.setIntegerAttribute(ATTR_FONT_STYLE, f.getStyle());
		ENVIRONMENT.config.setIntegerAttribute(ATTR_FONT_SIZE, f.getSize());
		if(XGMainWindow.MAINWINDOW != null) XGMainWindow.MAINWINDOW.updateUI();
		LOG.info(f.toString());
    } 

	static void setLookAndFeel(String name)
	{
//		String lfcn = LOOKANDFEELS.getOrDefault(name, UIManager.getCrossPlatformLookAndFeelClassName());
		String lfcn = LOOKANDFEELS.getOrDefault(name, UIManager.getSystemLookAndFeelClassName());
		String fontName = ENVIRONMENT.config.getStringAttributeOrDefault(ATTR_FONT_NAME, "Arial");
		int fontStyle = ENVIRONMENT.config.getIntegerAttribute(ATTR_FONT_STYLE, Font.PLAIN);
		int fontSize = ENVIRONMENT.config.getIntegerAttribute(ATTR_FONT_SIZE, DEF_FONTSIZE);
		try
		{	UIManager.setLookAndFeel(lfcn);
			ENVIRONMENT.config.setStringAttribute(ATTR_LOOKANDFEEL, name);
			LOG.info(lfcn);
			XGUI.setUIFont (new FontUIResource(fontName,fontStyle,fontSize));
		}
		catch(UnsupportedLookAndFeelException|IllegalAccessException|InstantiationException|ClassNotFoundException e)
		{	LOG.severe(e.getMessage());
		}
	}

	@Override  default XMLNode getConfig(){	return ENVIRONMENT.config;}

	@Override default void propertyChanged(XGProperty p){	LOG.info(p.toString());}
}
