package application;

import xml.XMLNode;
/**
 * qualifiziert das implementierende Object als "konfigurierbar", d.h. es muss zwingend über eine eigene XML-Node verfügen (get.Config());
 */
public interface Configurable extends ConfigurationConstants
{
	XMLNode getConfig();
}
