package config;

import xml.XMLNode;
/**
 * qualifiziert das implementierende Object als "konfigurierbar", d.h. es muss zwingend über eine eigene XML-Node verfügen (get.Config());
 */
public interface Configurable
{
	XMLNode getConfig();
	void configurationChanged(XMLNode node);
}
