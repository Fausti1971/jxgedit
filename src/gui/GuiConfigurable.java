package gui;

import java.awt.Component;
import application.Configurable;

public interface GuiConfigurable extends Configurable, GuiConstants
{
	Component getConfigurationGuiComponents();

}
