package application;

import xml.XMLNode;

public interface ConfigurationChangeListener extends ConfigurationConstants
{
	public void configurationChanged(XMLNode n);
}
