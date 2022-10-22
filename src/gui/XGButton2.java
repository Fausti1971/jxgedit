package gui;

import javax.swing.JButton;
import value.XGValue;

/**
* zweizeiliger JButton, erste Zeile Parametername, zweite Zeile Wert
*/
public class XGButton2 extends JButton implements value.XGValueChangeListener
{
/*******************************************************************************/

	private final XGValue value;
	private final String firstRow;

	public XGButton2(String firstRow, XGValue val)
	{	this.value = val;
		this.firstRow = firstRow;
		this.setText(this.createText());
//		this.setFont(MEDIUM_FONT);
		if(this.value != null) this.value.getValueListeners().add(this);
	}

	private String createText()
	{	String s = "-";
		if(this.value != null) s = this.value.toString();
		return "<html><center><b>" + this.firstRow + "</b><br>" + s + "</center></html>";
	}

	public void contentChanged(value.XGValue v){	this.setText(this.createText());}
}
