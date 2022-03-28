package gui;

import adress.XGAddress;import adress.XGAddressable;import adress.XGAddressableSet;import module.XGModule;import xml.XGProperty;import static xml.XMLNodeConstants.*;import javax.swing.*;import java.awt.*;import java.awt.event.ComponentEvent;

public abstract class XGEditWindow extends XGWindow implements XGAddressable
{
	static final XGAddressableSet<XGEditWindow> EDITWINDOWS = new XGAddressableSet<>();

	public static XGEditWindow getEditWindow(XGModule mod)
	{	XGAddress adr = mod.getAddress();
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
				default:		return null;
			}
			EDITWINDOWS.add(win);
		}
		return win;
	}

/***********************************************************************************************************/

	abstract JComponent createContent();

	final XGModule module;

	public XGEditWindow(XGWindow own, XGModule mod)
	{	super(own, mod.getType().getTag());
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.module = mod;
		this.setContentPane(this.createContent());
	}

	@Override public String getTag(){	return this.module.getType().getTag();}

	@Override public void propertyChanged(XGProperty p){	this.setTitle(this.getTitle());}

	@Override public String getTitle(){	return XGMainWindow.MAINWINDOW.getTitle() + " - " + this.module;}

	@Override public XGAddress getAddress(){	return this.module.getAddress();}
}
