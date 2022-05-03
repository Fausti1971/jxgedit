package gui;

import xml.XMLNode;import javax.swing.*;import java.awt.*;

public class XGTabbedFrame extends JTabbedPane
{	
	public XGTabbedFrame(XMLNode node)
	{	super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	@Override public void add(Component comp, Object constraint)
	{	if(comp instanceof XGTab)
		{	XGTab tab = (XGTab)comp;
			this.addTab(tab.label, null, tab, tab.title);
		}
	}
}
