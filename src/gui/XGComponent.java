package gui;

import javax.swing.JComponent;

public interface XGComponent extends XGClickable, GuiConstants
{
//	public XGDevice getDevice();
	public JComponent getJComponent();
}
