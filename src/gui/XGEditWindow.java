package gui;

public abstract class XGEditWindow extends XGWindow
{
	private static final java.util.Map<module.XGModule, XGEditWindow> EDITWINDOWS = new java.util.HashMap<>();

	public static XGEditWindow getEditWindow(module.XGModule mod)
	{	if(EDITWINDOWS.containsKey(mod)) return EDITWINDOWS.get(mod);
		else
			switch(mod.getType().getName())
			{	case "rev":		return new gui.XGReverbEditWindow(mod);
				default:		return null;
			}
	}

/***********************************************************************************************************/

	private final module.XGModule module;

	public XGEditWindow(javax.swing.JFrame own, module.XGModule mod, String name)
	{	super(own,name);
		this.module = mod;
		EDITWINDOWS.put(this.module, this);
	}
}
