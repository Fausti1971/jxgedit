package application;

import xml.XMLNode;

public interface Configurable extends ConfigurationConstants
{
	XMLNode getConfig();
}
