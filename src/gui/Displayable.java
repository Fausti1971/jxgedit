package gui;

import java.awt.Component;

/**  qualifiziert das implementierende Object als darstellbar, das heißt, es muss eine JComponent erzeugen können;
 * 
 * @author thomas
 *
 */
public interface Displayable
{
	Component getGuiComponent();
}
