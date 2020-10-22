package gui;

import java.awt.Dimension;
import javax.swing.JTabbedPane;
import module.XGModule;
import xml.XMLNode;

public class XGTabFrame extends XGFrame
{
	private static final long serialVersionUID = 1L;
	private static final int TABBEDPANE_OVERHAED_H = 47, TABBEDPANE_OVERHAED_W = 21;

/***********************************************************************************************/

	private final JTabbedPane tabs;

	public XGTabFrame(XMLNode n, XGModule mod)
	{	super(n);
		this.tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		Dimension d = new Dimension();
		for(XMLNode t : n.getChildNodes(TAG_FRAME))
		{	XGFrame f = new XGFrame(t, mod);
			d.width = Math.max(d.width, f.getSize().width);
			d.height = Math.max(d.height, f.getSize().height);
			if(f.getComponentCount() != 0) this.tabs.addTab(f.getName(), f);
		}

		this.tabs.setSize(d.width + TABBEDPANE_OVERHAED_W, d.height + TABBEDPANE_OVERHAED_H);
		this.add(this.tabs);
	}
}
