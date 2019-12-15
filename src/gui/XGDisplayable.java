package gui;

import javax.swing.JComponent;

/**  qualifiziert das implementierende Object als darstellbar, das heißt, es muss eine JComponent erzeugen können oder sein;
 * 
 * @author thomas
 *
 */
public interface XGDisplayable extends GuiConstants
{
	JComponent getGuiComponent();
}
