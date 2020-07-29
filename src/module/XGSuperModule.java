package module;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JComponent;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressableSet;
import adress.XGBulkDump;
import device.TimeoutException;
import device.XGDevice;
import gui.XGComponent;
import gui.XGTemplate;
import gui.XGTree;
import gui.XGTreeNode;
import gui.XGWindow;
import msg.XGMessageDumpRequest;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGSuperModule implements XGModule, XMLNodeConstants, XGValueChangeListener
{	static
	{	ACTIONS.add(ACTION_EDIT);
		ACTIONS.add(ACTION_REQUEST);
		ACTIONS.add(ACTION_TRANSMIT);
		ACTIONS.add(ACTION_LOADFILE);
		ACTIONS.add(ACTION_SAVEFILE);
	}

/***************************************************************************************************************/
/**
 * selected Status für XGTreeNode (besitzt geöffnetes Kindfenster)
 */
	private boolean selected;
/**
 * Referenz auf ein mögliches Kindfenster
 */
	private XGWindow window;
	private final XGTreeNode parent;
	private final XGAddressableSet<XGModule> childModules = new XGAddressableSet<>();
	private final String category;
	private final XGAddress address;
	private final XGDevice device;
	private final XGAddressableSet<XGBulkDump> bulks;
	private final XGTemplate guiTemplate;
	private final Set<XGValue> info = new LinkedHashSet<>();

	public XGSuperModule(XGDevice dev, XGTreeNode par, String cat, XGAddress adr, XMLNode cfg)
	{	this.parent = par;
		this.category = cat;
		this.address = adr;
		this.device = dev;
		if(this.isInstance())
		{	this.bulks = XGBulkDump.init(this, cfg);
			this.registerValueListener(cfg);
		}
		else this.bulks = null;
		this.guiTemplate = dev.getTemplates().getFirstIncluding(this.address);
	}

	void registerValueListener(XMLNode n)
	{	XGValue v = this.device.getValues().get(new XGAddress(n.getStringAttribute(ATTR_INFO1), this.address));
		if(v != null)
		{	this.info.add(v);
			v.addValueListener(this);
		}
		v = this.device.getValues().get(new XGAddress(n.getStringAttribute(ATTR_INFO2), this.address));
		if(v != null)
		{	this.info.add(v);
			v.addValueListener(this);
		}
		v = this.device.getValues().get(new XGAddress(n.getStringAttribute(ATTR_INFO3), this.address));
		if(v != null)
		{	this.info.add(v);
			v.addValueListener(this);
		}
	}

/**
 * sendet für jeden XGBulkDump und jedes childModule je einen request
 */
	@Override public void request()
	{	if(this.isInstance())
		{	for(XGBulkDump b : this.getBulks())
			{	try
				{	new XGMessageDumpRequest(this.getDevice(), this.getDevice().getMidi(), b.getAddress()).request();
				}
				catch(InvalidXGAddressException | InvalidMidiDataException | TimeoutException e)
				{	log.info(e.getMessage());
				}
			}
		}
		else for(XGModule m : this.childModules) m.request();
	}

	@Override public XGDevice getDevice()
	{	return this.device;
	}

	@Override public XGTreeNode getParentNode()
	{	return this.parent;
	}

	@Override public XGAddressableSet<XGModule> getChildModule()
	{	return this.childModules;
	}

	@Override public XGTemplate getGuiTemplate()
	{	return this.guiTemplate;
	}

	@Override public void setTreeComponent(XGTree t)
	{
	}

	@Override public void setSelected(boolean s)
	{	this.selected = s;
	}

	@Override public boolean isSelected()
	{	return this.selected;
	}

	@Override public void nodeFocussed(boolean b)
	{
	}

	@Override public void windowOpened(WindowEvent e)
	{	this.setSelected(true);
		this.repaintNode();
	}

	@Override public void windowClosed(WindowEvent e)
	{	this.setSelected(false);
		this.setChildWindow(null);
		this.repaintNode();
	}

	@Override public Set<String> getContexts()
	{	return ACTIONS;
	}

	@Override public void actionPerformed(ActionEvent e)
	{	log.info(e.getActionCommand());
		switch(e.getActionCommand())
		{	case ACTION_EDIT:		if(!this.isInstance()) break;
									if(this.window == null) new XGWindow(this, XGWindow.getRootWindow(), false, this.toString());
									else this.window.toFront();
									break;
			case ACTION_REQUEST:	this.request(); break;
		}
	}

	@Override public XGWindow getChildWindow()
	{	return this.window;
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.window = win;
	}

	@Override public JComponent getChildWindowContent()
	{	return XGComponent.init(this);
	}

	@Override public XGAddress getAddress()
	{	return this.address;
	}

	@Override public String getCategory()
	{	return this.category;
	}

	@Override public String toString()
	{	if(this.isInstance())
		{	String s = this.address.getMid().toString() + ": ";
			for(XGValue v : this.info) s += v.getInfo() + " ";
			return s;
		}
		else return this.category + " (" + this.getChildCount() + ")";
	}

	@Override public XGAddressableSet<XGBulkDump> getBulks()
	{	return this.bulks;
	}

	@Override public void contentChanged(XGValue v)
	{	this.repaintNode();
	}
}
