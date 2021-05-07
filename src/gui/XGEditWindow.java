package gui;

public abstract class XGEditWindow extends gui.XGWindow implements adress.XGAddressable
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
				case "master":	win = new gui.XGMasterEditWindow(mod); break;
				case "eq":		win = new XGEQEditWindow(mod); break;
				case "mp":		win = new XGMultipartEditWindow(mod); break;
				default:		return win;
			}
			EDITWINDOWS.add(win);
		}
		return win;
	}

/***********************************************************************************************************/

	final module.XGModule module;

	public XGEditWindow(XGWindow own, module.XGModule mod, String name)
	{	super(own, name);
		this.module = mod;
	}

	@Override public adress.XGAddress getAddress()
	{	return this.module.getAddress();
	}
}
