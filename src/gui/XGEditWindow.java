package gui;

import adress.XGIdentifiable;import config.XGConfigurable;import device.XGDevice;import module.XGDrumsetModuleType;import module.XGModule;import module.XGModuleType;import static module.XGModuleType.MODULE_TYPES;import tag.XGTagable;import tag.XGTagableIdentifiableSet;import xml.XGProperty;import xml.XMLNode;import javax.swing.*;import java.awt.event.ActionEvent;import java.io.IOException;import java.util.HashMap;import java.util.Map;

public class XGEditWindow extends XGWindow implements XGTagable, XGIdentifiable, XGConfigurable
{
	static final XGTagableIdentifiableSet<XGEditWindow> EDITWINDOWS = new XGTagableIdentifiableSet<>();
	static final Map<String, XMLNode> TEMPLATES = new HashMap<>();

	static public void init()
	{	try
		{	XMLNode xml = XMLNode.parse(XML_TEMPLATES);
			for(XMLNode n : xml.getChildNodes(TAG_TEMPLATE))
			{	String tag = n.getStringAttribute(ATTR_ID);
				TEMPLATES.put(tag, n);
				LOG.info(tag);
			}
		}
		catch(IOException e)
		{	LOG.severe(e.getMessage());
		}
	}

	public static XGEditWindow getEditWindow(XGModule mod)
	{	XGEditWindow win = EDITWINDOWS.get(mod.getTag(), mod.getID());
		if(win != null) return win;
		else
		{	win = new XGEditWindow(mod);
			EDITWINDOWS.add(win);
		}

		//{	switch(mod.getTag())
		//	{	case "rev":		win = new XGReverbEditWindow(mod); break;
		//		case "cho":		win = new XGChorusEditWindow(mod); break;
		//		case "var":		win = new XGVariationEditWindow(mod); break;
		//		case "ins":		win = new XGInsertionEditWindow(mod); break;
		//		case "sys":		win = new XGSystemEditWindow(mod); break;
		//		case "eq":		win = new XGEQEditWindow(mod); break;
		//		case "mp":		win = new XGMultipartEditWindow(mod); break;
		//		case "ad":		win = new XGADPartEditWindow(mod); break;
		//		case "ds1":
		//		case "ds2":
		//		case "ds3":
		//		case "ds4":
		//		case "ds5":
		//		case "ds6":
		//		case "ds7":
		//		case "ds8":
		//		case "ds9":
		//		case "ds10":
		//		case "ds11":
		//		case "ds12":
		//		case "ds13":
		//		case "ds14":
		//		case "ds15":
		//		case "ds16":	win = new XGDrumEditWindow(mod); break;
		//		default:		return null;
		//	}
		return win;
	}

/***********************************************************************************************************/

	final XGModule module;

	public XGEditWindow(XGModule mod)
	{	super(mod.getType().getTag());
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.module = mod;
		this.setContentPane(this.createContent());
	}

	@Override JComponent createContent()
	{	XMLNode xml = TEMPLATES.get(this.module.getTag());
		if(xml == null) return new XGFrame("missing xml for id: " + this.module.getTag());
		return this.createItems(xml);
	}

	private JComponent createItems(XMLNode item)
	{	JComponent component;
		String type = item.getStringAttribute(ATTR_TYPE);
		if(TAG_TEMPLATE.equals(item.getTag())) type = ATTR_FRAME;
		if(type == null) return new XGFrame("missing type for " + item);
		switch(type)
		{	case ATTR_FRAME:
				component = new XGFrame(item);
				break;
			case ATTR_KNOB:
				component = new XGKnob(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG)));
				break;
			case ATTR_RESET_BUTTONS:
				component = createResetButtons(item);
				break;
			case ATTR_PROG_SELECT:
				component = new XGProgramSelector(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG)));
				break;
			case ATTR_COMBO:
				component = new XGCombo(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG)));
				break;
			case ATTR_RADIO:
				component = new XGRadio(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG)), XGComponent.XGOrientation.valueOf(item.getStringAttribute(ATTR_ORIENTATION)));
				break;
			default:
				return new XGFrame("unknown item type: " + type);
		}
		//...
		for(XMLNode i : item.getChildNodes(TAG_ITEM))
		{	String c = i.getStringAttribute(ATTR_CONSTRAINT);
			component.add(this.createItems(i), c);
		}
		return component;
	}

	private JComponent createResetButtons(XMLNode item)
	{	XGFrame root = new XGFrame(item);

		JButton b;
		int y = 0;
		for(XGModuleType mt : MODULE_TYPES)
		{	if(mt instanceof XGDrumsetModuleType)
			{	b = new JButton("Reset " + mt.getName());
				b.addActionListener((ActionEvent e)->((XGDrumsetModuleType)mt).reset());
				root.add(b, new int[]{0,y++,4,1});
			}
		}
		b = new JButton("XG System On");
		b.addActionListener((ActionEvent e)->XGDevice.device.resetXG(true, true));
		root.add(b, new int[]{0,y++,4,1});

		b = new JButton("Reset All Parameters");
		b.addActionListener((ActionEvent e)->XGDevice.device.resetAll(true, true));
		root.add(b, new int[]{0,y++,4,1});

		return root;
	}


	@Override public String getTag(){	return this.module.getType().getTag();}

	@Override public int getID(){	return this.module.getID();}

	@Override public void propertyChanged(XGProperty p){	this.setTitle(this.getTitle());}

	@Override public String getTitle(){	return XGMainWindow.MAINWINDOW.getTitle() + " - " + this.module;}
}
