package gui;

import adress.XGAddressable;import xml.XGProperty;import static xml.XMLNodeConstants.ATTR_NAME;import javax.swing.*;

public abstract class XGEditWindow extends XGWindow implements XGAddressable
{
	protected static final int GAP = 0;
	static final adress.XGAddressableSet<XGEditWindow> EDITWINDOWS = new adress.XGAddressableSet<>();

	public static XGEditWindow getEditWindow(module.XGModule mod)
	{	adress.XGAddress adr = mod.getAddress();
		String tag = mod.getType().getTag();
		XGEditWindow win = EDITWINDOWS.get(adr);
		if(win != null) return win;
		else
		{	switch(tag)
			{	case "rev":		win = new XGReverbEditWindow(mod); break;
				case "cho":		win = new XGChorusEditWindow(mod); break;
				case "var":		win = new XGVariationEditWindow(mod); break;
				case "ins":		win = new XGInsertionEditWindow(mod); break;
				case "master":	win = new XGMasterEditWindow(mod); break;
				case "eq":		win = new XGEQEditWindow(mod); break;
				case "mp":		win = new XGMultipartEditWindow(mod); break;
				case "ad":		win = new XGADPartEditWindow(mod); break;
				case "ds1":
				case "ds2":
				case "ds3":
				case "ds4":
				case "ds5":
				case "ds6":
				case "ds7":
				case "ds8":
				case "ds9":
				case "ds10":
				case "ds11":
				case "ds12":
				case "ds13":
				case "ds14":
				case "ds15":
				case "ds16":	win = new XGDrumEditWindow(mod); break;
				default:		return win;
			}
			EDITWINDOWS.add(win);
		}
		return win;
	}

/***********************************************************************************************************/

	abstract JComponent createContent();

	final module.XGModule module;

	public XGEditWindow(XGWindow own, module.XGModule mod)
	{	super(own, own.config);
//		own.config.getAttributes().get(ATTR_NAME).getListeners().add((XGProperty p)->{this.setTitle(own.getTitle() + " - " + p.getValue().toString());});
		this.setResizable(false);
		this.module = mod;
	}

	@Override public void propertyChanged(XGProperty p)
	{	this.setTitle(this.getTitle());
	}

	@Override public String getTitle()
	{	return XGMainWindow.window.getTitle() + " - " + this.module;
	}

	@Override public adress.XGAddress getAddress()
	{	return this.module.getAddress();
	}
}
