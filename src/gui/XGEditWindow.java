package gui;

import adress.XGIdentifiable;import config.XGConfigurable;import device.XGDevice;import static gui.XGRange.newRange;import module.XGDrumsetModuleType;import module.XGModule;import module.XGModuleType;import static module.XGModuleType.MODULE_TYPES;import tag.XGTagable;import tag.XGTagableIdentifiableSet;import value.XGValue;import xml.XGProperty;import xml.XMLNode;import javax.swing.*;import java.awt.event.ActionEvent;import java.io.IOException;import java.util.HashMap;import java.util.Map;

public class XGEditWindow extends XGWindow implements XGTagable, XGIdentifiable, XGConfigurable
{
	static final XGTagableIdentifiableSet<XGEditWindow> EDITWINDOWS = new XGTagableIdentifiableSet<>();
	static final Map<String, XMLNode> TEMPLATES = new HashMap<>();

	static public void init()
	{	try
		{	XMLNode xml = XMLNode.parse(XML_TEMPLATES);
			for(XMLNode n : xml.getChildNodes(TAG_TEMPLATE))
			{	String tag = n.getStringAttribute(ATTR_ID);
				if("ds".equals(tag))
				{	for(XGDrumsetModuleType dsmt: XGDrumsetModuleType.DRUMSETS.values())
					{	TEMPLATES.put(dsmt.getTag(), n);
						LOG.info(dsmt.getTag());
					}
					continue;
				}
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
		if(type == null) return new XGFrame("missing type for item: " + item);
		switch(type)
		{	case ATTR_FRAME:
				component = XGFrame.newFrame(item); break;
			case ATTR_KNOB:
				component = new XGKnob(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG))); break;
			case ATTR_RESET_BUTTONS:
				component = createResetButtons(item); break;
			case ATTR_PROG_SELECT:
				component = new XGProgramSelector(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG)));
				break;
			case ATTR_COMBO:
				component = new XGCombo(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG))); break;
			case ATTR_RADIO:
				component = new XGRadio(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG)), XGComponent.XGOrientation.valueOf(item.getStringAttributeOrDefault(ATTR_ORIENTATION, "vertical")));
				break;
			case ATTR_FLAGBOX:
				component = XGFlagBox.newFlagbox(this.module, item); break;
			case ATTR_CHECKBOX:
				component = new XGCheckbox(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG))); break;
			case ATTR_HSLIDER:
				component = new XGSlider(this.module.getValues().get(item.getStringAttribute(ATTR_VALUE_TAG))); break;
			case ATTR_RANGE:
				component = XGRange.newRange(this.module, item); break;
			case ATTR_VELO_ENV:
				component = new XGVelocityEnvelope(this.module); break;
			case ATTR_EQ_ENV:
				component = new XGMEQ(this.module); break;
			case ATTR_PITCH_ENV:
				component = XGPitchEnvelope.newPitchEnvelope(this.module, item); break;
			case ATTR_AMP_ENV:
				component = XGAmplifierEnvelope.newAmplifierEnvelope(this.module, item); break;
			case ATTR_TABBED:
				component = new XGTabbedFrame(item); break;
			case ATTR_TAB:
				component = new XGTab(item); break;
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
	{	XGFrame root = XGFrame.newFrame(item);

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
