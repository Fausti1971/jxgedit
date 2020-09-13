package gui;

import application.XGLoggable;

/**
 * qualifiziert das implementierende Objekt als in einem XGTree darstellbar.
 * @author thomas
 *
 */

public interface XGTreeNode extends XGContext, XGLoggable
{

/*****************************************************************************************************************/

/**
 * übergibt den selected-Status an die XGTreeNode, damit diese darauf reagieren kann; Darstellung und Aktualisierung übernimmt weiterhin der XGTree;
 * @param s
 */
	void setSelected(boolean s);

/**
 * returniert den selected-Status der XGTreeNode; erfragt XGTree zur Darstellung
 * @return Status
 */
	boolean isSelected();

/**
 * informiert die XGTreeNode darüber, dass sich der focussed-Status der Node verändert hat und gibt dieser somit die Möglichkeit, darauf zu reagieren;
 * @param b status
 */
	void nodeFocussed(boolean b);

/**
 * erstellt den Text zur Repräsentation im Tree
 * @return text
 */
	String getNodeText();

}
