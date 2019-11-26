package gui;

import javax.swing.JComponent;
/**  qualifiziert das implementierende Object als darstellbar, das heißt, es muss eine JComponent mit GUI-Elementen erzeugen können (getGuiElements());
 * 
 * @author thomas
 *
 */
public interface Displayable
{
	JComponent getGuiComponents();
}
