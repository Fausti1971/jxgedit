package gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import adress.XGAddressableSet;
import module.XGModule;
import value.XGValue;
import xml.XMLNode;

public interface XGComponent extends GuiConstants
{
	public static final int DIR_X = 1, DIR_Y = 2;

	public static JComponent init(XGModule mod)
	{	XMLNode xml = mod.getGuiTemplate();
		XGAddressableSet<XGValue> set = mod.getFilteredSet();
		if(xml == null) return new XGFrame("no template");
		if(set == null || set.isEmpty()) return new XGFrame("no values");
		return newItem(xml.getChildNode(TAG_ITEM), set);
	}

	private static JComponent newItem(XMLNode n, XGAddressableSet<XGValue> set)
	{	String s = n.getStringAttribute(ATTR_TYPE);
		XGControl type = XGControl.valueOf(s);
		JComponent c = new JButton("unknown control: " + s);
		switch(type)
		{	case auto:		break;
			case envelope:	c = new XGEnvelope(n, set); break;
			case env_point:	break;
			case frame:		c = new XGFrame(n, set); break;
			case knob:		c = new XGKnob2(n, set); break;
			case slider:	break;
			default:		break;
		}
		for(XMLNode x : n.getChildNodes(TAG_ITEM)) c.add(newItem(x, set));
		return c;
	}

/********************************************************************************************/

	public JComponent getJComponent();
}
