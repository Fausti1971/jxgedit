package xml;

/**
 * qualifiziert das implementierende Object als "konfigurierbar", d.h. es muss zwingend über eine eigene XML-Node verfügen (get.Config());
 */
public interface XGConfigurable extends XMLNodeConstants
{
	XMLNode getConfig();
}
