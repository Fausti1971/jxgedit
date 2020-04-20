package gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import adress.XGAddressableSet;
import module.XGModule;
import value.XGValue;
import xml.XMLNode;

public interface XGComponent extends GuiConstants
{
	public static final int AXIS_X = 1, AXIS_Y = 2, AXIS_XY = 3, DEF_AXIS = 1;

	public static JComponent init(XGModule mod)
	{	XMLNode xml = mod.getGuiTemplate();
		XGAddressableSet<XGValue> set = mod.getDevice().getValues().getAllValid(mod.getAddress());
		if(xml == null) return new XGFrame("no template");
		if(set == null || set.isEmpty()) return new XGFrame("no values");
		return newItem(xml, set);
	}

	private static JComponent newItem(XMLNode n, XGAddressableSet<XGValue> set)
	{	String s = n.getTag();
		JComponent c = new JButton("unknown " + s);
		switch(s)
		{	case TAG_AUTO:		break;
			case TAG_ENVELOPE:	c = new XGEnvelope(n, set); break;
			case TAG_ENVPOINT:	break;
			case TAG_FRAME:		c = new XGFrame(n, set); break;
			case TAG_KNOB:		c = new XGKnob2(n, set); break;
			case TAG_SLIDER:	break;
			default:			break;
		}
		for(XMLNode x : n.getChildNodes()) c.add(newItem(x, set));
		return c;
	}

	static int getAxis(String s)
	{	if(s != null)
		{	if(s.equals("x")) return AXIS_X;
			if(s.equals("y")) return AXIS_Y;
			if(s.equals("xy")) return AXIS_XY;
		}
		return DEF_AXIS;
	}
/********************************************************************************************/

	public JComponent getJComponent();
}
