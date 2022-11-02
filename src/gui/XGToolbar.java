package gui;

import javax.swing.*;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;

public class XGToolbar extends JToolBar implements XGUI
{

/************************************************************************************************************************/

	private final ActionMap actions = new ActionMap();

	XGToolbar()
	{	this.setFloatable(false);
		this.setRollover(true);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	public void addAction(String key, String name, String desc, ActionListener l)
	{	XGToolbarAction a = new XGToolbarAction(key, l);
		a.putValue(Action.LONG_DESCRIPTION, desc);
		a.putValue(Action.SHORT_DESCRIPTION, desc);
		a.putValue(Action.NAME, name);
		a.putValue(Action.ACCELERATOR_KEY, XGUI.KEYSTROKES.get(key));
		a.putValue(Action.SMALL_ICON, XGUI.loadImage(ICON_KEYS_40.get(key)));
		a.putValue(Action.LARGE_ICON_KEY, XGUI.loadImage(ICON_KEYS_40.get(key)));
		this.add(new JMenuItem(a));//AcceleratorKeys funktionieren nur bei Erben von JMenuItem
	//	DefaultToolBarLayout
	}

/************************************************************************************************************************/

	private class XGToolbarAction extends AbstractAction
	{
		final String key;
		final ActionListener listener;

		XGToolbarAction(String key, ActionListener lstn)
		{	this.key = key;
			this.listener = lstn;
		}

		public void actionPerformed(ActionEvent event)
		{	ActionEvent e = new ActionEvent(this, 0, this.key);
			this.listener.actionPerformed(e);
		}
	}
}
