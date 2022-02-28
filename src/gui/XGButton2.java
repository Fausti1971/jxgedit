package gui;

import javax.swing.JButton;
import static gui.XGUI.MEDIUM_FONT;
import value.XGValue;

/**
* zweizeiliger JButton, erste Zeile Parametername, zweite Zeile Wert
*/
public class XGButton2 extends JButton implements value.XGValueChangeListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*******************************************************************************/

	private final XGValue value;

	public XGButton2(XGValue val)
	{	this.value = val;
		if(val == null)
		{	this.setVisible(false);
			return;
		}
//		this.setHorizontalAlignment(javax.swing.JButton.CENTER);
		this.setText(this.createText());
		this.setFont(MEDIUM_FONT);
		this.value.getValueListeners().add(this);
	}

	private String createText()
	{	return "<html><center><b>" + this.value.getParameter().getName() + "</b><br>" + this.value.toString() + "</center></html>";
	}

	public void contentChanged(value.XGValue v)
	{	this.setText(this.createText());
	}
}
