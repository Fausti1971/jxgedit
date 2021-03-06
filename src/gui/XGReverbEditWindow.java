package gui;
import javax.swing.*;

public class XGReverbEditWindow extends JFrame
{
	private static XGReverbEditWindow window = null;

	public static void open()
	{	if(window == null) window = new XGReverbEditWindow();
		window.setVisible(true);
		window.toFront();
	}
}
