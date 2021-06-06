package config;

import xml.XGProperty;import xml.XMLNode;
/**
 * qualifiziert das implementierende Object als "konfigurierbar", d.h. es muss zwingend über eine eigene XML-Node verfügen (get.Config());
 */
public interface Configurable extends XGPropertyChangeListener
{
	XMLNode getConfig();
}
