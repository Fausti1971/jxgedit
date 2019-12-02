package gui;

import java.awt.Component;

/**  qualifiziert das implementierende Object als darstellbar, das heißt, es muss eine JComponent erzeugen können oder sein;
 * 
 * @author thomas
 *
 */
public interface XGDisplayable extends GuiConstants
{
	Component getGuiComponent();
}
