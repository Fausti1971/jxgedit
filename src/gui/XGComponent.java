package gui;

import javax.swing.JComponent;
import xml.XMLNode;

public interface XGComponent extends GuiConstants
{
	public static JComponent init(XMLNode xml)
	{	XGFrame root = new XGFrame("Test");
		return root;
	}

/********************************************************************************************/

	public JComponent getJComponent();
}
