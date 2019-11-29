package gui;

import javax.swing.JComponent;
import application.Configurable;

public interface GuiConfigurable extends Configurable, GuiConstants
{
	JComponent getConfigurationGuiComponents();

}
