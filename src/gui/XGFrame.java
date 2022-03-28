package gui;

import java.awt.*;
import javax.swing.*;import javax.swing.border.BevelBorder;

public class XGFrame extends JPanel implements XGComponent
{	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

/**
* erzeugt ein JPanel mit einem XGLayout mit den übergebenen Grid-Dimensionen;
*/
	public XGFrame(int columns, int rows)
	{	this.setLayout(new XGLayout(new Dimension(columns, rows)));
		this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	}

	public XGFrame()
	{	this(1, 1);
	}
}
